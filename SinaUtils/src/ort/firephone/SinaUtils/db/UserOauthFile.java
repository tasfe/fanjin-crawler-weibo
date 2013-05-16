package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

public class UserOauthFile extends Fdb {
	final public static String default_table_name = "file";
	int id;
	String path;
	String fileName;

	public static UserOauthFile getById(int id) {
		String sql = "select * from " + default_table_name + " where id=" + id;
		Vector<Hashtable> all = getDb().query(sql);
		if (all.size() >= 1) {
			UserOauthFile one = new UserOauthFile();
			one.fromHash(all.get(0));
			return one;
		}
		return null;
	}

	public void fromHash(Hashtable<String, String> ht) {
		this.id = Integer.parseInt(ht.get("id"));
		this.path = ht.get("path");
		this.fileName = ht.get("fileName");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
