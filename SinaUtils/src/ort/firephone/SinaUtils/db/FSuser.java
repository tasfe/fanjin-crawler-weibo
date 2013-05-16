package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;

import com.skyzoo.Jutil.DBConnections;

public class FSuser extends Fdb {
	long id;
	String screen_name;
	String name;
	int province;
	int city;
	String location;
	String description;
	String url;
	String profile_image_url;
	String domain;
	String gender;
	int followers_count;
	int friends_count;
	int statuses_count;
	int favourites_count;
	Date created_at;
	boolean geo_enabled;
	boolean verified;
	public static String table_name = "s_user";

	static String select_sql = " id,screen_name,name,province,city,location,description,url,profile_image_url,domain,gender,followers_count,friends_count,statuses_count,favourites_count,UNIX_TIMESTAMP(created_at) as created_at,geo_enabled,verified";

	public static boolean isExist(long id) {
		String sql = " select id from " + table_name + " where id=" + id;
		Vector<Hashtable> vh = Env.getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<FSuser> getAllLimited(int limit_s, int max) {
		Vector<FSuser> all = new Vector<FSuser>();
		String sql = "select " + select_sql + " from " + table_name
				+ " where statuses_count > 3 limit " + limit_s + "," + max;
		Vector<Hashtable> vh = Env.getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			FSuser fsuser = new FSuser();
			fsuser.fromHash(vh.get(i));
			all.add(fsuser);
		}
		return all;
	}

	public void fromHash(Hashtable ht) {
		this.id = Long.parseLong(ht.get("id").toString());
		this.screen_name = ht.get("screen_name").toString();
		this.name = ht.get("name").toString();
		this.province = Integer.parseInt(ht.get("province").toString());
		this.city = Integer.parseInt(ht.get("city").toString());
		this.location = ht.get("location").toString();
		this.description = ht.get("description").toString();
		this.url = ht.get("url").toString();
		this.profile_image_url = ht.get("profile_image_url").toString();
		this.domain = ht.get("domain").toString();
		this.gender = ht.get("gender").toString();
		this.followers_count = Integer.parseInt(ht.get("followers_count")
				.toString());
		this.friends_count = Integer.parseInt(ht.get("friends_count")
				.toString());
		this.statuses_count = Integer.parseInt(ht.get("statuses_count")
				.toString());
		this.favourites_count = Integer.parseInt(ht.get("favourites_count")
				.toString());
		String created_at_str = ht.get("created_at").toString();
		created_at = new Date();
		created_at.setTime(Long.parseLong(created_at_str + "000"));
		if (ht.get("geo_enabled").equals("1")) {
			this.geo_enabled = true;
		} else {
			this.geo_enabled = false;
		}
		if (ht.get("verified").equals("1")) {
			this.verified = true;
		} else {
			this.verified = false;
		}
	}

	/**
	 * 
	 * @param ids
	 *            输入的ID
	 * @return 所有输入ID中数据库里也有的部份
	 */
	public static Hashtable<Long, Integer> getExistHash(Vector<Long> ids) {
		Hashtable<Long, Integer> exist_ids = new Hashtable<Long, Integer>();
		if (ids.size() == 0) {
			return exist_ids;
		}
		String sql = "  select id from " + table_name + " where id in ( "
				+ ids.get(0);
		for (int i = 1; i < ids.size(); i++) {
			sql += " , " + ids.get(i);
		}
		sql += " )";
		Vector<Long> vl = getDb().queryListLong(sql);
		for (int i = 0; i < vl.size(); i++) {
			exist_ids.put(vl.get(i), 1);
		}
		return exist_ids;
	}

	public void insertIntoDB() {
		String sql = "insert into " + table_name + " set ";
		sql += item("id", id) + ",";
		sql += item("screen_name", screen_name) + ",";
		sql += item("name", name) + ",";
		sql += item("province", province) + ",";
		sql += item("city", city) + ",";
		sql += item("location", location) + ",";
		sql += item("description", description) + ",";
		sql += item("url", url) + ",";
		sql += item("profile_image_url", profile_image_url) + ",";
		sql += item("domain", domain) + ",";
		sql += item("gender", gender) + ",";
		sql += item("followers_count", followers_count) + ",";
		sql += item("friends_count", friends_count) + ",";
		sql += item("statuses_count", statuses_count) + ",";
		sql += item("favourites_count", favourites_count) + ",";
		sql += item("created_at", created_at) + ",";
		sql += item("geo_enabled", geo_enabled) + ",";
		sql += item("verified", verified) + "";
		getDb().executequery(sql);
	}

	public static void insertIntoDb(Vector<FSuser> users) {
		if (users.size() == 0) {
			return;
		}
		String sql = "INSERT INTO `s_user` (`id`, `screen_name`, `name`, `province`,";
		sql += " `city`, `location`, `description`, `url`,";
		sql += "`profile_image_url`, `domain`, `gender`, `followers_count`, ";
		sql += "`friends_count`, `statuses_count`, `favourites_count`, `created_at`,";
		sql += "`geo_enabled`, `verified`, `fp_active`) VALUES";
		for (int i = 0; i < users.size(); i++) {
			FSuser u = users.get(i);
			sql += "(" + item(u.getId()) + "," + item(u.getScreen_name()) + ","
					+ item(u.getName()) + "," + item(u.getProvince()) + ",";
			sql += item(u.getCity()) + "," + item(u.getLocation()) + ","
					+ item(u.getDescription()) + "," + item(u.getUrl()) + ",";
			sql += item(u.getProfile_image_url()) + "," + item(u.getDomain())
					+ "," + item(u.getGender()) + ","
					+ item(u.getFollowers_count()) + ",";
			sql += item(u.getFriends_count()) + ","
					+ item(u.getStatuses_count()) + ","
					+ item(u.getFavourites_count()) + ","
					+ item(u.getCreated_at()) + ",";
			sql += item(u.isGeo_enabled()) + "," + item(u.isVerified()) + ","
					+ item(1) + ")";
			if (i == users.size() - 1) {
				sql += ";";
			} else {
				sql += ",";
			}

		}
		getDb().executequery(sql);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public boolean isGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(boolean geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

}
