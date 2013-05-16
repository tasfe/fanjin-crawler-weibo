package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class Ffavorite_status extends Fdb {
	public static String table_name = "favorite_status";
	long status_id;
	String desc;
	int type;
	int active;
	final public static int TYPE_REPOSE_AND_AT = 4;

	public static Vector<Ffavorite_status> getAllActive() {
		Vector<Ffavorite_status> all = new Vector<Ffavorite_status>();
		String sql = "select * from " + table_name + "  where active=1";
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Ffavorite_status one = new Ffavorite_status();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;

	}

	public void fromHash(Hashtable<String, String> h) {
		status_id = Long.parseLong(h.get("status_id"));
		desc = h.get("desc");
		type = Integer.parseInt(h.get("type"));
		active = Integer.parseInt(h.get("active"));
	}

	public long getStatus_id() {
		return status_id;
	}

	public void setStatus_id(long statusId) {
		status_id = statusId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

}
