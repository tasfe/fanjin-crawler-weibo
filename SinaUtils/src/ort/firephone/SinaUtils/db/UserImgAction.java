package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

public class UserImgAction extends Fdb {
	public static String default_table_name = "user_img_action";
	final public static int STEP_RESERVE = 100000000;
	final public static int STEP_INFO = 100000001;
	final public static int STEP_ERROR = 200000000;

	final public static int TYPE_UNKNOWN = 0;
	final public static int TYPE_NORMAL = 1;
	final public static int TYPE_DEFAULT = 100001;
	final public static int TYPE_ERROR = 200001;
	private long user_id;
	private String user_name;
	private String url;
	private int action_step;
	private int img_type = TYPE_UNKNOWN;

	public static UserImgAction getByUserId(long user_id) {
		String sql = "select * from " + default_table_name + " where "
				+ item("user_id", user_id);
		Vector<Hashtable> vh = getDb().query(sql);
		if(vh.size()>=1){
			UserImgAction one = new UserImgAction();
			one.fromHash(vh.get(0));
			return one;
		}else{
			return null;
		}
	}

	public static Vector<UserImgAction> getReserve(int max, int offset) {
		return getReserve(default_table_name, max, offset);
	}

	public static Vector<UserImgAction> getReserve(String table_name, int max,
			int offset) {
		Vector<UserImgAction> all = new Vector<UserImgAction>();
		String sql = "select * from " + table_name + " where "
				+ item("action_step", STEP_RESERVE) + " limit " + offset + ","
				+ max;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			UserImgAction one = new UserImgAction();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public static void inserIntoDb(Vector<UserImgAction> all) {
		inserIntoDb(all, default_table_name);
	}

	public static void inserIntoDb(Vector<UserImgAction> all, String table_name) {
		if (all.size() == 0) {
			return;
		}
		String sql = "insert into " + table_name;
		sql += "  (`user_id`,`user_name`,`url`,`action_step`,`img_type`) VALUES ";
		for (int i = 0; i < all.size(); i++) {
			sql += "(" + item(all.get(i).getUser_id()) + " , "
					+ item(all.get(i).getUser_name()) + " , "
					+ item(all.get(i).getUrl()) + " , "
					+ item(all.get(i).getAction_step()) + " , "
					+ item(all.get(i).getImg_type()) + ")";
			if (i == all.size() - 1) {
				sql += ";";
			} else {
				sql += ",";
			}
		}
		getDb().executequery(sql);
	}

	public void inserIntoDb() {
		inserIntoDb(default_table_name);
	}

	public void inserIntoDb(String table_name) {
		String sql = "insert into " + table_name + " set ";
		sql += item("user_id", user_id) + ",";
		sql += item("user_name", user_name) + ",";
		sql += item("url", url) + ",";
		sql += item("action_step", action_step) + ",";
		sql += item("img_type", img_type) + " ";
		getDb().executequery(sql);
	}

	public void updateToDb() {
		updateToDb(default_table_name);
	}

	public void updateToDb(String table_name) {
		String sql = "update " + table_name + " set ";
		sql += item("user_name", user_name) + ",";
		sql += item("url", url) + ",";
		sql += item("action_step", action_step) + ",";
		sql += item("img_type", img_type) + " ";
		sql += " where " + item("user_id", user_id);
		getDb().executequery(sql);
	}

	public void fromHash(Hashtable ht) {
		user_id = Long.parseLong(ht.get("user_id").toString());
		user_name = ht.get("user_name").toString();
		url = ht.get("url").toString();
		action_step = Integer.parseInt(ht.get("action_step").toString());
		img_type = Integer.parseInt(ht.get("img_type").toString());
	}

	public int getImg_type() {
		return img_type;
	}

	public void setImg_type(int imgType) {
		img_type = imgType;
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

	public int getAction_step() {
		return action_step;
	}

	public void setAction_step(int action_step) {
		this.action_step = action_step;
	}

}
