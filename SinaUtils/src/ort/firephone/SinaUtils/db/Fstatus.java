package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

import weibo4j.Status;
import weibo4j.User;

public class Fstatus extends Fdb {
	public static String table_name = "status";

	private Date created_at;
	private long id;
	private String text;
	private String source;
	private boolean favorited;
	private boolean truncated;
	private long in_reply_to_status_id;
	private int in_reply_to_user_id;
	private String in_reply_to_screen_name;
	private double latitude = -1;
	private double longitude = -1;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;

	private long user_id;
	private String user_screen_name;

	private long retweeted_id = 0;

	public static boolean isExist(long id) {
		String sql = " select id from " + table_name + " where id=" + id;
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public boolean exactEqual(Fstatus other) {
		if (id == other.id
				&& text.equals(other.text)
				&& source.equals(other.source)
				&& favorited == other.favorited
				&& truncated == other.truncated
				&& in_reply_to_status_id == other.in_reply_to_status_id
				&& in_reply_to_user_id == other.in_reply_to_user_id
				&& in_reply_to_screen_name
						.equals(other.in_reply_to_screen_name)
				&& latitude == other.latitude && longitude == other.longitude
				&& thumbnail_pic.equals(other.thumbnail_pic)
				&& bmiddle_pic.equals(other.bmiddle_pic)
				&& original_pic.equals(other.original_pic)
				&& user_id == other.user_id
				&& user_screen_name.equals(other.user_screen_name)
				&& retweeted_id == other.retweeted_id) {
			return true;
		}
		return false;

	}

	public static void insertIntoDb(int user_id, String user_screen_name,
			Status status) {
		String sql = "insert into " + table_name + " set ";
		sql += item("id", status.getId());
		sql += item("created_at", status.getCreatedAt()) + ",";
		sql += item("text", status.getText());
		sql += item("source", status.getSource());
		sql += item("favorited", status.isFavorited());
		sql += item("truncated", status.isTruncated());
		sql += item("in_reply_to_status_id", status.getInReplyToStatusId());
		sql += item("in_reply_to_user_id", status.getInReplyToUserId());
		sql += item("in_reply_to_screen_name", status.getInReplyToScreenName());
		sql += item("latitude", status.getLatitude());
		sql += item("longitude", status.getLongitude());
		sql += item("thumbnail_pic", status.getThumbnail_pic());
		sql += item("bmiddle_pic", status.getBmiddle_pic());
		sql += item("original_pic", status.getOriginal_pic());
		sql += item("user_id", user_id);
		sql += item("user_screen_name", user_screen_name);
		if (status.isRetweet()) {
			sql += item("retweeted_id", status.getRetweeted_status().getId());
		} else {
			sql += item("retweeted_id", -1);
		}
	}

}

// created_at: 创建时间
// id: 微博ID
// text: 微博信息内容
// source: 微博来源
// favorited: 是否已收藏
// truncated: 是否被截断
// in_reply_to_status_id: 回复ID
// in_reply_to_user_id: 回复人UID
// in_reply_to_screen_name: 回复人昵称
// thumbnail_pic: 缩略图
// bmiddle_pic: 中型图片
// original_pic：原始图片
// user: 作者信息
// retweeted_status: 转发的博文，内容为status，如果不是转发，则没有此字段

