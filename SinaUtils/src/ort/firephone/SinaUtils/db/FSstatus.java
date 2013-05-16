package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;

import com.skyzoo.Jutil.DBConnections;

import weibo4j.Status;

public class FSstatus extends Fdb {
	public static String table_name = "s_status";

	private Date created_at;
	private long id;
	private String text;
	private String source;
	private boolean favorited;
	private boolean truncated;
	private long in_reply_to_status_id;
	private long in_reply_to_user_id;
	private String in_reply_to_screen_name;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	private long mid;
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

	/**
	 * 
	 * @param ids
	 *            输入的所有ID
	 * @return 输入的ID中数据库里有的那部份
	 */
	public static Hashtable<Long, Integer> getExistHash(Vector<Long> ids) {
		Hashtable<Long, Integer> exist_ids = new Hashtable<Long, Integer>();
		if (ids.size() == 0) {
			return exist_ids;
		}
		String sql = "  select id from " + table_name + " where id in ( "
				+ ids.get(0);
		for (int i = 1; i < ids.size(); i++) {
			sql += " , " + ids.get(i);
		}
		sql += " )";
		Vector<Long> vl = getDb().queryListLong(sql);
		for (int i = 0; i < vl.size(); i++) {
			exist_ids.put(vl.get(i), 1);
		}
		return exist_ids;
	}

	public static Vector<String> searchText(String key, int max, int start_limit) {
		Vector<String> all = new Vector<String>();
		String sql = "select text from " + table_name + " where text like '%"
				+ key + "%' limit " + start_limit + " , " + max;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			all.add(vh.get(i).get("text").toString());
		}
		return all;

	}

	public static void insertIntoDb(Vector<FSstatus> statuses) {
		if (statuses.size() == 0) {
			return;
		}
		String sql = "INSERT INTO `" + table_name
				+ "` (`id`, `created_at`, `text`, `source`,";
		sql += "`favorited`, `truncated`,`in_reply_to_status_id`, `in_reply_to_user_id`,";
		sql += " `in_reply_to_screen_name`, `thumbnail_pic`,`bmiddle_pic`, `original_pic`,";
		sql += " `mid`, `user_id`, `user_screen_name`, `retweeted_id`, `fp_active`) VALUES ";

		for (int i = 0; i < statuses.size(); i++) {
			FSstatus s = statuses.get(i);
			sql += "( " + item(s.getId()) + "," + item(s.getCreated_at()) + ","
					+ item(s.getText()) + "," + item(s.getSource());
			sql += "," + item(s.isFavorited()) + "," + item(s.isTruncated())
					+ "," + item(s.getIn_reply_to_status_id()) + ","
					+ item(s.getIn_reply_to_user_id());
			sql += "," + item(s.getIn_reply_to_screen_name()) + ","
					+ item(s.getThumbnail_pic()) + ","
					+ item(s.getBmiddle_pic()) + ","
					+ item(s.getOriginal_pic());
			sql += "," + item(s.getMid()) + "," + item(s.getUser_id()) + ","
					+ item(s.getUser_screen_name()) + ","
					+ item(s.getRetweeted_id()) + "," + item(1);
			sql += " ) ";
			if (i == statuses.size() - 1) {
				sql += ";";
			} else {
				sql += ",";
			}
		}
		getDb().executequery(sql);
	}

	public void insertIntoDb() {
		String sql = "insert into " + table_name + " set ";
		sql += item("id", id) + ",";
		sql += item("created_at", created_at) + ",";
		sql += item("text", text) + ",";
		sql += item("source", source) + ",";
		sql += item("favorited", favorited) + ",";
		sql += item("truncated", truncated) + ",";
		sql += item("in_reply_to_status_id", in_reply_to_status_id) + ",";
		sql += item("in_reply_to_user_id", in_reply_to_user_id) + ",";
		sql += item("in_reply_to_screen_name", in_reply_to_screen_name) + ",";
		sql += item("thumbnail_pic", thumbnail_pic) + ",";
		sql += item("bmiddle_pic", bmiddle_pic) + ",";
		sql += item("original_pic", original_pic) + ",";
		sql += item("mid", mid) + ",";
		sql += item("user_id", user_id) + ",";
		sql += item("user_screen_name", user_screen_name) + ",";
		sql += item("retweeted_id", retweeted_id);
		getDb().executequery(sql);
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public long getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(long in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public long getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(long in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUser_screen_name() {
		return user_screen_name;
	}

	public void setUser_screen_name(String user_screen_name) {
		this.user_screen_name = user_screen_name;
	}

	public long getRetweeted_id() {
		return retweeted_id;
	}

	public void setRetweeted_id(long retweeted_id) {
		this.retweeted_id = retweeted_id;
	}

}
