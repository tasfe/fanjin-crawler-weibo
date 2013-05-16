package ort.firephone.SinaUtils.inf;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fmember;
import ort.firephone.SinaUtils.db.PostTiming;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * 
 * 对PostTiming表里的项目，定期发贴
 * 
 */
public class RollPostTiming {

	public static void main(String argv[]) {
		Env.initial();
		RollPostTiming rollPostTiming = new RollPostTiming();
		while (true) {
			rollPostTiming.try_post();
			sleep(Env.roll_gap_sec * 1000L);
		}
	}

	public void try_post() {
		try {
			Vector<PostTiming> post_expected = PostTiming
					.getAllCurrentExpected();
			for (int i = 0; i < post_expected.size(); i++) {
				PostTiming onePost = post_expected.get(i);
				Vector<Fmember> fms = Fmember.getAllActive();
				for (int j = 0; j < fms.size(); j++) {
					Fmember fm_one = fms.get(j);
					try {
						if (onePost.getImage().equals("")) {
							getWeibo(fm_one).updateStatus(onePost.getText());
						} else {
							File f = new File(onePost.getImage());
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

	private static Weibo getWeibo(Fcorpse fc) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fc.getApp_key(), fc.getApp_secret());
		weibo.setToken(fc.getOauth_token(), fc.getOauth_token_secret());
		return weibo;
	}

	private static Weibo getWeibo(Fmember fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}

}
