package ort.firephone.SinaUtils.inf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.ActionLog;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Ffavorite_status;
import ort.firephone.SinaUtils.db.Ffavorite_user;
import ort.firephone.SinaUtils.db.Fmember;
import ort.firephone.SinaUtils.db.PicDir;
import ort.firephone.SinaUtils.db.RunParam;
import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class RollFav {
	String comment_msg = "我只是路过的，路过记录如下：";

	RollFollowAction rollFollowAction = new RollFollowAction();

	/**
	 * 获取用户粉丝对象uid列表
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Env.initial();
			RollFav rollFav = new RollFav();
			rollFav.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		int types[] = { Ffavorite_user.TYPE_REPOST, Ffavorite_user.TYPE_NIGHT,
				Ffavorite_user.TYPE_COMMENT, Ffavorite_user.TYPE_ONE_COMMENT,
				Ffavorite_user.TYPE_COMMENT_ANYWAY };
		Vector<Ffavorite_user> fav_list = null;
		roll_images_item_t roll_pic = new roll_images_item_t();
		int repostme_maxnum = 10;
		while (true) {
			Log.log(com.skyzoo.Jutil.Sys.getTimeString(null) + "  roll   start");
			try {
				// 处理互粉
				rollFollowAction.try_roll_once();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// do action
				repostme_maxnum = RunParam.getByName(RunParam.REPOSTME_MAXNUM)
						.getValue_int();
				comment_msg = RunParam.getByName(RunParam.COMMENT_MSG)
						.getValue();
				fav_list = Ffavorite_user.getByType(types);
				for (int i = 0; i < fav_list.size(); i++) {
					Ffavorite_user one = fav_list.get(i);
					Log.log("fetch: " + one.getName());
					Vector<Status> new_status = checkNew(one);
					if (new_status.size() >= 1) {
						doAction(one, new_status);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// 用户贴必转

				Vector<Fmember> fms = Fmember.getAllActive();
				for (int i = 0; i < fms.size(); i++) {
					Fmember fm = fms.get(i);
					Log.log(" member  fetch: " + fm.getSina_user());
					java.util.Random random = new java.util.Random();
					int random_num = random.nextInt(repostme_maxnum);
					Vector<Status> new_status = checkNew(fm);
					doRespose(fm, new_status, random_num);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Vector<Ffavorite_status> vff = Ffavorite_status.getAllActive();
				for (int i = 0; i < vff.size(); i++) {
					Ffavorite_status f_s_one = vff.get(i);
					doStatusAction(f_s_one);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// 定期发图
				if (roll_pic.checkAndSet()) {
					doImage(roll_pic);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(Env.roll_gap_sec * 1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	Hashtable<Long, Hashtable<Long, Integer>> S_hash = new Hashtable<Long, Hashtable<Long, Integer>>();

	public Vector<Status> checkIsNew(Long sina_id, List<Status> status) {
		Vector<Status> new_status_id = new Vector<Status>();
		Hashtable<Long, Integer> status_list = null;

		if (!S_hash.containsKey(sina_id)) {
			status_list = new Hashtable<Long, Integer>();
			for (int i = 0; i < status.size(); i++) {
				status_list.put(status.get(i).getId(), 1);

			}
			S_hash.put(sina_id, status_list);
			Log.log("start initial ..." + sina_id);
			return new_status_id;
		}
		status_list = S_hash.get(sina_id);
		for (int i = 0; i < status.size(); i++) {

			Long status_id = status.get(i).getId();
			if (!status_list.containsKey(status_id)) {
				status_list.put(status_id, 1);
				new_status_id.add(status.get(i));
				Log.log("new ......" + status_id);
			}
		}
		return new_status_id;
	}

	public void doCorpse(WeiboException e, Fcorpse corpse) {
		if (e.getMessage().indexOf(
				"40312:Error: IP requests out of rate limit!") != -1) {
			try {
				System.out
						.println("IP requests out of rate limit! wait .... 5 min");
				Thread.sleep(300 * 1000L);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}

	}

	public Vector<Status> checkNew(Ffavorite_user f_user) {
		Vector<Status> list_status_new = new Vector<Status>();
		Fcorpse corpse = FcorpseCache.getIns().getNextOne();
		try {
			List<Status> list_status = getWeibo(corpse).getUserTimeline(
					"" + f_user.getSina_id());
			list_status_new = checkIsNew(f_user.getSina_id(), list_status);
			return list_status_new;

		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
		return list_status_new;
	}

	public Vector<Status> checkNew(Fmember f_user) {
		Vector<Status> list_status_new = new Vector<Status>();
		Fcorpse corpse = FcorpseCache.getIns().getNextOne();
		try {
			List<Status> list_status = getWeibo(corpse).getUserTimeline(
					"" + f_user.getSina_id());
			list_status_new = checkIsNew(f_user.getSina_id(), list_status);
			return list_status_new;

		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
		return list_status_new;
	}

	public void doImage(roll_images_item_t roll_pic) {
		Vector<Fmember> fms = Fmember.getAllActive();
		File f = roll_pic.getNextFile();
		String text = roll_pic.getText();
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm = fms.get(i);
			try {
				String url1 = java.net.URLEncoder.encode(text, "UTF-8");
				getWeibo(fm).uploadStatus(url1, f);
				log(fm, text + f.getAbsolutePath(),
						ActionLog.TYPE_UPLOAD_OF_MEM,
						ActionLog.STATUS_SUCCESSED);
				Log.log("pic new ---");
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				log(fm, text + f.getAbsolutePath(),
						ActionLog.TYPE_UPLOAD_OF_MEM, ActionLog.STATUS_FAIL);
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void log(Fmember fm, String text, int type, int status) {
		try {
			ActionLog actionLog = new ActionLog();
			actionLog.setSrc_id(fm.getSina_id());
			actionLog.setDest_id(0L);
			actionLog.setText(fm.getSina_user() + " "
					+ ActionLog.getTypeMsg(type) + " " + text);
			actionLog.setTime(new Date());
			actionLog.setType(type);
			actionLog.setStatus(status);
			actionLog.insertToDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(Fcorpse fcorpse, Status one, String text, int type,
			int status) {
		try {
			ActionLog actionLog = new ActionLog();
			actionLog.setSrc_id(fcorpse.getSina_id());
			actionLog.setDest_id(one.getUser().getId());
			actionLog.setText(fcorpse.getSina_user() + " "
					+ ActionLog.getTypeMsg(type) + " "
					+ one.getUser().getScreenName() + one.getText());
			actionLog.setTime(new Date());
			actionLog.setType(type);
			actionLog.setStatus(status);
			actionLog.insertToDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(Fmember fm, Status one, String text, int type,
			int status) {
		try {
			ActionLog actionLog = new ActionLog();
			actionLog.setSrc_id(fm.getSina_id());
			actionLog.setDest_id(one.getUser().getId());
			actionLog.setText(fm.getSina_user() + " "
					+ ActionLog.getTypeMsg(type) + " "
					+ one.getUser().getScreenName() + one.getText());
			actionLog.setTime(new Date());
			actionLog.setType(type);
			actionLog.setStatus(status);
			actionLog.insertToDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Hashtable<Long, Long> favorite_status_h = new Hashtable<Long, Long>();

	public void doStatusAction(Ffavorite_status f_status) {
		if (f_status.getType() == Ffavorite_status.TYPE_REPOSE_AND_AT) {
			Long current_sec = System.currentTimeMillis() / 1000L;
			if (favorite_status_h.containsKey(f_status.getStatus_id())) {
				long s_id = f_status.getStatus_id();
				long gap_sec = current_sec - favorite_status_h.get(s_id);
				if (gap_sec > 72000L) {
					favorite_status_h.put(s_id, current_sec);
					String at_str = FcorpseCache.getIns().getAtStr(4);
					Vector<Fmember> fms = Fmember.getAllActive();
					for (int i = 0; i < fms.size(); i++) {
						Fmember fm = fms.get(i);
						String time_str = com.skyzoo.Jutil.Sys
								.getTimeString(null);
						String repose_str = at_str + " " + time_str;
						try {

							getWeibo(fm).repost("" + s_id,
									at_str + " " + time_str, 3);
							log(fm, repose_str,
									ActionLog.TYPE_STATUS_REPOSE_AND_AT,
									ActionLog.STATUS_SUCCESSED);
							Log.log("REPOST status  new ---");
						} catch (WeiboException e) {
							e.printStackTrace();
							log(fm, repose_str,
									ActionLog.TYPE_STATUS_REPOSE_AND_AT,
									ActionLog.STATUS_FAIL);
						}
					}
				} else {
					return;
				}
			} else {
				favorite_status_h.put(f_status.getStatus_id(), current_sec);
			}
		}

	}

	public void doRespose(Fmember fm, Vector<Status> status, int num) {
		Status rep_status = null;
		Fcorpse corpse = null;

		for (int i = 0; i < status.size(); i++) {
			Status one_status = status.get(i);
			if (one_status.isRetweet()) {
				continue;
			}
			for (int j = 0; j < num; j++) {
				corpse = FcorpseCache.getIns().getNextOne();
				try {
					String rep_text = one_status.getText();
					rep_text = rep_text.substring(0,
							rep_text.length() > 100 ? 100 : rep_text.length());
					rep_status = getWeibo(corpse).repost(
							one_status.getId() + "",
							"转发: " + one_status.getId() + " ", 1);
					Log.log("REPOST member  new ---");
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log(corpse, one_status, "转发：",
							ActionLog.TYPE_REPOSE_TO_MEM, ActionLog.STATUS_FAIL);

				}
			}
			log(corpse, one_status, "转发了" + num + "次",
					ActionLog.TYPE_REPOSE_TO_MEM, ActionLog.STATUS_SUCCESSED);
			try {

				if (rep_status != null && false) {
					String t_time = com.skyzoo.Jutil.Sys.getTimeString("");
					getWeibo(fm).updateComment(
							rep_status.getUser().getName() + "你又在转了啊。。。"
									+ t_time, rep_status.getId() + "", null);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void doAction(Ffavorite_user f_user, Vector<Status> status) {
		Vector<Fmember> fms = Fmember.getAllActive();
		for (int i = 0; i < fms.size(); i++) {
			Fmember fm = fms.get(i);
			if (f_user.getType() == Ffavorite_user.TYPE_REPOST) {
				for (int j = 0; j < status.size(); j++) {
					Status one_status = status.get(j);

					try {
						getWeibo(fm).repost(one_status.getId() + "",
								"转发:" + one_status.getId(), 1);
						log(fm, one_status, "转发：",
								ActionLog.TYPE_REPOSE_TO_FAV,
								ActionLog.STATUS_SUCCESSED);
						Log.log("REPOST new ---");
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						log(fm, one_status, "转发：",
								ActionLog.TYPE_REPOSE_TO_FAV,
								ActionLog.STATUS_FAIL);
						e.printStackTrace();
					}
				}
			} else if (f_user.getType() == Ffavorite_user.TYPE_NIGHT) {
				for (int j = 0; j < status.size(); j++) {
					Status one_status = status.get(j);
					Date c_date = one_status.getCreatedAt();
					Calendar cd = Calendar.getInstance();
					cd.setTime(c_date);
					int hour_of_day = cd.get(Calendar.HOUR_OF_DAY);
					try {
						if (hour_of_day > 0 && hour_of_day < 5) {
							String t_time = com.skyzoo.Jutil.Sys
									.getTimeString("");
							String comment = "半夜查岗，还不睡觉," + f_user.getName()
									+ "在" + t_time;
							getWeibo(fm).updateComment(comment,
									one_status.getId() + "", null);
							log(fm, one_status, comment,
									ActionLog.TYPE_COMMENT_TO_FAV,
									ActionLog.STATUS_SUCCESSED);
						}
						Log.log("NIGHT new ---");
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						log(fm, one_status, "", ActionLog.TYPE_COMMENT_TO_FAV,
								ActionLog.STATUS_FAIL);
						e.printStackTrace();
					}
				}
			} else if (f_user.getType() == Ffavorite_user.TYPE_COMMENT_ANYWAY) {
				for (int j = 0; j < status.size(); j++) {
					Status one_status = status.get(j);
					Date c_date = one_status.getCreatedAt();
					Calendar cd = Calendar.getInstance();
					cd.setTime(c_date);
					int hour_of_day = cd.get(Calendar.HOUR_OF_DAY);
					try {
						String retweet_txt = getRetweetText(one_status, 4);
						if (retweet_txt != null) {
							getWeibo(fm).updateComment(retweet_txt,
									one_status.getId() + "", null);
							log(fm, one_status, retweet_txt,
									ActionLog.TYPE_COMMENT_TO_FAV,
									ActionLog.STATUS_SUCCESSED);
							Log.log("One COMMENT new ---");
						} else {
							String t_time = com.skyzoo.Jutil.Sys
									.getTimeString("");
							String screen_name = one_status.getUser()
									.getScreenName();
							String comment = comment_msg;
							comment = comment.replaceAll("#name", screen_name);
							comment = comment.replaceAll("#time", t_time);
							getWeibo(fm).updateComment(comment,
									one_status.getId() + "", null);
							log(fm, one_status, comment,
									ActionLog.TYPE_COMMENT_TO_FAV,
									ActionLog.STATUS_SUCCESSED);
							Log.log("COMMENT new ---");
						}
					} catch (WeiboException e) {
						log(fm, one_status, "", ActionLog.TYPE_COMMENT_TO_FAV,
								ActionLog.STATUS_FAIL);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (f_user.getType() == Ffavorite_user.TYPE_COMMENT) {
				for (int j = 0; j < status.size(); j++) {
					Status one_status = status.get(j);
					int current_unix_sec = (int) (System.currentTimeMillis() / 1000L);
					String s_sina_id = "" + f_user.getSina_id().toString();
					RunParam runParam = RunParam.getByName(s_sina_id);
					boolean check_do = false;
					if (runParam == null) {
						runParam = new RunParam();
						runParam.setName(s_sina_id);
						runParam.setType(5);
						runParam.setValue("");
						runParam.setValue_int(current_unix_sec);
						runParam.insertToDb();
					} else if (current_unix_sec - runParam.getValue_int() >= Env.roll_comment_gap_sec) {
						runParam.setValue_int(current_unix_sec);
						runParam.updateToDb();
						check_do = true;
					}
					if (check_do) {
						try {
							String retweet_txt = getRetweetText(one_status, 12);
							if (retweet_txt != null) {
								getWeibo(fm).updateComment(retweet_txt,
										one_status.getId() + "", null);
								log(fm, one_status, retweet_txt,
										ActionLog.TYPE_COMMENT_TO_FAV,
										ActionLog.STATUS_SUCCESSED);
								Log.log("One COMMENT new ---");
							} else {
								// 又不能人工智能，只能算了。
							}
						} catch (WeiboException e) {
							log(fm, one_status, "",
									ActionLog.TYPE_COMMENT_TO_FAV,
									ActionLog.STATUS_FAIL);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else if (f_user.getType() == Ffavorite_user.TYPE_ONE_COMMENT) {
				// 这里是比较小心的评论，只评论转贴，提取原贴的评论来发。

				int current_unix_sec = (int) (System.currentTimeMillis() / 1000L);
				String s_sina_id = "" + f_user.getSina_id().toString();
				RunParam runParam = RunParam.getByName(s_sina_id);
				boolean check_do = false;
				if (runParam == null) {
					runParam = new RunParam();
					runParam.setName(s_sina_id);
					runParam.setType(5);
					runParam.setValue("");
					runParam.setValue_int(current_unix_sec);
					runParam.insertToDb();
				} else if (current_unix_sec - runParam.getValue_int() >= Env.roll_one_comment_gap_sec) {
					runParam.setValue_int(current_unix_sec);
					runParam.updateToDb();
					check_do = true;
				}
				if (check_do) {
					Status one_status = status.get(0);
					try {
						String retweet_txt = getRetweetText(one_status, 24);
						if (retweet_txt != null) {
							getWeibo(fm).updateComment(retweet_txt,
									one_status.getId() + "", null);
							log(fm, one_status, retweet_txt,
									ActionLog.TYPE_COMMENT_TO_FAV,
									ActionLog.STATUS_SUCCESSED);
							Log.log("One COMMENT new ---");
						} else {
							// 原贴啊，不知如何评论只有放弃
						}
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						log(fm, one_status, "", ActionLog.TYPE_COMMENT_TO_FAV,
								ActionLog.STATUS_FAIL);
						e.printStackTrace();
					}
				}

			}
		}
	}

	/**
	 * 如果status是转贴的话，那么从status的原贴中取出不是status作者写的一条评论
	 * 
	 * @param status
	 * @param min_num_of
	 *            怕被发现，如果总评论数少于这个量，那么认为失败
	 * @return
	 */
	public static String getRetweetText(Status status, int min_num_of) {
		if (!status.isRetweet()) {
			return null;
		}
		String ret = null;
		long status_user_id = status.getUser().getId();
		long re_id = status.getRetweeted_status().getId();
		Fcorpse corpse = FcorpseCache.getIns().getNextOne();

		try {
			List<Comment> comments = getWeibo(corpse).getComments("" + re_id);
			if (comments.size() < min_num_of) {
				return null;
			}
			for (int i = 0; i < comments.size(); i++) {
				if (comments.get(i).getUser().getId() != status_user_id) {
					String t_s = com.skyzoo.Jutil.Sys.getTimeString(null);
					ret = comments.get(i).getText();
					ret = ret.replaceAll("转发此微博:", "");
					ret = ret.replaceAll("转发微博", "");
					ret = formatRetweetText(ret);
					ret += "  " + t_s;
					return ret;
				}
			}
			return null;
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatRetweetText(String x) {
		String ret = x;
		int pos_1 = 0;
		int pos_2 = 0;
		pos_1 = ret.indexOf("回复 @");
		while (pos_1 != -1) {
			pos_2 = ret.indexOf(":", pos_1);
			if (pos_2 == -1) {
				pos_2 = ret.indexOf("：", pos_1);
			}
			if (pos_2 == -1) {
				pos_2 = ret.indexOf(" ", pos_1);
			}
			if (pos_2 == -1) {
				pos_2 = ret.indexOf("\t", pos_1);
			}

			if (pos_2 != -1) {
				ret = ret.substring(0, pos_1) + ret.substring(pos_2 + 1);
			} else {
				ret = ret.substring(0, pos_1) + ret.substring(pos_1 + 3 + 1);
			}
			pos_1 = ret.indexOf("回复 @");
		}
		pos_1 = ret.indexOf("//@");
		while (pos_1 != -1) {
			pos_2 = ret.indexOf(":", pos_1);
			if (pos_2 == -1) {
				pos_2 = ret.indexOf("：", pos_1);
			}
			if (pos_2 == -1) {
				pos_2 = ret.indexOf(" ", pos_1);
			}
			if (pos_2 == -1) {
				pos_2 = ret.indexOf("\t", pos_1);
			}
			if (pos_2 != -1) {
				ret = ret.substring(0, pos_1) + ret.substring(pos_2 + 1);
			} else {
				ret = ret.substring(0, pos_1) + ret.substring(pos_1 + 3 + 1);
			}
			pos_1 = ret.indexOf("//@");
		}
		pos_1 = ret.indexOf("@");
		while (pos_1 != -1) {
			pos_2 = ret.indexOf(":", pos_1);
			if (pos_2 == -1) {
				pos_2 = ret.indexOf(" ", pos_1);
			}
			if (pos_2 == -1) {
				pos_2 = ret.indexOf("\t", pos_1);
			}
			if (pos_2 == -1) {
				if (ret.length() > pos_1 + 3 + 2) {
					pos_2 = pos_1 + 3;
				} else {
					pos_2 = ret.length() - 2;
				}
			}
			ret = ret.substring(0, pos_1) + ret.substring(pos_2 + 1);
			pos_1 = ret.indexOf("@");
		}
		return ret;
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

class roll_images_item_t {
	public int process_num = 0;
	public int dir_num = 15;
	public int pic_num = 6;
	public long last_sec = 0L;

	public long GAP_SEC = Env.pic_gap_sec;
	RunParam param_dir_num;
	RunParam param_pic_num;
	/**
	 * 24小时分隔，multiple_24h[0] 表示 0：00至0：59之间的处理。 值： 0，表示这个小时内不处理直接跳过。
	 * 1，表示按每间隔1*GAP_SEC 时间后处理。 2，表示按每间隔2*GAP_SEC 时间后处理。
	 */
	public int multiple_24h[] = { 2, 2, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1 };

	Hashtable<Integer, PicDir> picDirs_h = new Hashtable<Integer, PicDir>();

	public roll_images_item_t() {
		Vector<PicDir> picDirs = PicDir.getAllDirs();
		for (int i = 0; i < picDirs.size(); i++) {
			picDirs_h.put(picDirs.get(i).getId(), picDirs.get(i));
		}

		param_dir_num = RunParam.getByName(RunParam.PIC_DIR_NUM);
		param_pic_num = RunParam.getByName(RunParam.PIC_PIC_NUM);
		dir_num = param_dir_num.getValue_int();
		pic_num = param_pic_num.getValue_int();

	}

	public boolean checkAndSet() {
		long current_sec = System.currentTimeMillis() / 1000L;
		Calendar time = Calendar.getInstance();
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int multipleOfCurrentHour = multiple_24h[hour];
		if (multipleOfCurrentHour == 0) {
			return false;
		}
		if (current_sec - last_sec > multipleOfCurrentHour * GAP_SEC) {
			last_sec = current_sec;
			process_num++;
			return true;
		}
		return false;
	}

	public File getNextFile() {
		File f;
		String filename = Env.pic_base_dir;
		String dir = id2string(dir_num, 3);
		String name = id2string(pic_num, 2) + ".jpg";
		filename += dir + "\\" + name;
		pic_num++;
		f = new File(filename);
		if (!f.exists()) {
			dir_num++;
			pic_num = 0;
			return getNextFile();
		}
		if (f.length() >= 5 * 1000 * 1000) {
			// 文件大于5M
			pic_num++;
			return getNextFile();
		}
		param_dir_num.setValue_int(dir_num);
		param_dir_num.updateToDb();
		param_pic_num.setValue_int(pic_num);
		param_pic_num.updateToDb();
		return f;
	}

	public String getUrl() {
		return "http://p.yanjian.com/" + id2string(dir_num, 3) + "/"
				+ id2string(pic_num - 1, 2) + ".jpg";
	}

	public String getDirName() {
		PicDir picDir = picDirs_h.get(dir_num);
		return picDir.getDesc();
	}

	public String getText() {
		String text = "美图发送：  第" + dir_num + "批 《" + getDirName() + "》  第"
				+ (pic_num - 1) + "张。    大家养养眼哟   继续关注，继续美图。      下载此图请点这里： "
				+ getUrl();
		return text;
	}

	final private static char IDSTR[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	public static String id2string(int id, int num) {
		String ret = "";
		int bt[] = new int[num];
		for (int i = 0; i < num; i++) {
			bt[i] = id % IDSTR.length;
			id = id / IDSTR.length;
		}
		for (int i = num - 1; i >= 0; i--) {
			ret += IDSTR[bt[i]];
		}
		return ret;
	}
}
