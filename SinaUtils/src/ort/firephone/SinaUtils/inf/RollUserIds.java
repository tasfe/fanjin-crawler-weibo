package ort.firephone.SinaUtils.inf;

import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;
import org.firephone.SinaUtil.err.OauthErr;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Ffollowers;
import ort.firephone.SinaUtils.db.Ffriends;
import ort.firephone.SinaUtils.db.UserIdAction;
import ort.firephone.SinaUtils.db.UserInfoAction;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class RollUserIds {

	public static void main(String[] args) {

		try {
			Env.initial();
			RollUserIds rollUserIds = new RollUserIds();
			rollUserIds.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Vector<UserIdAction> f_u = null;
		if (Env.RollUserIds_offset == 0) {
			f_u = UserIdAction.getReserve(1000);
		} else {
			f_u = UserIdAction.getReserveOffset(1000, Env.RollUserIds_offset);
		}
		while (f_u.size() != 0) {
			for (int i = 0; i < f_u.size(); i++) {
				UserIdAction one = f_u.get(i);
				try {

					Fcorpse fc = FcorpseCache.getIns().getNextOne();
					String ret = doFriendsOnce(Long.parseLong(one.getId()), fc);
					Log.log(ret);

					fc = FcorpseCache.getIns().getNextOne();
					ret = doFollowsOnce(Long.parseLong(one.getId()), fc);
					Log.log(ret);
					one.setAction_step(UserIdAction.STEP_INFO);
					one.updateToDb();
				} catch (Exception e) {
					e.printStackTrace();
					one.setType(UserIdAction.TYPE_ERROR);
					one.updateToDb();
				}
			}
			if (Env.RollUserIds_offset == 0) {
				f_u = UserIdAction.getReserve(1000);
			} else {
				f_u = UserIdAction.getReserveOffset(1000,
						Env.RollUserIds_offset);
			}
			Log.log("RollUserIds 100");
		}
		Log.log("GetFollowerIds end........");
	}

	public static String doFollowsOnce(long source_id, Fcorpse fm) {
		String ret = "";
		int addNum = 0;
		Long t0 = System.currentTimeMillis();
		Vector<Long> v_this = getFollowerIds(source_id, fm);
		ret += "get by (" + fm.getSina_user() + ")from weibo.com(" + source_id
				+ ") count=" + v_this.size() + "\n";
		Long t1 = System.currentTimeMillis();
		// addNum = Ffollowers.tryAddToDb(source_id, v_this);
		addNum = saveToFollowsId(source_id, v_this);
		Long t2 = System.currentTimeMillis();
		saveToUserId(v_this);
		Long t3 = System.currentTimeMillis();
		ret += "Follows new_id count=" + addNum + " get=" + (t1 - t0)
				+ " saveToFollowsId=" + (t2 - t1) + " saveToUserId="
				+ (t3 - t2) + "\n";
		return ret;
	}

	public static int saveToUserId(Vector<Long> v_this) {
		int num = 0;
		Vector<String> vs = new Vector<String>();
		for (int i = 0; i < v_this.size(); i++) {
			vs.add(v_this.get(i) + "");
			if (i % 500 == 499) {
				num += UserInfoAction
						.tryInsertToDb(vs, UserInfoAction.TYPE_NORMAL,
								UserInfoAction.STEP_RESERVE);
				vs.clear();
			}
		}
		num += UserInfoAction.tryInsertToDb(vs, UserInfoAction.TYPE_NORMAL,
				UserInfoAction.STEP_RESERVE);
		return num;
	}

	public static int saveToFollowsId(long source_id, Vector<Long> v_this) {
		int num = 0;
		Vector<Long> vs = new Vector<Long>();
		for (int i = 0; i < v_this.size(); i++) {
			vs.add(v_this.get(i));
			if (i % 500 == 499) {
				num += Ffollowers.tryInsertToDb(source_id, vs);
				vs.clear();
			}
		}
		num += Ffollowers.tryInsertToDb(source_id, vs);
		return num;
	}

	public static String doFriendsOnce(long source_id, Fcorpse fm) {
		String ret = "";
		int addNum = 0;
		Vector<Long> v_this = getFriendsIds(source_id, fm);
		ret += "Friends get by (" + fm.getSina_user() + ")from weibo.com("
				+ source_id + ") count=" + v_this.size() + "\n";
		addNum = Ffriends.tryAddToDb(source_id, v_this);
		saveToUserId(v_this);
		ret += "Friends new_id count=" + addNum + "\n";
		return ret;
	}

	public static Vector<Long> getFollowerIds(long source_id, Fcorpse fm) {
		Vector<Long> follower_ids = new Vector<Long>();
		for (int kk = 0; kk < 3; kk++) {
			try {
				long[] ids = getWeibo(fm).getFollowersIDs((int) source_id, 0)
						.getIDs();
				long[] ids2 = new long[0];
				if (ids.length == 5000) {
					ids2 = getWeibo(fm).getFollowersIDs((int) source_id, 4999)
							.getIDs();
				}
				for (int i = 0; i < ids.length; i++) {
					follower_ids.add(new Long(ids[i]));
				}
				for (int i = 0; i < ids2.length; i++) {
					follower_ids.add(new Long(ids2[i]));
				}
				return follower_ids;
			} catch (WeiboException e) {
				int errType = OauthErr.getErrType(e);
				String e_msg = "getFriendsIds source_id=" + source_id
						+ e.getMessage();
				if (errType == OauthErr.TypeForRetry) {
					Log.log(e_msg + " TypeForRetry");
					continue;
				} else if (errType == OauthErr.TypeObjectNotExist) {
					Log.log(e_msg + " TypeObjectNotExist");
					return null;
				} else if (errType == OauthErr.TypeReject) {
					Log.log(e_msg + " TypeReject");
					break;
				} else if (errType == OauthErr.TypeActionRateLimit) {
					Log.log(e_msg + " TypeActionRateLimit wait ... ");
					sleep(2 * 60 * 1000L);
					continue;
				} else if (errType == OauthErr.TypeIpRateLimit) {
					Log.log(e_msg + " TypeIpRateLimit wait ... ");
					sleep(5 * 60 * 1000L);
					continue;
				} else {
					Log.log(e_msg + " Unknown wait ... ");
					sleep(1 * 60 * 1000L);
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return follower_ids;
	}

	public static Vector<Long> getFriendsIds(long source_id, Fcorpse fm) {
		Vector<Long> friend_ids = new Vector<Long>();
		for (int kk = 0; kk < 3; kk++) {
			try {
				long[] ids = getWeibo(fm).getFriendsIDs((int) source_id, 0)
						.getIDs();
				long[] ids2 = new long[0];
				if (ids.length == 5000) {
					ids2 = getWeibo(fm).getFriendsIDs((int) source_id, 4999)
							.getIDs();
				}
				for (int i = 0; i < ids.length; i++) {
					friend_ids.add(new Long(ids[i]));
				}
				for (int i = 0; i < ids2.length; i++) {
					friend_ids.add(new Long(ids2[i]));
				}
				return friend_ids;
			} catch (WeiboException e) {
				int errType = OauthErr.getErrType(e);
				String e_msg = "getFriendsIds source_id=" + source_id
						+ e.getMessage();
				if (errType == OauthErr.TypeForRetry) {
					Log.log(e_msg + " TypeForRetry");
					continue;
				} else if (errType == OauthErr.TypeObjectNotExist) {
					Log.log(e_msg + " TypeObjectNotExist");
					return null;
				} else if (errType == OauthErr.TypeReject) {
					Log.log(e_msg + " TypeReject");
					break;
				} else if (errType == OauthErr.TypeActionRateLimit) {
					Log.log(e_msg + " TypeActionRateLimit wait ... ");
					sleep(2 * 60 * 1000L);
					continue;
				} else if (errType == OauthErr.TypeIpRateLimit) {
					Log.log(e_msg + " TypeIpRateLimit wait ... ");
					sleep(5 * 60 * 1000L);
					continue;
				} else {
					Log.log(e_msg + " Unknown wait ... ");
					sleep(1 * 60 * 1000L);
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return friend_ids;
	}

	public static void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Weibo getWeibo(Fcorpse fc) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fc.getApp_key(), fc.getApp_secret());
		weibo.setToken(fc.getOauth_token(), fc.getOauth_token_secret());
		return weibo;
	}

}
