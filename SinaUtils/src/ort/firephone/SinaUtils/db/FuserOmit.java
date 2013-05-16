package ort.firephone.SinaUtils.db;

import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class FuserOmit extends Fdb{
	Long sina_id;
	int code;
	public static String table_name = "user_omit";

	public static boolean isExist(long sina_id) {
		String sql = "select * from " + table_name + " where sina_id="
				+ sina_id;
		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 0) {
			return false;
		}
		return true;
	}

	public static Vector<Long> getAll() {
		Vector<Long> sina_ids = null;
		String sql = "select sina_id from " + table_name;
		sina_ids = getDb().queryListLong(sql);
		return sina_ids;
	}

	public void insertIntoDb() {
		String sql = " insert into " + table_name + " set";
		sql += " sina_id=" + sina_id + " , ";
		sql += " code=" + code + " ";
		getDb().executequery(sql);

	}

	public Long getSina_id() {
		return sina_id;
	}

	public void setSina_id(Long sina_id) {
		this.sina_id = sina_id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
