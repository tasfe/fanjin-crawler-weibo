package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class Fcorpse extends Fdb{

	final public static int ACTIVE_ACTIVE = 1;
	final public static int ACTIVE_NOACTIVE = 0;
	final public static int ACTIVE_LOCKED = 101;
	final public static int ACTIVE_DEAD = 102;
	int id;
	String app_key;
	String app_secret;
	String sina_user;
	Long sina_id;
	String sina_pass;
	String oauth_name;
	String oauth_token;
	String oauth_token_secret;
	int active;

	public static final String table_name = "corpse";

	public static Vector<Fcorpse> getEmpty() {
		Vector<Fcorpse> all = new Vector<Fcorpse>();
		String sql = "select * from  " + table_name
				+ " where  oauth_token = '' or oauth_token_secret=''";
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			Fcorpse one = new Fcorpse();
			one.sina_id = Long.parseLong(h.get("sina_id").toString());
			one.app_key = h.get("app_key").toString();
			one.app_secret = h.get("app_secret").toString();
			one.sina_user = h.get("sina_user").toString();
			one.sina_pass = h.get("sina_pass").toString();
			one.oauth_name = h.get("oauth_name").toString();
			one.oauth_token = h.get("oauth_token").toString();
			one.oauth_token_secret = h.get("oauth_token_secret").toString();
			one.active = Integer.parseInt(h.get("active").toString());
			all.add(one);
		}
		return all;
	}

	public static Vector<Fcorpse> getAllActive() {
		Vector<Fcorpse> all = new Vector<Fcorpse>();
		String sql = "select * from  "
				+ table_name
				+ " where active=1 and oauth_token <> '' and oauth_token_secret<>''";
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			Fcorpse one = new Fcorpse();
			one.sina_id = Long.parseLong(h.get("sina_id").toString());
			one.app_key = h.get("app_key").toString();
			one.app_secret = h.get("app_secret").toString();
			one.sina_user = h.get("sina_user").toString();
			one.sina_pass = h.get("sina_pass").toString();
			one.oauth_name = h.get("oauth_name").toString();
			one.oauth_token = h.get("oauth_token").toString();
			one.oauth_token_secret = h.get("oauth_token_secret").toString();
			one.active = Integer.parseInt(h.get("active").toString());
			all.add(one);
		}
		return all;
	}

	public boolean isExist(String sina_user) {
		String sql = " select * from " + table_name + " where  sina_user='"
				+ sina_user + "' ";
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public void insertToDB() {
		String sql = "";
		String sql_select = "";
		sql_select += "sina_id=" + sina_id + " ,";
		sql_select += "app_key='" + app_key + "' ,";
		sql_select += "app_secret='" + app_secret + "' ,";
		sql_select += "sina_user='" + sina_user + "' ,";
		sql_select += "sina_pass='" + sina_pass + "' ,";
		sql_select += "oauth_name='" + oauth_name + "' ,";
		sql_select += "oauth_token='" + oauth_token + "' ,";
		sql_select += "oauth_token_secret='" + oauth_token_secret + "' ,";
		sql_select += "active=" + active + " ";
		sql += "insert into " + table_name + " set ";
		sql += sql_select;
		getDb().executequery(sql);
	}

	public void insertOrUpdateToDB() {
		String sql = "";
		String sql_select = "";
		sql_select += "sina_id=" + sina_id + " ,";
		sql_select += "app_key='" + app_key + "' ,";
		sql_select += "app_secret='" + app_secret + "' ,";
		sql_select += "sina_pass='" + sina_pass + "' ,";
		sql_select += "oauth_name='" + oauth_name + "' ,";
		sql_select += "oauth_token='" + oauth_token + "' ,";
		sql_select += "oauth_token_secret='" + oauth_token_secret + "' ,";
		sql_select += "active=" + active + " ";

		if (isExist(this.sina_user)) {
			sql += "update " + table_name + " set ";
			sql += sql_select;
			sql += " where  sina_user='" + sina_user + "' ";
		} else {
			sql += "insert into " + table_name + " set ";
			sql += sql_select;
		}
		getDb().executequery(sql);
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getOauth_name() {
		return oauth_name;
	}

	public void setOauth_name(String oauthName) {
		oauth_name = oauthName;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getApp_secret() {
		return app_secret;
	}

	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}

	public String getSina_user() {
		return sina_user;
	}

	public void setSina_user(String sina_user) {
		this.sina_user = sina_user;
	}

	public Long getSina_id() {
		return sina_id;
	}

	public void setSina_id(Long sina_id) {
		this.sina_id = sina_id;
	}

	public String getSina_pass() {
		return sina_pass;
	}

	public void setSina_pass(String sina_pass) {
		this.sina_pass = sina_pass;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_token_secret() {
		return oauth_token_secret;
	}

	public void setOauth_token_secret(String oauth_token_secret) {
		this.oauth_token_secret = oauth_token_secret;
	}

}