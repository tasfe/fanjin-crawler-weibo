package ort.firephone.SinaUtils.tmp;

import java.sql.Connection;
import java.sql.DriverManager;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;

public class DoForTestData {
	public static void main(String argv[]) {
		Env.initial();
		for (int i = 0; i < 50000; i++) {
			long t1 = System.currentTimeMillis();
			insert(i);
			long t2 = System.currentTimeMillis();
			Log.log("" + i + "  " + (t2 - t1));
		}

	}

	public static void insert(int n) {
		int m = 400;
		Connection con;
		try {
			String sql = "insert into test3 (id,name,v1,v2) values ";
			for (int i = 1; i < m; i++) {
				sql += "(" + (n * m + i) + "," + "'" + (n * m + i) + "'," + "'"
						+ (n * m + i) + "'," + (n * m + i) + "),";
			}
			sql += "(" + (n * m) + "," + "'" + (n * m) + "'," + "'" + (n * m)
					+ "'," + (n * m) + ");";
			Env.getDb().executequery(sql);

		} catch (Exception e) {

		}

	}

}
