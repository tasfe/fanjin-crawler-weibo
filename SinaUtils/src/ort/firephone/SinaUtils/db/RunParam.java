package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class RunParam extends Fdb {
	final public static String PIC_DIR_NUM = "pic_dir_num";
	final public static String PIC_PIC_NUM = "pic_pic_num";
	final public static String COMMENT_MSG = "comment_msg";
	final public static String REPOSTME_MAXNUM = "repostme_maxnum";
	final public static String USERINFO_PROC_NUM = "userinfo_proc_num";
	final public static String USERINFO_ACTION_NUM = "userinfo_action_num";
	final public static String USERIMG_ACTION_NUM = "userimg_action_num";
	
	public static String table_name = "run_param";
	String name;
	String value;
	int value_int;
	int type;

	public static Vector<RunParam> getAll() {
		Vector<RunParam> all_param = new Vector<RunParam>();
		String sql = "select * from " + table_name;
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		for (int i = 0; i < vh.size(); i++) {
			RunParam one = new RunParam();
			one.fromHash(vh.get(i));
			all_param.add(one);
		}
		return all_param;
	}

	public static RunParam getByName(String name) {
		String sql = "select * from " + table_name + " where "
				+ item("name", name);
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		if (vh.size() == 1) {
			RunParam one = new RunParam();
			one.fromHash(vh.get(0));
			return one;
		} else {
			return null;
		}

	}

	public void updateToDb() {
		String sql = "update " + table_name + "  set ";
		sql += item("value", value) + " , ";
		sql += item("value_int", value_int) + " ";
		sql += " where " + item("name", name);
		getDb().executequery(sql);
	}

	public void insertToDb() {
		String sql = "insert into " + table_name + "  set ";
		sql += item("value", value) + " , ";
		sql += item("value_int", value_int) + " , ";
		sql += item("name", name);
		getDb().executequery(sql);
	}

	public void insertOrUpdateToDb() {
		if (getByName(this.getName()) == null) {
			insertToDb();
		} else {
			updateToDb();
		}
	}

	public void fromHash(Hashtable<String, String> h) {
		name = h.get("name");
		value = h.get("value");
		value_int = Integer.parseInt(h.get("value_int"));
		type = Integer.parseInt(h.get("type"));

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getValue_int() {
		return value_int;
	}

	public void setValue_int(int valueInt) {
		value_int = valueInt;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
