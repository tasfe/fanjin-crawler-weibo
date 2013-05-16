package ort.firephone.SinaUtils.inf;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.UserOauth;
import ort.firephone.SinaUtils.db.UserOauthFile;
import ort.firephone.SinaUtils.db.UserOauthTiming;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class RollUserOauthTiming {
	public static void main(String argv[]) {
		Env.initial();
		RollUserOauthTiming rollPostTiming = new RollUserOauthTiming();
		while (true) {
			rollPostTiming.try_post();
			sleep(Env.roll_gap_sec * 1000L);
		}
	}

	public void try_post() {
		try {
			Vector<UserOauthTiming> post_expected = UserOauthTiming
					.getAllCurrentExpected();
			for (int i = 0; i < post_expected.size(); i++) {
				UserOauthTiming onePost = post_expected.get(i);
				Vector<UserOauth> userOauths = UserOauth.getByUsername(1,
						onePost.getUsername());
				for (int j = 0; j < userOauths.size(); j++) {
					UserOauth fm_one = userOauths.get(j);
					try {
						if (onePost.getImage().equals("")) {
							getWeibo(fm_one).updateStatus(onePost.getText());
						} else {
							String filePath = getFilePath(Integer
									.parseInt(onePost.getImage()));
							File f = new File(filePath);
							getWeibo(fm_one).uploadStatus(onePost.getText(), f);
						}
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				onePost.setPost_time(new Date());
				onePost.setIs_posted(1);
				onePost.saveStatusToDb();
				Log.log(com.skyzoo.Jutil.Sys.getTimeString(null)
						+ " try_post-------post" + onePost.getText());
			}
			Log.log(com.skyzoo.Jutil.Sys.getTimeString(null) + " try_post----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFilePath(int file_id) {
		String filePath = "";
		UserOauthFile file = UserOauthFile.getById(file_id);
		filePath = Env.web_root + "uploads\\"+file.getPath() + "\\" + file.getFileName();
		return filePath;

	}

	private static Weibo getWeibo(UserOauth fc) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fc.getApp_key(), fc.getApp_secret());
		weibo.setToken(fc.getOauth_token_key(), fc.getOauth_token_secret());
		return weibo;
	}

}
