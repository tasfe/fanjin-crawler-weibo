package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class Ffriends extends Fdb {
	public static String table_name = "friends";

	public static boolean isExist(long source_id, long follower_id) {
		String sql = "select * from " + table_name + " where "
				+ item("source_id", source_id) + " and "
				+ item("friend_id", follower_id);
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<Long> getFlowers(long source_id, int max) {
		Vector<Long> follower_ids = null;
		String sql = "select friend_id from " + table_name
				+ " where source_id=" + source_id
				+ " order by update_time desc limit " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	public static Vector<Long> getAll(int max) {
		Vector<Long> follower_ids = null;
		String sql = "select friend_id from " + table_name
				+ "  order by update_time desc limit " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	public static Vector<Long> getAllLimited(int start, int max) {
		Vector<Long> follower_ids = null;
		String sql = "select friend_id from " + table_name + " limit " + start
				+ " , " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	public static int tryAddToDb(long source_id, Vector<Long> ids) {
		String sql = "";
		int added_num = 0;
		long update_time = System.currentTimeMillis() / 1000L;
		for (int i = 0; i < ids.size(); i++) {
			if (isExist(source_id, ids.get(i))) {
				continue;
			}
			try {
				sql = "insert into " + table_name + " set update_time="
						+ update_time + ",source_id=" + source_id
						+ " ,friend_id=" + ids.get(i);
				getDb().executequery(sql);
				added_num++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return added_num;
	}

	public static int addToDb(long source_id, Vector<Long> ids) {
		String sql = "";
		int added_num = 0;
		long update_time = System.currentTimeMillis() / 1000L;
		for (int i = 0; i < ids.size(); i++) {
			try {
				sql = "insert into " + table_name + " set update_time="
						+ update_time + ",source_id=" + source_id
						+ " ,friend_id=" + ids.get(i);
				getDb().executequery(sql);
				added_num++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return added_num;
	}

	public static void add(long source_id, long follower_id) {
		String sql = "";
		long update_time = System.currentTimeMillis() / 1000L;
		sql = "insert into " + table_name + " set update_time=" + update_time
				+ ",source_id=" + source_id + " ,friend_id=" + follower_id;
		getDb().executequery(sql);
	}
}
