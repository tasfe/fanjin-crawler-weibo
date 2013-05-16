package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

public class UserIdAction extends Fdb {
	final public static String default_table_name = "user_id_action";
	final public static int TYPE_NORMAL = 1;
	final public static int TYPE_CORPSE = 11;
	final public static int TYPE_ERROR = 12;
	final public static int STEP_RESERVE = 100000000;
	final public static int STEP_INFO = 100000001;
	private String id;
	private int type;
	private int action_step;

	public static Vector<UserIdAction> getReserve(int max) {
		Vector<UserIdAction> all = new Vector<UserIdAction>();
		String sql = "select * from " + default_table_name + " where ";
		sql += item("action_step", STEP_RESERVE) + " and "
				+ item("type", TYPE_NORMAL) + " limit " + max;
		Vector<Hashtable> hts = getDb().query(sql);
		for (int i = 0; i < hts.size(); i++) {
			UserIdAction one = new UserIdAction();
			one.fromHash(hts.get(i));
			all.add(one);
		}
		return all;
	}

	public static Vector<UserIdAction> getReserveOffset(int max, int offset) {
		if (offset == 0) {
			return getReserve(max);
		}
		Vector<UserIdAction> all = new Vector<UserIdAction>();
		String sql = "select * from " + default_table_name + " where ";
		sql += item("action_step", STEP_RESERVE) + " and "
				+ item("type", TYPE_NORMAL) + " limit " + offset + "," + max;
		Vector<Hashtable> hts = getDb().query(sql);
		for (int i = 0; i < hts.size(); i++) {
			UserIdAction one = new UserIdAction();
			one.fromHash(hts.get(i));
			all.add(one);
		}
		return all;
	}

	/**
	 * 
	 * @param ids
	 *            输入的所有ID
	 * @return 输入的ID中数据库里有的那部份
	 */
	public static Hashtable<String, Integer> getExistHash(Vector<String> ids) {
		Hashtable<String, Integer> exist_ids = new Hashtable<String, Integer>();
		if (ids.size() == 0) {
			return exist_ids;
		}
		String sql = "  select id from " + default_table_name
				+ " where id in ( '" + ids.get(0) + "' ";
		for (int i = 1; i < ids.size(); i++) {
			sql += " , '" + ids.get(i) + "'";
		}
		sql += " )";
		Vector<String> vl = getDb().queryListString(sql);
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
	public static Vector<String> getNotExist(Vector<String> ids) {
		Vector<String> all_not = new Vector<String>();
		Hashtable<String, Integer> existH = getExistHash(ids);
		for (int i = 0; i < ids.size(); i++) {
			if (!existH.containsKey(ids.get(i))) {
				all_not.add(ids.get(i));
			}
		}
		return all_not;
	}

	public static int tryInsertToDb(Vector<String> all_ids, int type, int step) {
		if (all_ids.size() == 0) {
			return 0;
		}
		Vector<String> ids = getNotExist(all_ids);
		insertToDb(ids, type, step);
		return ids.size();
	}

	public static void insertToDb(Vector<String> ids, int type, int step) {
		if (ids.size() == 0) {
			return;
		}
		String sql = "insert into `" + default_table_name
				+ "` (`id`,`type`,`action_step`) VALUES ";
		for (int i = 0; i < ids.size(); i++) {
			sql += " ( " + item(ids.get(i)) + "," + item(type) + ","
					+ item(step) + " )";
			if (i == ids.size() - 1) {
				sql += " ; ";
			} else {
				sql += " , ";
			}
		}
		getDb().executequery(sql);
	}

	public void fromHash(Hashtable ht) {
		id = ht.get("id").toString();
		type = Integer.parseInt(ht.get("type").toString());
		action_step = Integer.parseInt(ht.get("action_step").toString());
	}

	public void updateToDb() {
		String sql = " update " + default_table_name + " set ";
		sql += item("type", type) + " , ";
		sql += item("action_step", action_step) + " ";
		sql += " where " + item("id", id);
		getDb().executequery(sql);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAction_step() {
		return action_step;
	}

	public void setAction_step(int action_step) {
		this.action_step = action_step;
	}

}