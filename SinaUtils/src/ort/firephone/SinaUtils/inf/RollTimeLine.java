package ort.firephone.SinaUtils.inf;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.firephone.SinaUtil.cache.FcorpseCache;
import org.firephone.SinaUtil.cache.PhpProxyCache;
import org.firephone.SinaUtil.err.OauthErr;

import com.skyzoo.Jutil.EatAppleMuti;
import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.FSstatus;
import ort.firephone.SinaUtils.db.FSstatusU;
import ort.firephone.SinaUtils.db.FSuser;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fuser;
import ort.firephone.SinaUtils.db.UserInfoAction;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class RollTimeLine extends EatAppleMuti {
	public static Hashtable<Long, Integer> c_user_ids = new Hashtable<Long, Integer>();
	public static Hashtable<Long, Integer> c_status_ids = new Hashtable<Long, Integer>();
	final public static int ONCE_MAX = 5000;
	final public static int ONCE_BUF = 500;
	final public static int retweeted_NO = -1;

	public static void main(String[] args) {
		Env.initial();
		RollTimeLine rollTimeLine = new RollTimeLine();
		rollTimeLine.start();
		rollTimeLine.processAllApple();
	}

	public RollTimeLine() {
		this.setThreadNum(2);
		this.setEatGap(200);
		this.setMaxSize(50000);
	}

	@Override
	public boolean eat(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		Long user_id = (Long) arg0;
		boolean proc_status = false;
		Long start_t = 0L, endGet_t = 0L, proc_t = 0L;
		int userTimeLineCount = 0;
		try {
			start_t = System.currentTimeMillis();
			List<Status> statuses = getWeiboUserTimeline(user_id);

			endGet_t = System.currentTimeMillis();
			if (statuses != null) {
				processAllOfUser(statuses);
				userTimeLineCount = statuses.size();
				//
				proc_t = System.currentTimeMillis();
				Log.log("user_id=" + user_id + "  get_t="
						+ (endGet_t - start_t) + "  db_t="
						+ (proc_t - endGet_t) + " statuses="
						+ userTimeLineCount);
				proc_status = true;

			} else {
				Log.log("user_id=" + user_id + "  get_t="
						+ (endGet_t - start_t) + "  db_t="
						+ (proc_t - endGet_t) + " fail");
			}
		} catch (Exception ee) {
			ee.printStackTrace();

		}

		try {
			UserInfoAction userInfoAction = new UserInfoAction();
			userInfoAction.setId(user_id + "");
			userInfoAction.setType(UserInfoAction.TYPE_NORMAL);
			userInfoAction.setAction_step(UserInfoAction.STEP_STATUS);
			userInfoAction.updateToDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc_status;
	}

	public List<Status> getWeiboUserTimeline(long user_id) {
		int tryNum = 3;
		String s = "";
		for (int i = 0; i < tryNum; i++) {
			try {
				Fcorpse fc = FcorpseCache.getIns().getNextOne();
				Weibo w = getWeibo(fc);
				s = PhpProxyCache.getIns().getNext();
				w.setPhpProxyURL(s);
				List<Status> statuses = w.getUserTimeline("" + user_id,
						new Paging(1, 200));
				if (statuses.size() == 0) {
					// 可能出错了，
					continue;
				}

				if (statuses.size() >= 190) {
					// 取200不一定能完全得到200,
					List<Status> statuses_next = w.getUserTimeline(
							"" + user_id, new Paging(2, 200));
					statuses.addAll(statuses_next);
				}
				return statuses;
			} catch (WeiboException e) {
				int errType = OauthErr.getErrType(e);
				String e_msg = "user_id=" + user_id + " p=(" + s + ") "
						+ e.getMessage();
				if (e_msg.length() > 256) {
					e_msg = e_msg.substring(0, 256) + " ... ";
				}
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

	/**
	 * 多线程处理启动后，用此方法不停向线程池放入要处理的ID号，直到ID放完结束
	 */
	public void processAllApple() {
		try {
			Vector<String> all_user_ids = UserInfoAction.getInfoedId(ONCE_MAX);
			while (all_user_ids.size() > 0) {
				for (int i = 0; i < all_user_ids.size(); i++) {
					long user_id = Long.parseLong(all_user_ids.get(i));
					if (FSstatusU.isExist(user_id)) {
						Log.log(Log.Severity_Informational, "omit user_id="
								+ user_id);
						continue;
					}
					this.addApple(user_id);
					try {
						if (this.getAppleCount() > ONCE_BUF) {
							Log.log("buffer is full   wait ... ");
							Thread.sleep(5000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 等待处理完成,因为每次批量取的值和处理结果相关。所以每次批量取之前必须等上一批次处理完毕
				Log.log("Once (" + all_user_ids.size() + ") End --- wait left="
						+ this.getAppleCount());
				while (true) {
					try {
						if (this.getAppleCount() > 0) {
							Thread.sleep(1000);
						} else {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				all_user_ids = UserInfoAction.getInfoedId(ONCE_MAX);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 等待处理完成
		while (true) {
			try {
				if (this.getAppleCount() > 0) {
					Log
							.log("-----GetTimeLine processing will end soon----- left "
									+ this.getAppleCount());
					Thread.sleep(1000);
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.log("process all status end");
	}

	/**
	 * 多线程未启，单线程一个一个处理，真到处理完成。
	 */
	public void processAllSingleThread() {
		try {
			Vector<String> all_user_ids = UserInfoAction.getInfoedId(ONCE_MAX);
			while (all_user_ids.size() > 0) {
				for (int i = 0; i < all_user_ids.size(); i++) {
					long user_id = Long.parseLong(all_user_ids.get(i));
					if (FSstatusU.isExist(user_id)) {
						Log.log(Log.Severity_Informational, "omit user_id="
								+ user_id);
						continue;
					}
					this.eat(user_id);
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 等待处理完成,因为每次批量取的值和处理结果相关。所以每次批量取之前必须等上一批次处理完毕
				Log.log("Once (" + all_user_ids.size() + ") End --- wait left="
						+ this.getAppleCount());
				while (true) {
					try {
						if (this.getAppleCount() > 0) {
							Thread.sleep(1000);
						} else {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				all_user_ids = all_user_ids = UserInfoAction
						.getInfoedId(ONCE_MAX);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 等待处理完成
		while (true) {
			try {
				if (this.getAppleCount() > 0) {
					Log
							.log("-----GetTimeLine processing will end soon----- left "
									+ this.getAppleCount());
					Thread.sleep(1000);
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.log("-----GetTimeLine process end");
	}

	public synchronized boolean checkAndSetUserId(long id) {
		if (c_user_ids.containsKey(id)) {
			return false;
		}
		c_user_ids.put(id, 1);
		return true;
	}

	public synchronized boolean checkAndSetStatusId(long id) {
		if (c_status_ids.containsKey(id)) {
			return false;
		}
		c_status_ids.put(id, 1);
		return true;
	}

	public void processAllOfUser(List<Status> statuses) {
		long t1 = System.currentTimeMillis();
		Vector<FSstatus> all_fsstatuses = new Vector<FSstatus>();
		Vector<FSuser> all_fsusers = new Vector<FSuser>();
		for (int j = 0; j < statuses.size(); j++) {
			Status status = statuses.get(j);
			// 每条信息同时包含了一个用户信息，
			if (checkAndSetUserId(status.getUser().getId())) {
				all_fsusers.add(newFSuser(status.getUser()));
			}
			if (checkAndSetStatusId(status.getId())) {
				all_fsstatuses.add(newFstatus(status));
			}

			if (status.isRetweet()) {
				// 转发同时有转发的玩意，相当于完整的一条
				try {
					if (status.getRetweeted_status().getText().equals(
							"此微博已被原作者删除。")) {
						// Log.log("user_id=" + status.getUser().getId() +
						// " 转发的 "
						// + status.getRetweeted_status().getText());
					} else {
						if (checkAndSetUserId(status.getRetweeted_status()
								.getUser().getId())) {
							all_fsusers.add(newFSuser(status
									.getRetweeted_status().getUser()));
						}
						if (checkAndSetStatusId(newFstatus(
								status.getRetweeted_status()).getId())) {
							all_fsstatuses.add(newFstatus(status
									.getRetweeted_status()));
						}
					}
				} catch (Exception r_e) {
					// 可能有异常，比如原微博已被删除之类，不影响
					r_e.printStackTrace();
				}
			}
		}
		//
		long t2 = System.currentTimeMillis();

		Vector<Long> all_fsusers_id = new Vector<Long>();
		for (int j = 0; j < all_fsusers.size(); j++) {
			all_fsusers_id.add(all_fsusers.get(j).getId());
		}
		Hashtable<Long, Integer> fsusers_exist_id = FSuser
				.getExistHash(all_fsusers_id);
		for (int j = all_fsusers.size() - 1; j >= 0; j--) {
			if (fsusers_exist_id.containsKey(all_fsusers.get(j).getId())) {
				all_fsusers.remove(j);
			} else {
				fsusers_exist_id.put(all_fsusers.get(j).getId(), 1);
			}
		}
		long t3 = System.currentTimeMillis();
		FSuser.insertIntoDb(all_fsusers);
		long t4 = System.currentTimeMillis();
		// status
		Vector<Long> all_fsstatuses_id = new Vector<Long>();
		for (int j = 0; j < all_fsstatuses.size(); j++) {
			all_fsstatuses_id.add(all_fsstatuses.get(j).getId());
		}
		Hashtable<Long, Integer> fsstatuses_exist_id = FSstatus
				.getExistHash(all_fsstatuses_id);
		for (int j = all_fsstatuses.size() - 1; j >= 0; j--) {
			if (fsstatuses_exist_id.containsKey(all_fsstatuses.get(j).getId())) {
				all_fsstatuses.remove(j);
			} else {
				fsstatuses_exist_id.put(all_fsstatuses.get(j).getId(), 1);
			}
		}
		long t5 = System.currentTimeMillis();
		FSstatus.insertIntoDb(all_fsstatuses);
		long t6 = System.currentTimeMillis();
		Log.log("hash_t=" + (t2 - t1) + " udb_c=" + (t3 - t2) + " udb_i="
				+ (t4 - t3) + " sdb_c=" + (t5 - t4) + " sdb_i=" + (t6 - t5));
	}

	/**
	 * 一条一条处理取到的status
	 * 
	 * @param status
	 */
	public void processOne(Status status) {
		if (!FSuser.isExist(status.getUser().getId())) {
			newFSuser(status.getUser()).insertIntoDB();
		}
		if (!FSstatus.isExist(status.getId())) {
			newFstatus(status).insertIntoDb();
		}
		if (status.isRetweet()) {
			if (!FSuser.isExist(status.getRetweeted_status().getUser().getId())) {
				newFSuser(status.getRetweeted_status().getUser())
						.insertIntoDB();
			}
			if (!FSstatus.isExist(status.getRetweeted_status().getId())) {
				newFstatus(status.getRetweeted_status()).insertIntoDb();
			}
		}
	}

	public static void doError(int code, Fcorpse fm) {
		try {
			if (code == 403) {
				// 取的频率太高
				Thread.sleep(5 * 60 * 1000);
			}
			Log.log(Log.Severity_Informational, fm.getSina_user() + "--"
					+ fm.getSina_pass());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FSuser newFSuser(User user) {
		FSuser fuser = null;
		try {
			fuser = new FSuser();
			fuser.setId(user.getId());
			fuser.setScreen_name(user.getScreenName());
			fuser.setName(user.getName());
			fuser.setProvince(user.getProvince());
			fuser.setCity(user.getCity());
			fuser.setLocation(user.getLocation());
			fuser.setDescription(user.getDescription());
			if (user.getURL() != null) {
				fuser.setUrl(user.getURL().toString());
			} else {
				fuser.setUrl("");
			}
			fuser.setProfile_image_url(user.getProfileBackgroundImageUrl());
			fuser.setDomain(user.getUserDomain());
			fuser.setGender(user.getGender());
			fuser.setFollowers_count(user.getFollowersCount());
			fuser.setFriends_count(user.getFriendsCount());
			fuser.setStatuses_count(user.getStatusesCount());
			fuser.setFavourites_count(user.getFavouritesCount());
			fuser.setCreated_at(user.getCreatedAt());
			fuser.setGeo_enabled(user.isGeoEnabled());
			fuser.setVerified(user.isVerified());
		} catch (Exception e) {
			user = null;
			e.printStackTrace();
		}
		return fuser;
	}

	public static FSstatus newFstatus(Status status) {
		FSstatus fstatus = null;
		try {
			fstatus = new FSstatus();
			fstatus.setId(status.getId());
			fstatus.setCreated_at(status.getCreatedAt());
			fstatus.setText(status.getText());
			fstatus.setSource(status.getSource());
			fstatus.setFavorited(status.isFavorited());
			fstatus.setTruncated(status.isTruncated());
			fstatus.setIn_reply_to_status_id(status.getInReplyToStatusId());
			fstatus.setIn_reply_to_user_id(status.getInReplyToUserId());
			fstatus.setIn_reply_to_screen_name(status.getInReplyToScreenName());
			try {
				fstatus.setMid(0L);
				if (status.getMid() != null && !status.getMid().equals("null")) {
					fstatus.setMid(Long.parseLong(status.getMid()));
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			fstatus.setUser_id(status.getUser().getId());
			fstatus.setUser_screen_name(status.getUser().getScreenName());
			if (status.isRetweet()) {
				fstatus.setRetweeted_id(status.getRetweeted_status().getId());
			} else {
				fstatus.setRetweeted_id(retweeted_NO);
			}
			// fstatus.insertIntoDb();
		} catch (Exception e) {
			fstatus = null;
			e.printStackTrace();
		}
		return fstatus;
	}

	private static Weibo getWeibo(Fcorpse fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}

}
