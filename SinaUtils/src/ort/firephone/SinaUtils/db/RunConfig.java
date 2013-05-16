package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;
import com.skyzoo.Jutil.DBConnections;

public class RunConfig extends Fdb {
	final public static String PIC_DIR_NUM = "pic_dir_num";
	final public static String PIC_PIC_NUM = "pic_pic_num";
	final public static String COMMENT_MSG = "comment_msg";
	final public static String REPOSTME_MAXNUM = "repostme_maxnum";
	final public static String NAME_PHPPROXY = "phpProxy";

	public static String table_name = "run_config";
	String name;
	String sub_name;
	String value = "";
	long value_long = 0L;
	int type = 1;

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

	public static RunConfig getByName(String name, String sub_name) {
		String sql = "select * from " + table_name + " where "
				+ item("name", name) + " and " + item("sub_name", sub_name);
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		if (vh.size() == 1) {
			RunConfig one = new RunConfig();
			one.fromHash(vh.get(0));
			return one;
		} else {
			return null;
		}
	}

	public static Vector<RunConfig> getByName(String name) {
		Vector<RunConfig> all = new Vector<RunConfig>();
		String sql = "select * from " + table_name + " where "
				+ item("name", name);
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		for (int i = 0; i < vh.size(); i++) {
			RunConfig one = new RunConfig();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public void updateToDb() {
		String sql = "update " + table_name + "  set ";
		sql += item("value", value) + " , ";
		sql += item("value_long", value_long) + " ";
		sql += " where " + item("name", name);
		sql += " and " + item("sub_name", sub_name);
		getDb().executequery(sql);
	}

	public void insertToDb() {
		String sql = "insert into " + table_name + "  set ";
		sql += item("value", value) + " , ";
		sql += item("value_long", value_long) + " , ";
		sql += item("name", name) + " , ";
		sql += item("sub_name", sub_name);
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
		sub_name = h.get("sub_name");
		value = h.get("value");
		value_long = Integer.parseInt(h.get("value_long"));
		type = Integer.parseInt(h.get("type"));
	}

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
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
		return (int) value_long;
	}

	public void setValue_int(int value_int) {
		this.value_long = value_int;
	}

	public long getValue_long() {
		return value_long;
	}

	public void setValue_long(long value_long) {
		this.value_long = value_long;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
