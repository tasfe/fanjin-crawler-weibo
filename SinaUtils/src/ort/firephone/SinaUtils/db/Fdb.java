package ort.firephone.SinaUtils.db;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ort.firephone.SinaUtils.Env;

import com.skyzoo.Jutil.DBConnections;

public class Fdb {

	public static DBConnections getDb() {
		return Env.getDb();
	}

	public static String item(String value) {
		return "  '" + S(value) + "' ";
	}

	public static String item(Date value) {
		return " '" + D(value) + "' ";
	}

	public static String item(int value) {
		return " " + value + " ";
	}

	public static String item(long value) {
		return " " + value + " ";
	}

	public static String item(double value) {
		return " " + value + " ";
	}

	public static String item(URL value) {
		return " '" + U(value) + "' ";
	}

	public static String item(boolean value) {
		return " " + B(value) + " ";
	}

	public static String item(String name, String value) {
		return " `" + name + "` = '" + S(value) + "' ";
	}

	public static String item_greater(String name, Date value) {
		return " `" + name + "` > '" + D(value) + "' ";
	}

	public static String item_less(String name, Date value) {
		return " `" + name + "` < '" + D(value) + "' ";
	}

	public static String item(String name, Date value) {
		return " `" + name + "` = '" + D(value) + "' ";
	}

	public static String item(String name, int value) {
		return " `" + name + "` = " + value + " ";
	}

	public static String item(String name, long value) {
		return " `" + name + "` = " + value + " ";
	}

	public static String item(String name, double value) {
		return " `" + name + "` = " + value + " ";
	}

	public static String item(String name, URL value) {
		return " `" + name + "` = '" + U(value) + "' ";
	}

	public static String item(String name, boolean value) {
		return " `" + name + "` = " + B(value) + " ";
	}

	public static String S(String s) {
		if (s == null) {
			return "";
		}
		s = s.replaceAll("\\\\", "\\\\\\\\");
		s = s.replaceAll("\n", "\\\\n");

		return s.replaceAll("'", "\\\\\'");
	}

	public static String D(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		if (d == null) {
			return "1970-00-00 00:00:00";
		}
		return sdf.format(d);

	}

	public static String U(URL u) {
		if (u == null) {
			return "";
		}
		return S(u.toString());

	}

	public static String B(boolean b) {
		if (b) {
			return "1";
		}
		return "0";
	}
}
