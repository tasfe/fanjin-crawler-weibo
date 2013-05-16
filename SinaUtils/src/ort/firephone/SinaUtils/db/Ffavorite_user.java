package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class Ffavorite_user extends Fdb {
	public static String table_name = "favorite_user";
	long sina_id;
	String name;
	int type;
	final public static int TYPE_FOLLOWS = 1;
	final public static int TYPE_REPOST = 2;
	final public static int TYPE_NIGHT = 3;
	final public static int TYPE_COMMENT = 4;
	final public static int TYPE_COMMENT_ANYWAY = 11;
	// 对每用户间隔一定的时间才能评论其下一条
	final public static int TYPE_ONE_COMMENT = 5;

	public static Vector<Ffavorite_user> getAll() {
		Vector<Ffavorite_user> all = new Vector<Ffavorite_user>();
		String sql = "select * from " + table_name + " ";
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			Ffavorite_user one = new Ffavorite_user();
			one.sina_id = Long.parseLong(h.get("sina_id").toString());
			one.name = h.get("name").toString();
			one.type = Integer.parseInt(h.get("type").toString());
			all.add(one);
		}
		return all;
	}

	public static Vector<Ffavorite_user> getByType(int type) {
		Vector<Ffavorite_user> all = new Vector<Ffavorite_user>();
		String sql = "select * from " + table_name + " where type=" + type;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			Ffavorite_user one = new Ffavorite_user();
			one.sina_id = Long.parseLong(h.get("sina_id").toString());
			one.name = h.get("name").toString();
			one.type = Integer.parseInt(h.get("type").toString());
			all.add(one);
		}
		return all;
	}

	public static Vector<Ffavorite_user> getByType(int type[]) {
		Vector<Ffavorite_user> all = new Vector<Ffavorite_user>();
		String type_sql = "";
		if (type.length == 0) {
			return all;
		} else {
			type_sql = " type=" + type[0];
			for (int i = 1; i < type.length; i++) {
				type_sql += " or type=" + type[i];
			}
		}

		String sql = "select * from " + table_name + " where " + type_sql;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			Ffavorite_user one = new Ffavorite_user();
			one.sina_id = Long.parseLong(h.get("sina_id").toString());
			one.name = h.get("name").toString();
			one.type = Integer.parseInt(h.get("type").toString());
			all.add(one);
		}
		return all;
	}

	public void insertIntoDb() {
		String sql = "insert into " + table_name + " set sina_id=" + sina_id
				+ " , name='" + name + "' , type=" + type;
		getDb().executequery(sql);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getSina_id() {
		return sina_id;
	}

	public void setSina_id(long sina_id) {
		this.sina_id = sina_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
