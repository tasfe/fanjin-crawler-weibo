package ort.firephone.SinaUtils.inf;

import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Ffavorite_user;
import ort.firephone.SinaUtils.db.Ffollowers;

import weibo4j.Weibo;
import weibo4j.WeiboException;

public class CreateFriendship extends Thread {
	String my_id = "1881808204";

	/**
	 * 获取用户粉丝对象uid列表
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Env.initial();
			CreateFriendship create_a = new CreateFriendship();
			create_a.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Vector<Fcorpse> fcorpses = Fcorpse.getAllActive();
		Vector<Long> all = Ffollowers.getFlowers(Long.parseLong(my_id), 100000);

		for (int i = 0; i < fcorpses.size(); i++) {
			Fcorpse fcorpse = fcorpses.get(i);

			String ret = doOnce(my_id, fcorpse);
			Log.log(Log.Severity_Informational, fcorpse.getSina_user() + "  "
					+ ret);
		}
	}

	public static String doOnce(String my_id, Fcorpse fcorpse) {
		String ret = "";
		try {
			getWeibo(fcorpse).createFriendship(my_id);

		} catch (WeiboException e) {
			e.printStackTrace();
		}

		return ret;
	}

	private static Weibo getWeibo(Fcorpse fcorpse) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fcorpse.getApp_key(), fcorpse.getApp_secret());
		weibo.setToken(fcorpse.getOauth_token(), fcorpse
				.getOauth_token_secret());
		return weibo;
	}
}
