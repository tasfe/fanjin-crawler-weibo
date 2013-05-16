package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class UserOauth extends Fdb {
	int id;
	int app_type;
	String app_key;
	String app_secret;
	String username;
	String oauth_name;
	String oauth_token_key;
	String oauth_token_secret;

	public static final String table_name = "user_oauth";

	public static Vector<UserOauth> getByAppType(int app_type) {
		Vector<UserOauth> all = new Vector<UserOauth>();
		String sql = "select * from  " + table_name + " where app_type="
				+ app_type;
		Vector<Hashtable> vh = DBConnections.getInstance("1").query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			UserOauth one = new UserOauth();
			one.app_key = h.get("app_key").toString();
			one.app_secret = h.get("app_secret").toString();
			one.username = h.get("username").toString();
			one.oauth_name = h.get("oauth_name").toString();
			one.oauth_token_key = h.get("oauth_token_key").toString();
			one.oauth_token_secret = h.get("oauth_token_secret").toString();
			one.app_type = Integer.parseInt(h.get("app_type").toString());
			all.add(one);
		}
		return all;
	}

	public static Vector<UserOauth> getByUsername(int app_type, String username) {
		Vector<UserOauth> all = new Vector<UserOauth>();
		String sql = "select * from  " + table_name + " where app_type="
				+ app_type + " and username='" + username + "'";
		Vector<Hashtable> vh = DBConnections.getInstance("1").query(sql);
		for (int i = 0; i < vh.size(); i++) {
			Hashtable h = vh.get(i);
			UserOauth one = new UserOauth();
			one.app_key = h.get("app_key").toString();
			one.app_secret = h.get("app_secret").toString();
			one.username = h.get("username").toString();
			one.oauth_name = h.get("oauth_name").toString();
			one.oauth_token_key = h.get("oauth_token_key").toString();
			one.oauth_token_secret = h.get("oauth_token_secret").toString();
			one.app_type = Integer.parseInt(h.get("app_type").toString());
			all.add(one);
		}
		return all;
	}

	public boolean isExist(int app_type, String username) {
		String sql = " select * from " + table_name + " where  username='"
				+ username + "' and app_type=" + app_type;
		Vector<Hashtable> vh = DBConnections.getInstance("1").query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public void insertToDB() {
		String sql = "";
		String sql_select = "";
		sql_select += "username=" + username + " ,";
		sql_select += "app_key='" + app_key + "' ,";
		sql_select += "app_secret='" + app_secret + "' ,";
		sql_select += "oauth_name='" + oauth_name + "' ,";
		sql_select += "oauth_token_key='" + oauth_token_key + "' ,";
		sql_select += "oauth_token_secret='" + oauth_token_secret + "' ,";
		sql_select += "app_type=" + app_type + " ";
		sql += "insert into " + table_name + " set ";
		sql += sql_select;
		getDb().executequery(sql);
	}

	public void insertOrUpdateToDB() {
		String sql = "";
		String sql_select = "";
		sql_select += "username=" + username + " ,";
		sql_select += "app_key='" + app_key + "' ,";
		sql_select += "app_secret='" + app_secret + "' ,";
		sql_select += "oauth_name='" + oauth_name + "' ,";
		sql_select += "oauth_token_key='" + oauth_token_key + "' ,";
		sql_select += "oauth_token_secret='" + oauth_token_secret + "' ,";
		sql_select += "app_type=" + app_type + " ";

		if (isExist(this.app_type, this.username)) {
			sql += "update " + table_name + " set ";
			sql += sql_select;
			sql += " where  username='" + username + "' ";
		} else {
			sql += "insert into " + table_name + " set ";
			sql += sql_select;
		}
		getDb().executequery(sql);
	}

	public int getApp_type() {
		return app_type;
	}

	public void setApp_type(int appType) {
		app_type = appType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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



	public String getOauth_token_key() {
		return oauth_token_key;
	}

	public void setOauth_token_key(String oauthTokenKey) {
		oauth_token_key = oauthTokenKey;
	}

	public String getOauth_token_secret() {
		return oauth_token_secret;
	}

	public void setOauth_token_secret(String oauth_token_secret) {
		this.oauth_token_secret = oauth_token_secret;
	}
}
