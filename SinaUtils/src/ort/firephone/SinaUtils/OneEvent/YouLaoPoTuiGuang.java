package ort.firephone.SinaUtils.OneEvent;

import java.io.File;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fmember;
import ort.firephone.SinaUtils.db.FollowAction;
import ort.firephone.SinaUtils.db.PicDir;
import ort.firephone.SinaUtils.db.PostTiming;
import ort.firephone.SinaUtils.db.RunParam;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class YouLaoPoTuiGuang {
	roll_images_item_t roll_images = null;
	Vector<PostTiming> text_msgs = null;
	W_Url w_urls = null;
	int text_msg_pos = 0;

	public static void main(String argv[]) {
		Env.initial();
		String followAction_pos = "";
		if (argv.length >= 1) {
			followAction_pos = argv[0].trim();
			FollowAction.default_table_name = "follow_action"
					+ followAction_pos;
			Log.log(FollowAction.default_table_name);
		}
		YouLaoPoTuiGuang youLaoPoTuiGuang = new YouLaoPoTuiGuang();
		for (int i = 0; i < 3; i++) {
			new one_thread(youLaoPoTuiGuang).start();
		}
	}

	public void roll() {
		while (true) {
			try {
				postNextOne();
			} catch (Exception e) {
				e.printStackTrace();
				sleep(60000);
			}
			sleep(Env.pic_gap_sec);
		}
	}

	public YouLaoPoTuiGuang() {
		text_msgs = PostTiming.getAll();
		roll_images = new roll_images_item_t();
		w_urls = new W_Url();
	}

	public void postNextOne() {
		Fcorpse fc = FcorpseCache.getIns().getNextOne();
		String text = "";
		try {

			Weibo w = getWeibo(fc);
			File f = roll_images.getNextFile();
			String text_msg = getNextMsg() + " ";
			String w_url = w_urls.getNextUrl();
			one_ats_t at_str = getNextFollowAction(3);
			one_ats_t at_str2 = getNextFollowAction(3);
			if (at_str == null || at_str2 == null) {
				Log.log("game over !");
				System.exit(0);
			}

			text = at_str.toString() + text_msg + at_str2.toString()
					+ " 更多打折加油站详见：" + w_url;
			String url1 = java.net.URLEncoder.encode(text, "UTF-8");
			w.uploadStatus(url1, f);
			Log.log(fc.getSina_user() + " " + fc.getSina_pass() + "  " + text
					+ " oked ");
			at_str.setSuccessToDb();
			at_str2.setSuccessToDb();
			try {
				for (int i = 0; i < at_str.followActions.size(); i++) {
					// w.createFriendship(at_str.followActions.get(i)
					// .getOauth_id());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log.log(fc.getSina_user() + " " + fc.getSina_pass() + "  " + text
					+ " fail ");
			sleep(60000);
		} catch (Exception e) {
			e.printStackTrace();
			Log.log(fc.getSina_user() + " " + fc.getSina_pass() + "  " + text
					+ " fail ");
			sleep(10000);
		}
	}

	Vector<FollowAction> followActionsCache = new Vector<FollowAction>();
	int followActionsCachePos = 0;

	public synchronized one_ats_t getNextFollowAction(int max) {
		if ((followActionsCache.size() - followActionsCachePos) < max) {
			followActionsCache = FollowAction.getByAtStep("1", 0, max * 10);
			if (followActionsCache.size() == 0) {
				return null;
			}
			followActionsCachePos = 0;
			for (int i = 0; i < followActionsCache.size(); i++) {
				followActionsCache.get(i).setAt_step(2);
				followActionsCache.get(i).saveToDb();
			}
		}
		one_ats_t one_ats = new one_ats_t();
		one_ats.followActions = new Vector<FollowAction>();
		for (int i = 0; i < max; i++) {
			one_ats.followActions.add(followActionsCache
					.get(followActionsCachePos++));
		}
		for (int i = 0; i < one_ats.followActions.size(); i++) {
			// one_ats.followActions.get(i).setAt_step(2);
			// one_ats.followActions.get(i).saveToDb();
		}
		return one_ats;
	}

	public String getNextMsg() {
		if (text_msg_pos >= text_msgs.size()) {
			text_msgs = PostTiming.getAll();
			text_msg_pos = 0;
		}
		return " 油老婆-打折加油站提供：   " + text_msgs.get(text_msg_pos).getText();
	}

	public void iniMsg() {

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

	public static void sleep(long usec) {
		try {
			Thread.sleep(usec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class one_ats_t {
	public Vector<FollowAction> followActions = null;

	public String toString() {
		String text = "";
		for (int i = 0; i < followActions.size(); i++) {
			text += "//@" + followActions.get(i).getOauth_name() + ":";
		}
		return text;
	}

	public void setSuccessToDb() {
		try {
			for (int i = 0; i < followActions.size(); i++) {
				followActions.get(i).setAt_step(5);
				followActions.get(i).saveToDb();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFailToDb() {
		try {
			for (int i = 0; i < followActions.size(); i++) {
				followActions.get(i).setAt_step(4);
				followActions.get(i).saveToDb();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class one_thread extends Thread {
	YouLaoPoTuiGuang youLaoPoTuiGuang;

	public one_thread(YouLaoPoTuiGuang youLaoPoTuiGuang) {
		this.youLaoPoTuiGuang = youLaoPoTuiGuang;
	}

	public void run() {
		while (true) {
			youLaoPoTuiGuang.roll();
		}
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
		if (f.length() >= 300 * 1000) {
			// 文件大于5M
			pic_num++;
			return getNextFile();
		}
		if (dir_num >= 430) {
			dir_num = 1;
		}
		param_dir_num.setValue_int(dir_num);
		param_dir_num.updateToDb();
		param_pic_num.setValue_int(pic_num);
		param_pic_num.updateToDb();

		return f;
	}

	public String getDirName() {
		PicDir picDir = picDirs_h.get(dir_num);
		return picDir.getDesc();
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

class W_Url {
	Vector<String> urls = new Vector<String>();
	int pos = 0;

	public W_Url() {
		urls.add("http://www.youlaopo.com");
		urls.add("http://youlaopo.com");
		urls.add("http://youlaopo.com/index.php?m=area&alias=download");
		urls.add("http://youyeye.com");
		urls.add("http://www.youyeye.com");
		urls.add("http://youlaopo.com");
		urls.add("http://www.youlaolao.com");
		urls.add("http://youlaolao.com");
	}

	public String getNextUrl() {
		if (pos >= urls.size()) {
			pos = 0;
		}
		return urls.get(pos++);
	}
}