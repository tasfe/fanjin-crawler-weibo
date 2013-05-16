package ort.firephone.SinaUtils.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Joke;

public class jokemdb {
	public static void main(String argv[]) {
		Env.initial();
		process_B();

	}

	public static void process_B() {
		try {
			Hashtable<Integer, String> cat_h = new Hashtable<Integer, String>();
			cat_h.put(1, "儿童篇");
			cat_h.put(2, "夫妻篇");
			cat_h.put(4, "医疗篇");
			cat_h.put(5, "爱情篇");
			cat_h.put(9, "家庭篇");
			cat_h.put(10, "政治篇");
			cat_h.put(11, "电脑篇");
			cat_h.put(12, "军事篇");
			cat_h.put(13, "古代篇");
			cat_h.put(14, "文化篇");
			cat_h.put(15, "艺术篇");
			cat_h.put(16, "经营篇");
			cat_h.put(17, "司法篇");
			cat_h.put(18, "体育篇");
			cat_h.put(19, "交通篇");
			cat_h.put(20, "宗教篇");
			cat_h.put(21, "鬼话篇");
			cat_h.put(22, "名人篇");
			cat_h.put(23, "校园篇");
			cat_h.put(24, "交往篇");
			cat_h.put(25, "愚人篇");
			cat_h.put(26, "民间篇");
			cat_h.put(27, "综合篇");
			cat_h.put(28, "顺口溜");
			cat_h.put(29, "动物篇");
			cat_h.put(30, "成人篇");

			String sql_url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=D:\\b.mdb";
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection(sql_url);
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery("select * from learning");
			ResultSetMetaData rsmt = rs.getMetaData();
			while (rs.next()) {
				try {
					int articleid = rs.getInt("articleid");
					int typeid = rs.getInt("typeid");
					int hits = rs.getInt("hits");
					if (!cat_h.containsKey(typeid)) {
						System.out.println("------------------------------");
						System.out.println("typeid=" + typeid);
					}
					String cat = cat_h.get(typeid);

					String title = new String(rs.getBytes("title"), "gbk");
					String content = new String(rs.getBytes("content"), "gbk");
					String dateandtime = rs.getString("dateandtime");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;&nbsp;", " ");
					content = content.replaceAll("&nbsp;", " ");
					content = content.replaceAll("<br>", "");
					content = content.replaceAll("\r\n\r\n", "\r\n");
					content = content.replaceAll("\n\n", "\n");

					if (content.startsWith(title + "\r\n")) {
						content = content.substring(title.length() + 2);
					} else if (content.startsWith(title + "\n")) {
						content = content.substring(title.length() + 1);
					}

					Joke joke = new Joke();
					joke.setCat(cat);
					joke.setTitle(title);
					joke.setSize(content.length());
					joke.setContent(content);
					joke.setSource("2");
					joke.insertIntoDb();
					System.out.println(articleid+" "+cat+" "+title);
				

					// System.out.println(nSize+ "  " + sContent.length());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void process_A() {
		insertA("D:\\joke\\1.mdb", "爱情");
		insertA("D:\\joke\\2.mdb", "爆笑");
		insertA("D:\\joke\\3.mdb", "电脑");
		insertA("D:\\joke\\4.mdb", "恶心");
		insertA("D:\\joke\\5.mdb", "儿童");
		insertA("D:\\joke\\6.mdb", "夫妻");
		insertA("D:\\joke\\7.mdb", "古代");
		insertA("D:\\joke\\8.mdb", "鬼话");
		insertA("D:\\joke\\9.mdb", "家庭");
		insertA("D:\\joke\\10.mdb", "交通");
		insertA("D:\\joke\\11.mdb", "交往");
		insertA("D:\\joke\\12.mdb", "经营");
		insertA("D:\\joke\\13.mdb", "军事");
		insertA("D:\\joke\\14.mdb", "民间");
		insertA("D:\\joke\\15.mdb", "名人");
		insertA("D:\\joke\\16.mdb", "顺口溜");
		insertA("D:\\joke\\17.mdb", "司法");
		insertA("D:\\joke\\18.mdb", "体育");
		insertA("D:\\joke\\19.mdb", "文化");
		insertA("D:\\joke\\20.mdb", "校园");
		insertA("D:\\joke\\21.mdb", "医疗");
		insertA("D:\\joke\\22.mdb", "愚人");
		insertA("D:\\joke\\23.mdb", "政治");
		insertA("D:\\joke\\24.mdb", "宗教");
		insertA("D:\\joke\\25.mdb", "综合");
	}

	public static void insertA(String url, String cat) {
		try {
			String sql_url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="
					+ url;

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection(sql_url);
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery("select * from TextTable");
			ResultSetMetaData rsmt = rs.getMetaData();
			while (rs.next()) {
				int Index = rs.getInt("Index");
				int nSize = rs.getInt("nSize");
				String sTitle = new String(rs.getBytes("sTitle"), "gbk");
				String sContent = new String(rs.getBytes("sContent"), "gbk");
				String sUpdate = rs.getString("sUpdate");
				sContent = sContent.replaceAll("\r\n\r\n", "\r\n");
				sContent = sContent.replaceAll("\n\n", "\n");
				if (sContent.startsWith(sTitle + "\r\n")) {
					sContent = sContent.substring(sTitle.length() + 2);
				} else if (sContent.startsWith(sTitle + "\n")) {
					sContent = sContent.substring(sTitle.length() + 1);
				}
				Joke joke = new Joke();
				joke.setCat(cat);
				joke.setTitle(sTitle);
				joke.setSize(sContent.length());
				joke.setContent(sContent);
				joke.setSource("1");
				joke.insertIntoDb();
				System.out.println(Index);

				// System.out.println(nSize+ "  " + sContent.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
