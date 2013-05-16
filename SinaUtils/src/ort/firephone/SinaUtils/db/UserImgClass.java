package ort.firephone.SinaUtils.db;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;

public class UserImgClass {
	public static String default_table_name = "user_img_class";
	int class_id;
	byte data[];

	public static Vector<UserImgClass> fromDb() {
		Vector<UserImgClass> all = new Vector<UserImgClass> ();
		byte[] data = null;
		String sql = "select id,user_id,img180 from " + default_table_name;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		InputStream result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(Env.db_url, Env.db_user,
					Env.db_pwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				result = rs.getBlob("img180").getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len = 0;
				byte buf[] = new byte[2048];
				while ((len = result.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				result.close();
				out.close();
				data = out.toByteArray();
				UserImgClass one = new UserImgClass();
				one.setClass_id(id);
				one.setData(data);
				all.add(one);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			closeConnection(con);
			closeConnection(stmt);
		}
		return all;
	}

	/**
	 * 
	 * @param all
	 * @param data_30
	 * @return -1 表未没有匹配的
	 */
	public static int getClassId(Vector<UserImgClass> all, byte data_180[]) {
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).check(data_180)) {
				return all.get(i).getClass_id();
			}
		}
		return -1;
	}

	public boolean check(byte data_180[]) {
		if (data_180.length != this.data.length) {
			return false;
		}
		for (int i = 0; i < data_180.length; i++) {
			if (data_180[i] != this.data[i]) {
				return false;
			}
		}
		return true;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			//
		}
	}

	public static void closeConnection(BufferedInputStream bis) {
		try {
			if (bis != null) {
				bis.close();
			}
		} catch (Exception e) {
			// none
		}
	}

	public static void closeConnection(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			// none
		}
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int classId) {
		class_id = classId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
