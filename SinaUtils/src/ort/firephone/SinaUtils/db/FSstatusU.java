package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class FSstatusU extends Fdb {
	long user_id;
	int count;
	public static String table_name = "s_status_u";

	public static boolean isExist(long user_id) {
		String sql = " select user_id from " + table_name + " where user_id="
				+ user_id;
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<Long> getAllIds() {
		String sql = " select user_id from  " + table_name;
		return getDb().queryListLong(sql);
	}

	public static Hashtable<Long, Integer> getAllIdsHash() {
		Hashtable<Long, Integer> all = new Hashtable<Long, Integer>();
		Vector<Long> all_v = getAllIds();
		for (int i = 0; i < all_v.size(); i++) {
			all.put(all_v.get(i), 1);
		}

		return all;
	}

	public void insertIntoDB() {
		String sql = "insert into " + table_name + " set ";
		sql += item("user_id", user_id) + ", ";
		sql += item("count", count);
		getDb().executequery(sql);
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
