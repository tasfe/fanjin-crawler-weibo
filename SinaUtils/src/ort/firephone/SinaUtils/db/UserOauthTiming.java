package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class UserOauthTiming extends Fdb{
	public static String table_name = "oauth_timing";
	int id;
	String username;
	String text;
	String image;
	Date expect_time;
	Date post_time;
	int is_posted;

	public static String sql_select = " id,username,text,image,UNIX_TIMESTAMP(expect_time) as expect_time,UNIX_TIMESTAMP(post_time) as post_time,is_posted ";

	public static Vector<UserOauthTiming> getAll() {
		Vector<UserOauthTiming> all = new Vector<UserOauthTiming>();

		String sql = " select " + sql_select + " from " + table_name;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			UserOauthTiming one = new UserOauthTiming();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public static Vector<UserOauthTiming> getAllCurrentExpected() {
		Vector<UserOauthTiming> all = new Vector<UserOauthTiming>();

		String sql = " select " + sql_select + " from " + table_name
				+ " where " + item_less("expect_time", new Date())
				+ " and is_posted=0";
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			UserOauthTiming one = new UserOauthTiming();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public void saveStatusToDb() {
		String sql = "update " + table_name + " set ";
		sql += item("post_time", post_time) + " , ";
		sql += item("is_posted", is_posted) + "  ";
		sql += " where " + item("id", id);
		getDb().executequery(sql);
	}

	public void fromHash(Hashtable ht) {
		id = Integer.parseInt(ht.get("id").toString());
		username = ht.get("username").toString();
		text = ht.get("text").toString();
		image = ht.get("image").toString();
		expect_time = new Date(
				Long.parseLong(ht.get("expect_time").toString()) * 1000L);
		post_time = new Date(
				Long.parseLong(ht.get("post_time").toString()) * 1000L);
		is_posted = Integer.parseInt(ht.get("is_posted").toString());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getExpect_time() {
		return expect_time;
	}

	public void setExpect_time(Date expect_time) {
		this.expect_time = expect_time;
	}

	public Date getPost_time() {
		return post_time;
	}

	public void setPost_time(Date post_time) {
		this.post_time = post_time;
	}

	public int getIs_posted() {
		return is_posted;
	}

	public void setIs_posted(int is_posted) {
		this.is_posted = is_posted;
	}
}
