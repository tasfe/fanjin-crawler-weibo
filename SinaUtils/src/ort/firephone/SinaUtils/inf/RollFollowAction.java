package ort.firephone.SinaUtils.inf;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;
import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.ActionLog;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fmember;
import ort.firephone.SinaUtils.db.FollowAction;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class RollFollowAction extends Thread {
	private long last_run_sec = 0L;
	/**
	 * 收到接口报超过rate_limit后，要保证在一定时间（1个小时）内不再有操作
	 */
	public Hashtable<String, Long> user_rate_limit = new Hashtable<String, Long>();
	public long user_rate_limit_timeout = 60 * 60 * 1000L;

	/**
	 * 多少秒数轮巡一次，一般超过一小时（3600）
	 */
	public int gap_sec = 4000;
	/**
	 * 每次处理多少个目标
	 */
	public int once_num = 20;
	/**
	 * 加了目标后，等多少秒后，目标没有回粉就去删掉
	 */
	public int check_time_sec = 2 * 24 * 3600;
	/**
	 * 对已经成功的目标，多少时间重新检测
	 */
	public int review_time_sec = 12 * 24 * 3600;

	public boolean follow_comment = true;
	public int last_DoFollowed_day = 0;

	public static void main(String argv[]) {
		Env.initial();
		RollFollowAction roll_one = new RollFollowAction();
		// roll_one.roll_once();
		roll_one.DoCheatAll();
	}



	public void run() {

	}

	/**
	 * 检查和最后一次被服务器拒绝的时间差，如果一定时间差内不要有操作，小心被封号。
	 * 
	 * @param fm_id
	 * @return
	 */
	private boolean checkRateLimit(String fm_id) {
		if (user_rate_limit.containsKey(fm_id)) {
			return (System.currentTimeMillis() - user_rate_limit.get(fm_id)) > user_rate_limit_timeout;
		}
		return true;
	}

	public void try_roll_once() {
		// 间隔一个小时多一点点，因为新浪以每小时统计rate limit ;
		if (System.currentTimeMillis() / 1000L - last_run_sec > gap_sec) {
			roll_once();
		}
	}

	public void roll_once() {
		last_run_sec = System.currentTimeMillis() / 1000L;
		Vector<Fmember> fms = Fmember.getAllActive();
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		int hour_of_day = cd.get(Calendar.HOUR_OF_DAY);
		int day_of_month = cd.get(Calendar.DAY_OF_MONTH);
		int day_of_year = cd.get(Calendar.DAY_OF_YEAR);
		int reserve_num = 0;
		int following_num = 0;
		int followed_num = 0;
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm_one = fms.get(i);
			if (hour_of_day < 8 || hour_of_day >= 22) {
				if (checkRateLimit(fm_one.getSina_id() + "")) {
					reserve_num = DoReserve(fm_one, 1);
				}
			} else {
				if (checkRateLimit(fm_one.getSina_id() + "")) {
					reserve_num = DoReserve(fm_one, once_num);
				}
			}
			if (hour_of_day >= 22) {
				reserve_num += DoReserve(fm_one, once_num * 10);
				following_num = DoFollowing(fm_one, once_num * 24);
				if ((day_of_month == 1 || day_of_month == 15)
						&& (last_DoFollowed_day != day_of_year)) {
					last_DoFollowed_day = day_of_year;
					followed_num = DoFollowed(fm_one, 10000);
				}
			}
			Log.log(fm_one.getSina_id() + "  DoReserve=" + reserve_num
					+ " DoFollowing=" + following_num + " DoFollowed="
					+ followed_num);
		}
	}

	public void DoFollowingAll() {
		Vector<Fmember> fms = Fmember.getAllActive();
		String log_str = "";
		int num = 0;
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm_one = fms.get(i);
			if (checkRateLimit(fm_one.getSina_id() + ""))
				num = DoFollowing(fm_one, 10000);
			log_str += fm_one.getSina_user() + "(" + fm_one.getSina_id()
					+ ") DoFollowing num=" + num;
			Log.log(log_str);
		}
	}

	public void DoCheatAll() {
		Vector<Fmember> fms = Fmember.getAllActive();
		String log_str = "";
		int num = 0;
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm_one = fms.get(i);
			if (checkRateLimit(fm_one.getSina_id() + ""))
				num = DoCheate(fm_one, 10000);
			log_str += fm_one.getSina_user() + "(" + fm_one.getSina_id()
					+ ") DoCheat num=" + num + " size="+fms.size();
			Log.log(log_str);
		}
	}
	public void DoUnCheatAll() {
		Vector<Fmember> fms = Fmember.getAllActive();
		String log_str = "";
		int num = 0;
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm_one = fms.get(i);
			if (checkRateLimit(fm_one.getSina_id() + ""))
				num = DoUnCheat(fm_one, 50);
			log_str += fm_one.getSina_user() + "(" + fm_one.getSina_id()
					+ ") DoCheat num=" + num;
			Log.log(log_str);
		}
	}
	/**
	 * 处理新ID，
	 * 
	 * @param reserve
	 */
	public int DoReserve(Fmember fm_one, int max) {
		ActionLog actionLog = new ActionLog();
		actionLog.setSrc_id(fm_one.getSina_id());
		actionLog.setDest_id(0);
		actionLog.setType(ActionLog.TYPE_FOLLOWME_DOFOLLOW);
		actionLog.setTime(new Date());
		actionLog.setText("");

		int successed_num = 0;
		int error_num = 0;

		for (int try_roll_num = 0; try_roll_num < 5; try_roll_num++) {
			Vector<FollowAction> reserves = FollowAction.getByTypeAndStep(""
					+ fm_one.getSina_id(), FollowAction.ACTION_TYPE_FOLLOWME,
					FollowAction.ACTION_STEP_RESERVE, max);
			for (int i = 0; i < reserves.size(); i++) {
				if ((successed_num >= max) || (error_num > max * 2)) {
					break;
				}
				FollowAction one = reserves.get(i);
				int ret_status = WBcreateFriendship(one, fm_one);
				switch (ret_status) {
				case 1:
					successed_num++;
					actionLog.addText(one.getOauth_id() + "=1;");
					break;
				case 2:
					// rate limit
					error_num += 10000;
					actionLog.addText(one.getOauth_id() + "=2;");
					break;
				case 3:
					actionLog.addText(one.getOauth_id() + "=3;");
					continue;
				case 4:
				default:
					actionLog.addText(one.getOauth_id() + "=4;");
					error_num++;
					break;
				}
			}
			if ((successed_num >= max) || (error_num > max * 2)) {
				break;
			}
		}
		actionLog.setStatus(ActionLog.STATUS_SUCCESSED);
		actionLog.insertToDb();
		System.out.println("DoReserve : " + actionLog.getText());
		return successed_num;
	}

	/**
	 * 
	 * @param one
	 * @return 1:successed,2:limited,3:this one not
	 *         fit(already_followed?)4:unknown error.
	 */
	public int WBcreateFriendship(FollowAction one, Fmember fm) {
		try {
			Fcorpse fc = FcorpseCache.getIns().getNextOne();
			Date last_post = null;
			try {
				try {
					List<Status> list_status = getWeibo(fc).getUserTimeline(
							one.getOauth_id());
					if (list_status != null && list_status.size() > 0) {
						Status one_status = list_status.get(0);
						last_post = one_status.getCreatedAt();
						Calendar cd = Calendar.getInstance();
						cd.setTime(last_post);
						int post_day_of_year = cd.get(Calendar.DAY_OF_YEAR);
						cd.setTime(new Date());
						int this_day_of_year = cd.get(Calendar.DAY_OF_YEAR);
						if ((this_day_of_year - post_day_of_year) > 14) {
							// 14天前才发过微博非活跃用户
							one.setAction_type(FollowAction.ACTION_TYPE_NOT_FOLLOWME);
							one.setAction_time(new Date());
							one.saveToDb();
							return 3;
						}
					}
					if (follow_comment) {
						if (list_status != null && list_status.size() > 0) {
							Status one_status = list_status.get(0);
							String retweet_txt = null;
							if (one_status.isRetweet()) {

								retweet_txt = RollFav.getRetweetText(
										one_status, 10);
							}
							if (retweet_txt != null) {

								getWeibo(fm).updateComment(retweet_txt,
										one_status.getId() + "", null);
							} else {
								// getWeibo(fm).updateComment(one.getOauth_name()
								// + " 我一直在关注你哟！ ",
								// one_status.getId() + "", null);
							}
						}
					}
				} catch (WeiboException ee) {
					ee.printStackTrace();
				}

				getWeibo(fm).createFriendship("" + one.getOauth_id());
				one.setAction_step(FollowAction.ACTION_STEP_FOLLOWING);
				one.setAction_time(new Date());
				one.saveToDb();
				return 1;
			} catch (WeiboException e) {
				if (e.getMessage().indexOf("40303:Error: already followed") != -1) {
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOWING);
					one.setAction_time(new Date());
					one.saveToDb();
					return 3;
				} else if (e.getMessage().indexOf(
						"40304:Error: Social graph updates out of rate limit!") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					return 2;
				} else if (e.getMessage().indexOf("40028:根据对方的设置，你不能进行此操作") != -1) {
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_BLOCKME);
					one.saveToDb();
					return 3;
				} else if (e.getMessage().indexOf("40028:fuid错误") != -1) {
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_DELETED);
					one.saveToDb();
					return 3;
				} else if (e.getMessage().indexOf(
						"40028:hi 超人，你今天已经关注很多喽，接下来的时间想想如何让大家都来关注你吧") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					return 2;
				}
				e.printStackTrace();
				// 待选ID很多取之不尽，不明错误直接放弃这个ID
				one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_UNKNOWN);
				one.saveToDb();
				return 4;
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			// 待选ID很多取之不尽，不明错误直接放弃这个ID
			one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_UNKNOWN);
			one.saveToDb();
			return 4;
		}
	}

	/**
	 * 处理上次加关注的
	 * 
	 * @param following
	 */
	public int DoFollowing(Fmember fm, int max) {
		String log_str = "";
		ActionLog actionLog = new ActionLog();
		actionLog.setSrc_id(fm.getSina_id());
		actionLog.setDest_id(0);
		actionLog.setType(ActionLog.TYPE_FOLLOWME_UNFOLLOW);
		actionLog.setTime(new Date());
		actionLog.setText("");
		Vector<FollowAction> followings = FollowAction.getFollowingBySec(""
				+ fm.getSina_id(), check_time_sec, max);
		for (int i = 0; i < followings.size(); i++) {
			FollowAction one = followings.get(i);
			try {
				if (getWeibo(fm).existsFriendship("" + one.getOauth_id(),
						"" + fm.getSina_id())) {
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOWED);
					one.setAction_num(one.getAction_num() + 1);
					one.setAction_time(new Date());
					one.saveToDb();
					actionLog.addText(one.getOauth_id() + ";");
					// one关注了fm,那么互粉了。
				} else {
					getWeibo(fm).destroyFriendship(one.getOauth_id());
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOW_FAIL);
					one.setAction_num(one.getAction_num() + 1);
					one.setAction_time(new Date());
					one.saveToDb();
					actionLog.addText(one.getOauth_id() + ";");
				}
			} catch (WeiboException e) {
				if (e.getMessage().indexOf(
						"40304:Error: Social graph updates out of rate limit!") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf(
						"40028:hi 超人，你今天已经关注很多喽，接下来的时间想想如何让大家都来关注你吧") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf("40028:fuid错误") != -1) {
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_DELETED);
					one.saveToDb();
				}
				e.printStackTrace();
			}
			log_str += one.getOauth_id() + ",";
			if (i % 10 == 0) {
				Log.log("DoFollowing : " + log_str);
				log_str = "";
			}
		}
		actionLog.setStatus(ActionLog.STATUS_SUCCESSED);
		actionLog.insertToDb();
		Log.log("DoFollowing : " + log_str);
		return followings.size();

	}

	/**
	 * 处理已经加关注
	 * 
	 * @param followed
	 */
	public int DoFollowed(Fmember fm, int max) {
		String log_str = "";
		ActionLog actionLog = new ActionLog();
		actionLog.setSrc_id(fm.getSina_id());
		actionLog.setDest_id(0);
		actionLog.setType(ActionLog.TYPE_FOLLOWME_DOFOLLOWED);
		actionLog.setTime(new Date());
		actionLog.setText("");
		Vector<FollowAction> followeds = FollowAction.getByTypeAndStep(
				"" + fm.getSina_id(), FollowAction.ACTION_TYPE_FOLLOWME,
				FollowAction.ACTION_STEP_FOLLOWED, max);
		for (int i = 0; i < followeds.size(); i++) {
			FollowAction one = followeds.get(i);
			try {
				if (getWeibo(fm).existsFriendship("" + one.getOauth_id(),
						"" + fm.getSina_id())) {
					// one关注了fm,那么互粉了。
					actionLog.addText(one.getOauth_id() + ";");
				} else {
					getWeibo(fm).destroyFriendship(one.getOauth_id());
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOW_FAIL);
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_CHEATER);
					one.setAction_time(new Date());
					one.saveToDb();
					actionLog.addText(one.getOauth_id() + ";");
				}
			} catch (WeiboException e) {
				if (e.getMessage().indexOf(
						"40304:Error: Social graph updates out of rate limit!") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf(
						"40028:hi 超人，你今天已经关注很多喽，接下来的时间想想如何让大家都来关注你吧") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf("40028:fuid错误") != -1) {
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_DELETED);
					one.saveToDb();
				}
				e.printStackTrace();
			}
			log_str += one.getOauth_id() + ",";
			if (i % 10 == 0) {
				System.out.println("DoFollowed : " + log_str);
				log_str = "";
			}
		}
		actionLog.setStatus(ActionLog.STATUS_SUCCESSED);
		actionLog.insertToDb();
		Log.log("DoFollowed : " + log_str);
		return followeds.size();
	}

	/**
	 * 去掉所有自动加粉的
	 * 
	 * @param followed
	 */
	public int DoCheate(Fmember fm, int max) {
		String log_str = "";
		ActionLog actionLog = new ActionLog();
		actionLog.setSrc_id(fm.getSina_id());
		actionLog.setDest_id(0);
		actionLog.setType(ActionLog.TYPE_FOLLOWME_DOFOLLOWED);
		actionLog.setTime(new Date());
		actionLog.setText("");
		Vector<FollowAction> followeds = FollowAction.getByTypeAndStep(
				"" + fm.getSina_id(), FollowAction.ACTION_TYPE_FOLLOWME,
				FollowAction.ACTION_STEP_FOLLOWED, max);
		for (int i = 0; i < followeds.size(); i++) {
			FollowAction one = followeds.get(i);
			try {
				Fcorpse fc = FcorpseCache.getIns().getNextOne();
				if (getWeibo(fc).existsFriendship("" + one.getOauth_id(),
						"" + fm.getSina_id())) {
					// one关注了fm,那么互粉了。
					actionLog.addText(one.getOauth_id() + ";");
					getWeibo(fm).destroyFriendship(one.getOauth_id());
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOW_CHEATED);
					one.setAction_time(new Date());
					one.saveToDb();
					actionLog.setType(ActionLog.TYPE_FOLLOWME_DOCHEATED);
					actionLog.addText(one.getOauth_id() + ";");
				} else {
					getWeibo(fm).destroyFriendship(one.getOauth_id());
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOW_FAIL);
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_CHEATER);
					one.setAction_time(new Date());
					one.saveToDb();
					actionLog.addText(one.getOauth_id() + ";");
				}
				Thread.sleep(200);
			} catch (WeiboException e) {
				if (e.getMessage().indexOf(
						"40304:Error: Social graph updates out of rate limit!") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf(
						"40028:hi 超人，你今天已经关注很多喽，接下来的时间想想如何让大家都来关注你吧") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf(
						"40310:Error: user requests out of rate limit!") != -1) {
					user_rate_limit.put("" + fm.getSina_id(),
							System.currentTimeMillis());
					break;
				} else if (e.getMessage().indexOf("40028:fuid错误") != -1) {
					one.setAction_type(FollowAction.ACTION_TYPE_BLOCK_DELETED);
					one.saveToDb();
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			log_str += one.getOauth_id() + ",";
			if (i % 10 == 0) {
				Log.log("DoCheat : " + log_str);
				log_str = "";
			}
		}
		actionLog.setStatus(ActionLog.STATUS_SUCCESSED);
		actionLog.insertToDb();
		Log.log("DoFollowed : " + log_str);
		return followeds.size();
	}

	/**
	 * 处理新ID，
	 * 
	 * @param reserve
	 */
	public int DoUnCheat(Fmember fm_one, int max) {
		ActionLog actionLog = new ActionLog();
		actionLog.setSrc_id(fm_one.getSina_id());
		actionLog.setDest_id(0);
		actionLog.setType(ActionLog.TYPE_FOLLOWME_DOFOLLOW);
		actionLog.setTime(new Date());
		actionLog.setText("");

		int successed_num = 0;
		int error_num = 0;

		for (int try_roll_num = 0; try_roll_num < 5; try_roll_num++) {
			Vector<FollowAction> reserves = FollowAction.getByTypeAndStep(""
					+ fm_one.getSina_id(), FollowAction.ACTION_TYPE_FOLLOWME,
					FollowAction.ACTION_STEP_FOLLOW_CHEATED, max);
			for (int i = 0; i < reserves.size(); i++) {
				if ((successed_num >= max) || (error_num > max * 2)) {
					break;
				}
				FollowAction one = reserves.get(i);
				int ret_status = WBcreateFriendship(one, fm_one);
				switch (ret_status) {
				case 1:
					successed_num++;
					actionLog.addText(one.getOauth_id() + "=1;");
					one.setAction_step(FollowAction.ACTION_STEP_FOLLOWED);
					break;
				case 2:
					// rate limit
					error_num += 10000;
					actionLog.addText(one.getOauth_id() + "=2;");
					break;
				case 3:
					actionLog.addText(one.getOauth_id() + "=3;");
					continue;
				case 4:
				default:
					actionLog.addText(one.getOauth_id() + "=4;");
					error_num++;
					break;
				}
			}
			if ((successed_num >= max) || (error_num > max * 2)) {
				break;
			}
		}
		actionLog.setStatus(ActionLog.STATUS_SUCCESSED);
		actionLog.insertToDb();
		System.out.println("DoReserve : " + actionLog.getText());
		return successed_num;
	}

	private static Weibo getWeibo(Fcorpse fc) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fc.getApp_key(), fc.getApp_secret());
		weibo.setToken(fc.getOauth_token(), fc.getOauth_token_secret());
		return weibo;
	}

	private static Weibo getWeibo(Fmember fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}
}
