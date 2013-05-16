package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class PicDir extends Pic {
	public static String table_name = "pic_dir";
	private int id;
	private String name;
	private String desc;
	private int size;
	private int file_num;
	private int level;
	private int active = ACTIVE_NOT_READY;
	private String source_dir;

	public void insertToDB() {
		String sql = " insert into `" + table_name + "`  set ";
		sql += item("id", id) + " , ";
		sql += item("name", name) + " , ";
		sql += item("desc", desc) + " , ";
		sql += item("size", size) + " , ";
		sql += item("file_num", file_num) + " , ";
		sql += item("level", level) + " , ";
		sql += item("source_dir", source_dir) + " , ";
		sql += item("active", active) + "  ";
		getDb().executequery(sql);
	}

	public void updateToDb() {
		String sql = " update `" + table_name + "`  set ";
		sql += item("name", name) + " , ";
		sql += item("desc", desc) + " , ";
		sql += item("size", size) + " , ";
		sql += item("file_num", file_num) + " , ";
		sql += item("level", level) + " , ";
		sql += item("source_dir", source_dir) + " , ";
		sql += item("active", active) + "  where ";
		sql += item("id", id);
		getDb().executequery(sql);
	}

	public static Vector<PicDir> getAllDirs() {
		Vector<PicDir> all_dirs = new Vector<PicDir>();
		String sql = "select * from " + table_name;
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		for (int i = 0; i < vh.size(); i++) {
			PicDir one = new PicDir();
			one.fromHash(vh.get(i));
			all_dirs.add(one);
		}
		return all_dirs;
	}

	public void fromHash(Hashtable<String, String> ht) {
		this.id = Integer.parseInt(ht.get("id"));
		this.size = Integer.parseInt(ht.get("size"));
		this.file_num = Integer.parseInt(ht.get("file_num"));
		this.level = Integer.parseInt(ht.get("level"));
		this.active = Integer.parseInt(ht.get("active"));
		this.name = ht.get("name");
		this.desc = ht.get("desc");
		this.source_dir = ht.get("source_dir");
	}

	public static int getMaxId() {
		String sql = "select max(id) as id from " + table_name;
		Vector<Hashtable<String, String>> vh =getDb()
				.query(sql);
		return Integer.parseInt(vh.get(0).get("id"));
	}

	public static boolean hasSameSourceDir(String source_dir) {
		String sql = "select id from " + table_name + " where "
				+ item("source_dir", source_dir);
		Vector<Hashtable<String, String>> vh = getDb()
				.query(sql);
		if (vh.size() == 1) {
			return true;
		}
		return false;
	}

	public String getSource_dir() {
		return source_dir;
	}

	public void setSource_dir(String source_dir) {
		this.source_dir = source_dir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFile_num() {
		return file_num;
	}

	public void setFile_num(int file_num) {
		this.file_num = file_num;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}