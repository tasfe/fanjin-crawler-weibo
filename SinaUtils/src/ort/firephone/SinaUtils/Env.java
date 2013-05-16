package ort.firephone.SinaUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.skyzoo.Jutil.DBConnections;

import weibo4j.Weibo;

public class Env {

	static public String db_driver = "com.mysql.jdbc.Driver";
	static public String db_server = "yanjian.firephone.org";
	static public String db_url = "jdbc:mysql://yanjian.firephone.org:3306/food?useUnicode=true&characterEncoding=gbk";
	static public String db_user = "yanjian";
	static public String db_pwd = "fp1234";
	static public Long pic_gap_sec = 600L;
	static public int roll_gap_sec = 10;
	static public int roll_one_comment_gap_sec = 86400;
	static public int roll_comment_gap_sec = 300;
	static public String pic_base_dir = "\\\\192.168.77.72\\inetpub2\\photo\\pic_done\\";
	static public String user_img_dir = "D:\\sina_img\\";
	static public String roll_comment_msg = "我只是路过的，路过记录如下：";
	public static boolean _isInitial = false;

	public static String CONSUMER_KEY = "2247832591";
	public static String CONSUMER_SECRET = "766ce77ea1493dfcf2be91d2bf8d8bd4";
	static public int oauth_get_try_num = 3;
	public static int db_connect_num = 4;
	public static int RollUserInfo_offset = 0;
	public static int RollUserIds_offset = 0;
	public static int RollUserImg_offset = 0;
	public static String web_root = "D:\\inetpub\\wwwroot\\food.yanjian.com\\";

	static public void initial() {
		if (_isInitial) {
			return;
		}
		if (!fromFile()) {
			System.out.println("util.ini not found");
		}
		echo();
		for (int i = 1; i <= db_connect_num; i++) {
			DBConnections.add("" + i, Env.db_driver, Env.db_url, Env.db_user,
					Env.db_pwd, null);
		}

		_isInitial = true;
	}

	public static int db_i = 1;

	public static DBConnections getDb() {
		db_i++;
		if (db_i > db_connect_num) {
			db_i = 1;
		}
		return DBConnections.getInstance("" + db_i);
	}

	public static void echo() {
		System.out.println("db_url=" + db_url);
		System.out.println("db_user=" + db_user);
		System.out.println("db_pwd=" + db_pwd);
		System.out.println("CONSUMER_KEY=" + CONSUMER_KEY);
		System.out.println("CONSUMER_SECRET=" + CONSUMER_SECRET);
		System.out.println("pic_gap_sec=" + pic_gap_sec);
		System.out.println("roll_gap_sec=" + roll_gap_sec);
		System.out.println("pic_base_dir=" + pic_base_dir);
		System.out.println("user_img_dir=" + user_img_dir);
		System.out.println("roll_comment_msg=" + roll_comment_msg);
		System.out.println("roll_one_comment_gap_sec="
				+ roll_one_comment_gap_sec);
		System.out.println("db_connect_num=" + db_connect_num);
		System.out.println("RollUserInfo_offset=" + RollUserInfo_offset);
		System.out.println("RollUserIds_offset=" + RollUserIds_offset);
		System.out.println("RollUserImg_offset=" + RollUserImg_offset);
		System.out.println("web_root=" + web_root);

	}

	public static boolean fromFile() {
		Properties prop = new Properties();
		File file = null;
		try {
			file = new File("conf/util.ini");
			if (!file.exists()) {
				file = new File("../conf/util.ini");
			}
		} catch (Exception e) {
			return false;
		}

		try {

			prop.load(new FileInputStream(file));

			db_url = String.valueOf(prop.get("db_url"));
			db_user = String.valueOf(prop.get("db_user"));
			db_pwd = String.valueOf(prop.get("db_pwd"));
			CONSUMER_KEY = String.valueOf(prop.get("CONSUMER_KEY"));
			CONSUMER_SECRET = String.valueOf(prop.get("CONSUMER_SECRET"));
			if (prop.containsKey("pic_gap_sec")) {
				pic_gap_sec = Long.parseLong(String.valueOf(prop
						.get("pic_gap_sec")));
			}
			if (prop.containsKey("roll_gap_sec")) {
				roll_gap_sec = Integer.parseInt(String.valueOf(prop
						.get("roll_gap_sec")));
			}
			if (prop.containsKey("pic_base_dir")) {
				pic_base_dir = String.valueOf(prop.get("pic_base_dir"));
			}
			if (prop.containsKey("user_img_dir")) {
				user_img_dir = String.valueOf(prop.get("user_img_dir"));
			}
			if (prop.containsKey("roll_comment_msg")) {
				roll_comment_msg = prop.getProperty("roll_comment_msg");
			}
			if (prop.containsKey("roll_one_comment_gap_sec")) {
				roll_one_comment_gap_sec = Integer.parseInt(String.valueOf(prop
						.get("roll_one_comment_gap_sec")));
			}
			if (prop.containsKey("db_connect_num")) {
				db_connect_num = Integer.parseInt(String.valueOf(prop
						.get("db_connect_num")));
			}
			if (prop.containsKey("RollUserInfo_offset")) {
				RollUserInfo_offset = Integer.parseInt(String.valueOf(prop
						.get("RollUserInfo_offset")));
			}
			if (prop.containsKey("RollUserIds_offset")) {
				RollUserIds_offset = Integer.parseInt(String.valueOf(prop
						.get("RollUserIds_offset")));
			}
			if (prop.containsKey("RollUserImg_offset")) {
				RollUserImg_offset = Integer.parseInt(String.valueOf(prop
						.get("RollUserImg_offset")));
			}
			if (prop.containsKey("web_root")) {
				web_root = prop.getProperty("web_root");
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
