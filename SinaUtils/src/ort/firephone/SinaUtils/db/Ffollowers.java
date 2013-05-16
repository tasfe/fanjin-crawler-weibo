package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class Ffollowers extends Fdb {
	public static String table_name = "followers";

	public static boolean isExist(long source_id, long follower_id) {
		String sql = "select * from " + table_name + " where "
				+ item("source_id", source_id) + " and "
				+ item("follower_id", follower_id);
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<Long> getFlowers(long source_id, int max) {
		Vector<Long> follower_ids = null;
		String sql = "select follower_id from " + table_name
				+ " where source_id=" + source_id
				+ " order by update_time desc limit " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	public static Vector<Long> getAll(int max) {
		Vector<Long> follower_ids = null;
		String sql = "select follower_id from " + table_name
				+ "  order by update_time desc limit " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	public static Vector<Long> getAllLimited(int start, int max) {
		Vector<Long> follower_ids = null;
		String sql = "select follower_id from " + table_name + " limit "
				+ start + " , " + max;
		follower_ids = getDb().queryListLong(sql);
		return follower_ids;
	}

	/**
	 * 
	 * @param ids
	 *            输入的所有ID
	 * @return 输入的ID中数据库里有的那部份
	 */
	public static Hashtable<Long, Integer> getExistHash(long source_id,
			Vector<Long> ids) {
		Hashtable<Long, Integer> exist_ids = new Hashtable<Long, Integer>();
		if (ids.size() == 0) {
			return exist_ids;
		}
		String sql = "  select follower_id from " + table_name
				+ " where follower_id in ( " + ids.get(0) + " ";
		for (int i = 1; i < ids.size(); i++) {
			sql += " , " + ids.get(i) + "";
		}
		sql += " ) and " + item("source_id", source_id);
		Vector<Long> vl = getDb().queryListLong(sql);
		for (int i = 0; i < vl.size(); i++) {
			exist_ids.put(vl.get(i), 1);
		}
		return exist_ids;
	}

	/**
	 * 分析输入的ID，返回这些ID中在数据库不存在的部份
	 * 
	 * @param ids
	 * @return
	 */
	public static Vector<Long> getNotExist(long source_id, Vector<Long> ids) {
		Vector<Long> all_not = new Vector<Long>();
		Hashtable<Long, Integer> existH = getExistHash(source_id, ids);
		for (int i = 0; i < ids.size(); i++) {
			if (!existH.containsKey(ids.get(i))) {
				all_not.add(ids.get(i));
			}
		}
		return all_not;
	}

	public static int tryInsertToDb(long source_id, Vector<Long> all_ids) {
		if (all_ids.size() == 0) {
			return 0;
		}
		Vector<Long> ids = getNotExist(source_id, all_ids);
		insertToDb(source_id, ids);
		return ids.size();
	}

	public static int insertToDb(long source_id, Vector<Long> ids) {
		if (ids.size() == 0) {
			return 0 ;
		}
		long update_time = System.currentTimeMillis() / 1000L;
		String sql = "insert into " + table_name
				+ " (`source_id`,`follower_id`,`update_time`) VALUES ";
		for (int i = 0; i < ids.size(); i++) {
			sql += " ( " + item(source_id) + "," + item(ids.get(i)) + ","
					+ item(update_time) + " )";
			if (i == ids.size() - 1) {
				sql += " ; ";
			} else {
				sql += " , ";
			}
		}
		getDb().executequery(sql);
		return ids.size();
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
						+ " ,follower_id=" + ids.get(i);
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
				+ ",source_id=" + source_id + " ,follower_id=" + follower_id;
		getDb().executequery(sql);
	}
}
