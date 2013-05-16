package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Vector;

/**
 * 和UserClassimg相对应。大量用户的用户图可能相同
 * 
 * @author CT0178
 * 
 */
public class UserImgSame extends Fdb {
	public static String default_tablename = "user_img_same";
	long user_id;
	String user_name;
	String url;
	int class_id;
	Date update_time;

	public void inserIntoDb() {
		Vector<UserImgSame> all = new Vector<UserImgSame>();
		all.add(this);
		inserIntoDb(all, default_tablename);
	}

	public static void inserIntoDb(Vector<UserImgSame> all, String table_name) {
		if (all.size() == 0) {
			return;
		}
		String sql = "insert into " + table_name;
		sql += "  (`user_id`,`user_name`,`url`,`class_id`,`update_time`) VALUES ";
		for (int i = 0; i < all.size(); i++) {
			sql += "(" + item(all.get(i).getUser_id()) + " , "
					+ item(all.get(i).getUser_name()) + " , "
					+ item(all.get(i).getUrl()) + " , "
					+ item(all.get(i).getClass_id()) + " , "
					+ item(all.get(i).getUpdate_time()) + ")";
			if (i == all.size() - 1) {
				sql += ";";
			} else {
				sql += ",";
			}
		}
		getDb().executequery(sql);
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
