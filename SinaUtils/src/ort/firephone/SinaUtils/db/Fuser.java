package ort.firephone.SinaUtils.db;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;

import com.skyzoo.Jutil.DBConnections;

import weibo4j.User;
import weibo4j.WeiboException;
import weibo4j.org.json.JSONObject;

public class Fuser extends Fdb {
	public static String table_name = "user";
	public User data;

	public static boolean isExist(long id) {
		String sql = " select id from user where id=" + id;
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static boolean isExist(Vector<Long> ids) {
		if (ids.size() == 0) {
			return false;
		}
		String sql = "  select id from user where id in ( " + ids.get(0);
		for (int i = 1; i < ids.size(); i++) {
			sql += " , " + ids.get(i);
		}
		sql += " )";
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<Hashtable> getByFilter(String f1, String f2) {
		String sql = "";
		if (f2 == null) {
			sql = " select * from user where `status-text` like '" + f1 + "' ";
		} else {
			sql = " select * from user where `status-text` like '" + f1
					+ "' or  `status-text` like '" + f2 + "'";
		}

		Vector<Hashtable> vh = getDb().query(sql);
		return vh;
	}

	public static Vector<Long> getAllIds() {
		String sql = " select id from user ";
		return DBConnections.getInstance("1").queryListLong(sql);
	}

	public static Vector<Long> getAllIdsNotInFSstatusU(int max) {
		String sql = " SELECT id FROM user WHERE user.id NOT IN (SELECT s_status_u.user_id FROM s_status_u ) and user.statuses_count >6 limit "
				+ max;
		return getDb().queryListLong(sql);
	}

	public static void insertIntoDb(User user) {
		String sql = " insert into user ";
		sql += " set id=" + user.getId() + ", ";
		sql += "  screen_name='" + S(user.getScreenName()) + "', ";
		sql += "  name='" + S(user.getName()) + "', ";
		sql += "  province=" + 0 + ", ";
		sql += "  city=" + 0 + ", ";
		sql += "  location='" + S(user.getLocation()) + "', ";
		sql += "  description='" + S(user.getDescription()) + "', ";
		sql += "  url='" + U(user.getURL()) + "', ";
		sql += "  profile_image_url='" + U(user.getProfileImageURL()) + "', ";
		sql += "  domain='" + "" + "', ";
		sql += "  gender='" + "" + "', ";
		sql += "  followers_count=" + user.getFollowersCount() + ", ";
		sql += "  friends_count=" + user.getFriendsCount() + ", ";
		sql += "  statuses_count=" + user.getStatusesCount() + ", ";
		sql += "  favourites_count=" + user.getFavouritesCount() + ", ";
		sql += "  created_at='" + D(user.getCreatedAt()) + "', ";
		sql += "  following='" + "" + "', ";
		sql += "  allow_all_act_msg='" + "" + "', ";
		sql += "  geo_enabled=" + "0" + ", ";
		sql += "  verified=" + B(user.isVerified()) + ", ";
		sql += "  `status-created_at`='" + D(user.getStatusCreatedAt()) + "', ";
		sql += "  `status-id`=" + user.getStatusId() + ", ";
		sql += "  `status-text`='" + S(user.getStatusText()) + "', ";
		sql += "  `status-source`='" + user.getStatusSource() + "', ";
		sql += "  `status-truncated`=" + B(user.isStatusTruncated()) + ", ";
		sql += "  `status-in_reply_to_status_id`="
				+ user.getStatusInReplyToStatusId() + ", ";
		sql += "  `status-in_reply_to_user_id`="
				+ user.getStatusInReplyToUserId() + ", ";
		sql += "  `status-favorited`=" + B(user.isStatusFavorited()) + ", ";

		if (user.getStatus() != null) {
			if (user.getStatus().isRetweet()) {
				sql += "  `retweeted_status-id`="
						+ user.getStatus().getRetweeted_status().getId() + ", ";
				sql += "  `retweeted_status-created_at`='"
						+ D(user.getStatus().getRetweeted_status()
								.getCreatedAt()) + "', ";
				sql += "  `retweeted_status-text`='"
						+ S(user.getStatus().getRetweeted_status().getText())
						+ "', ";
				sql += "  `retweeted_status-source`='"
						+ user.getStatus().getRetweeted_status().getSource()
						+ "', ";
				sql += "  `retweeted_status-truncated`="
						+ B(user.getStatus().getRetweeted_status()
								.isTruncated()) + ", ";
				sql += "  `retweeted_status-user-id`="
						+ user.getStatus().getRetweeted_status().getUser()
								.getId() + ", ";
				sql += "  `retweeted_status-user-screen_name`='"
						+ user.getStatus().getRetweeted_status().getUser()
								.getScreenName() + "', ";
				sql += "  `retweeted_status-user-name`='"
						+ user.getStatus().getRetweeted_status().getUser()
								.getName() + "', ";
				sql += "  `retweeted_status-user-description`='"
						+ user.getStatus().getRetweeted_status().getUser()
								.getDescription() + "', ";
			}
		}

		sql += "  `status-in_reply_to_screen_name`='"
				+ S(user.getStatusInReplyToScreenName()) + "' ";

		// retweeted_status.id
		// retweeted_status.created_at
		// retweeted_status.text
		// retweeted_status.source
		// status.retweeted_status.favorited
		// status.retweeted_status.truncated
		// status.retweeted_status.in_reply_to_status_id
		// status.retweeted_status.in_reply_to_user_id
		// status.retweeted_status.in_reply_to_screen_name
		// status.retweeted_status.thumbnail_pic
		// http://ww4.sinaimg.cn/thumbnail/70105f2dtw1dj7bw5e00zj.jpg
		// status.retweeted_status.bmiddle_pic
		// http://ww4.sinaimg.cn/bmiddle/70105f2dtw1dj7bw5e00zj.jpg
		// status.retweeted_status.original_pic
		// http://ww4.sinaimg.cn/large/70105f2dtw1dj7bw5e00zj.jpg
		// status.retweeted_status.geo
		// status.retweeted_status.mid 3335725638599295
		// status.retweeted_status.user.id 1880121133
		// status.retweeted_status.user.screen_name 国际记者网IJNet
		// status.retweeted_status.user.name 国际记者网IJNet
		// status.retweeted_status.user.province 400
		// status.retweeted_status.user.city 1
		// status.retweeted_status.user.location 海外 美国
		// status.retweeted_status.user.description
		// 国际记者网是一个面向全球记者、媒体管理者、新闻教育者和其他媒体相关人士的在线平台。此帐号由中文版主编 @马金馨 维护。
		// status.retweeted_status.user.url http://www.ijnet.org/zh-hans
		// status.retweeted_status.user.profile_image_url
		// http://tp2.sinaimg.cn/1880121133/50/1291306353/1
		// status.retweeted_status.user.domain ijnet
		// status.retweeted_status.user.gender m
		// status.retweeted_status.user.followers_count 2473
		// status.retweeted_status.user.friends_count 1010
		// status.retweeted_status.user.statuses_count 196
		// status.retweeted_status.user.favourites_count 1
		// status.retweeted_status.user.created_at Thu Dec 02 00:00:00 +0800
		// 2010
		// status.retweeted_status.user.following
		// status.retweeted_status.user.allow_all_act_msg
		// status.retweeted_status.user.geo_enabled true
		// status.retweeted_status.user.verified true
		// status.retweeted_status.annotations.0.cartoon
		// status.retweeted_status.annotations.0.server_ip 10.73.19.72
		//
		getDb().executequery(sql);
	}

	public static String S(String s) {
		if (s == null) {
			return "";
		}
		return s.replaceAll("'", "\\\\\'");
	}

	public static String D(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		if (d == null) {
			return "1970-00-00 00:00:00";
		}
		return sdf.format(d);

	}

	public static String U(URL u) {
		if (u == null) {
			return "";
		}
		return S(u.toString());

	}

	public static String B(boolean b) {
		if (b) {
			return "1";
		}
		return "0";
	}

}
