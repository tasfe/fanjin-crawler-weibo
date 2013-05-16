package ort.firephone.SinaUtils.inf;

import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;
import org.firephone.SinaUtil.cache.PhpProxyCache;
import org.firephone.SinaUtil.err.OauthErr;

import com.skyzoo.Jutil.EatAppleMuti;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Ffollowers;
import ort.firephone.SinaUtils.db.Fuser;
import ort.firephone.SinaUtils.db.FuserOmit;
import ort.firephone.SinaUtils.db.RunParam;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class GetUserInfo extends EatAppleMuti {
	public static void main(String[] args) {

		try {
			Env.initial();
			GetUserInfo getUserInfo = new GetUserInfo();
			getUserInfo.start();
			getUserInfo.processAllApple();
			Log.log("-----End----- GetUserInfo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GetUserInfo() {
		this.setThreadNum(12);
		this.setEatGap(200);
	}

	/**
	 * 多线程处理启动后，用此方法不停向线程池放入要处理的ID号，直到ID放完结束
	 */
	public void processAllApple() {
		int once_max = 1000;
		RunParam limitNumParam = getLimitNumParam();
		Vector<Long> vl = Ffollowers.getAllLimited(
				limitNumParam.getValue_int(), once_max);
		while (vl.size() > 0) {
			for (int i = 0; i < vl.size(); i++) {
				try {
					long u_id = vl.get(i);
					if (this.getAppleCount() > 500) {
						Log.log("array is full  waiting ... ...");
						sleep(5000L);
					}
					if (Fuser.isExist(u_id)) {
						Log.log(Log.Severity_Informational, "already id="
								+ u_id);
						continue;
					}
					if (FuserOmit.isExist(u_id)) {
						Log.log(Log.Severity_Informational, "omit   id=" + u_id);
						continue;
					}
					// 多线程处理，放入线程池中
					addApple(u_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			limitNumParam.setValue_int(limitNumParam.getValue_int() + once_max);
			limitNumParam.updateToDb();
			vl = Ffollowers.getAllLimited(limitNumParam.getValue_int(),
					once_max);
			Log.log("start new arrray" + vl.size());
		}

		while (true) {
			Log.log("-----GetUserInfo processing will end soon----- left "
					+ this.getAppleCount());
			if (this.getAppleCount() != 0) {
				sleep(1000L);
			} else {
				break;
			}
		}
	}

	public RunParam getLimitNumParam() {
		RunParam runParam;
		runParam = RunParam.getByName(RunParam.USERINFO_PROC_NUM);
		if (runParam == null) {
			runParam = new RunParam();
			runParam.setName(RunParam.USERINFO_PROC_NUM);
			runParam.setType(1);
			runParam.setValue("");
			runParam.setValue_int(1);
		}
		return runParam;
	}

	@Override
	public boolean eat(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		Long user_id = (Long) arg0;
		try {
			Long start_t = System.currentTimeMillis();
			User user = getWeiboUserInfo(user_id);
			Long endG_t = System.currentTimeMillis();
			if (user != null) {
				Fuser.insertIntoDb(user);
				Long end_t = System.currentTimeMillis();
				Log.log(" do " + user_id + " (" + user.getName() + ")   g_t="
						+ (endG_t - start_t) + " p_t=" + (end_t - endG_t)
						+ "  oked");
				return true;
			} else {
				FuserOmit omit = new FuserOmit();
				omit.setSina_id(user_id);
				omit.setCode(400);
				omit.insertIntoDb();
				return false;
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return false;
	}

	public User getWeiboUserInfo(long user_id) {
		String phpProxy = "";
		for (int i = 0; i < Env.oauth_get_try_num; i++) {
			try {
				Fcorpse fc = FcorpseCache.getIns().getNextOne();
				phpProxy = PhpProxyCache.getIns().getNext();
				Weibo w = getWeibo(fc);
				w.setPhpProxyURL(phpProxy);
				User user = w.showUser("" + user_id);
				if (user.getId() == 0) {
					Log.log(user_id + "  failure  mybe user not exist!");
					return null;
				}
				return user;
			} catch (WeiboException e) {
				int errType = OauthErr.getErrType(e);
				String e_msg = "user_id=" + user_id + " phpProxy=" + phpProxy
						+ e.getMessage();
				if (errType == OauthErr.TypeForRetry) {
					Log.log(e_msg + " TypeForRetry");
					continue;
				} else if (errType == OauthErr.TypeObjectNotExist) {
					Log.log(e_msg + " TypeObjectNotExist");
					break;
				} else if (errType == OauthErr.TypeReject) {
					Log.log(e_msg + " TypeReject");
					break;
				} else if (errType == OauthErr.TypeActionRateLimit) {
					Log.log(e_msg + " TypeActionRateLimit");
					sleep(1 * 60 * 1000L);
					break;
				} else if (errType == OauthErr.TypeIpRateLimit) {
					Log.log(e_msg + " TypeIpRateLimit");
					sleep(5 * 60 * 1000L);
					continue;
				} else {
					Log.log(e_msg + " Unknown");
					sleep(2 * 60 * 1000L);
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void sleep(long usec) {
		try {
			Thread.sleep(usec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doError(long sina_id, int code) {
		try {
			if (code == 400) {
				// 用户不存在了，可能是被删了
				FuserOmit userOmit = new FuserOmit();
				userOmit.setSina_id(sina_id);
				userOmit.setCode(code);
				userOmit.insertIntoDb();
			}
			if (code == 403) {
				// 取的频率太高
				Thread.sleep(5 * 60 * 1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Weibo getWeibo(Fcorpse fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}

}
