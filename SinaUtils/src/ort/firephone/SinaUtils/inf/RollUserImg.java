package ort.firephone.SinaUtils.inf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import com.skyzoo.Jutil.EatAppleMuti;
import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.UserImgAction;
import ort.firephone.SinaUtils.db.UserImgClass;
import ort.firephone.SinaUtils.db.UserImgSame;

public class RollUserImg extends EatAppleMuti {

	public Vector<UserImgClass> all_class = null;

	public static void main(String argv[]) {
		try {
			Env.initial();

			RollUserImg rollUserImg = new RollUserImg();

			rollUserImg.all_class = UserImgClass.fromDb();
			if (rollUserImg.all_class == null
					|| rollUserImg.all_class.size() == 0) {
				Log.log("default_img180 not get");
				return;
			}
			rollUserImg.setThreadNum(15);
			rollUserImg.start();
			rollUserImg.processAllApple();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processAllApple() {
		int once_max = 10000;
		Hashtable<Long, Integer> this_hash = new Hashtable<Long, Integer>();
		Hashtable<Long, Integer> last_hash = new Hashtable<Long, Integer>();
		Vector<UserImgAction> items = UserImgAction.getReserve(once_max,
				Env.RollUserImg_offset);

		while (items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				this_hash.put(items.get(i).getUser_id(), 1);
				if (last_hash.containsKey(items.get(i).getUser_id())) {
					continue;
				}
				this.addApple(items.get(i));
			}

			while (this.getAppleCount() > once_max / 10) {
				Sleep(10 * 1000L);
				Log.log("full end");
			}
			items = UserImgAction.getReserve(once_max, Env.RollUserImg_offset);
			last_hash = this_hash;
			this_hash = new Hashtable<Long, Integer>();
		}
		Log.log("ended");
	}

	public static void Sleep(long usec) {
		try {
			Thread.sleep(usec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean eat(Object obj) {
		UserImgAction imgAction = (UserImgAction) obj;
		String img_url_50 = imgAction.getUrl();
		String img_url_30 = img_url_50.replaceAll("/50/", "/30/");
		String img_url_180 = img_url_50.replaceAll("/50/", "/180/");
		int ret_type = UserImgAction.TYPE_UNKNOWN;
		try {
			ret_type = getAndSaveToDb(imgAction.getUser_id(), imgAction
					.getUser_name(), img_url_30, img_url_50, img_url_180);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret_type == UserImgAction.TYPE_NORMAL) {
			imgAction.setAction_step(UserImgAction.STEP_INFO);
			imgAction.setImg_type(UserImgAction.TYPE_NORMAL);
			imgAction.updateToDb();
		} else if (ret_type == UserImgAction.TYPE_DEFAULT) {
			imgAction.setAction_step(UserImgAction.STEP_INFO);
			imgAction.setImg_type(UserImgAction.TYPE_DEFAULT);
			imgAction.updateToDb();
		} else {
			imgAction.setAction_step(UserImgAction.STEP_ERROR);
			imgAction.setImg_type(UserImgAction.TYPE_UNKNOWN);
			imgAction.updateToDb();
		}
		return true;
	}

	public byte[] getUrlImg(String urlname) {
		byte[] data = null;
		URL url = null;
		BufferedInputStream bis = null;
		URLConnection urlconn;
		try {
			url = new URL(urlname); // 创建URL
			urlconn = url.openConnection();
			urlconn.connect();
		} catch (Exception e) {
			e.printStackTrace();
			Log.log("connect rejected  " + urlname + "    wait......");
			Sleep(60000L);
			return null;
		}

		try {

			HttpURLConnection httpconn = (HttpURLConnection) urlconn;
			int HttpResult = httpconn.getResponseCode();
			if (HttpResult != HttpURLConnection.HTTP_OK) {
				return null;
			}
			int filesize = urlconn.getContentLength();
			data = new byte[filesize];
			bis = new BufferedInputStream(urlconn.getInputStream());
			int readed = 0;
			while (readed < filesize) {
				readed += bis.read(data, readed, filesize - readed);
			}

			return data;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log("fail :  " + urlname);
			Sleep(2000L);
			return null;
		} finally {
			closeConnection(bis);
		}
	}

	public int getAndSaveToDb(long user_id, String user_name,
			String urlname_30, String urlname_50, String urlname_180) {
		long t0 = 0L, t1 = 0L, t2 = 0L;
		t0 = System.currentTimeMillis();
		byte[] data_180 = getUrlImg(urlname_180);
		if (data_180 == null) {
			// try again once
			data_180 = getUrlImg(urlname_180);
		}

		if (data_180 == null) {
			Log.log("Fail   user_id=" + user_id);
			return UserImgAction.TYPE_UNKNOWN;
		}
		t1 = System.currentTimeMillis();
		int class_id = UserImgClass.getClassId(all_class, data_180);
		if (class_id != -1) {
			UserImgSame one = new UserImgSame();
			one.setClass_id(1);
			one.setUrl(urlname_50);
			one.setUser_id(user_id);
			one.setUser_name(user_name);
			one.setClass_id(class_id);
			one.setUpdate_time(new Date());
			one.inserIntoDb();
			t2 = System.currentTimeMillis();
			Log.log("UserSameimg  user_id=" + user_id + "  url_get="
					+ (t1 - t0) + " toDb=" + (t2 - t1));
			return UserImgAction.TYPE_DEFAULT;
		}
		if (saveImgToDb("user_img", user_id, user_name, urlname_50, data_180)) {
			t2 = System.currentTimeMillis();
			Log.log("UserImg      user_id=" + user_id + "  url_get="
					+ (t1 - t0) + " toDb=" + (t2 - t1));
			return UserImgAction.TYPE_NORMAL;
		} else {
			t2 = System.currentTimeMillis();
			Log.log("Fail       user_id=" + user_id + "  url_get=" + (t1 - t0)
					+ " toDb=" + (t2 - t1));
			return UserImgAction.TYPE_UNKNOWN;
		}
	}

	public void saveImgClassToDb(long user_id) {
		UserImgAction imgAction = UserImgAction.getByUserId(user_id);
		String urlname_50 = imgAction.getUrl();
		String urlname_30 = urlname_50.replaceAll("/50/", "/30/");
		String urlname_180 = urlname_50.replaceAll("/50/", "/180/");

		byte[] data_180 = getUrlImg(urlname_180);
		saveImgToDb("user_img_class", user_id, imgAction.getUser_name(),
				imgAction.getUrl(), data_180);
	}

	public boolean saveImgToDb(String table_name, long user_id,
			String user_name, String url, byte data_180[]) {
		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			Class.forName(Env.db_driver).newInstance();
			con = DriverManager.getConnection(Env.db_url, Env.db_user,
					Env.db_pwd);
			String sql = "INSERT INTO " + table_name
					+ " (user_id,user_name,url,img180) VALUES (  " + user_id
					+ ", '" + user_name + "' ," + "'" + url + "',?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setBytes(1, data_180);
			pstmt.execute();
			pstmt.close();
			pstmt = null;
			con.close();
			con = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection(con);
			closeConnection(pstmt);
		}

	}

	public void blobRead(String outfile, int picID) throws Exception {
		FileOutputStream fos = null;
		InputStream is = null;
		byte[] Buffer = new byte[4096];
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		File file = null;
		try {
			Class.forName(Env.db_driver).newInstance();
			conn = DriverManager.getConnection(Env.db_url, Env.db_user,
					Env.db_pwd);
			pstmt = conn
					.prepareStatement("select user_id,	img180 from user_img where id=?");
			pstmt.setInt(1, picID); // 传入要取的图片的ID
			rs = pstmt.executeQuery();
			rs.next();

			file = new File(outfile);
			if (!file.exists()) {
				file.createNewFile(); // 如果文件不存在，则创建
			}
			fos = new FileOutputStream(file);
			is = rs.getBinaryStream("img180");
			int size = 0;
			while ((size = is.read(Buffer)) != -1) {
				fos.write(Buffer, 0, size);
			}

		} catch (Exception e) {
			System.out.println("[OutPutFile error : ]" + e.getMessage());
		} finally {
			// 关闭用到的资源
			fos.close();
			rs.close();
			closeConnection(conn);
			closeConnection(pstmt);
		}
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

	public static void closeConnection(PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
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

}
