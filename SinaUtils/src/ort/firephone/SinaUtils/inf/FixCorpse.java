package ort.firephone.SinaUtils.inf;

import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fmember;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class FixCorpse extends Thread {
	public static void main(String argv[]) {
		Env.initial();
		FixCorpse fixCorpse = new FixCorpse();
		fixCorpse.run();
	}

	public void run() {
		Vector<Fcorpse> vfc = Fcorpse.getAllActive();
		Fcorpse corpse_one = null;
		for (int i = 0; i < vfc.size(); i++) {
			try {
				corpse_one = vfc.get(i);
				User one = getWeibo(corpse_one).verifyCredentials();
				corpse_one.setOauth_name(one.getName());
				corpse_one.setSina_id(one.getId());
				corpse_one.insertOrUpdateToDB();
				System.out.println(one.getId() + " " + one.getName() + " "
						+ corpse_one.getSina_user() + " "
						+ corpse_one.getSina_pass());

			} catch (WeiboException e) {
				processError(corpse_one, e);
				// e.printStackTrace();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void processError(Fcorpse corpse, WeiboException e) {
		System.out.println("***********************");

		if (e.getMessage().indexOf("40313:Error: invalid weibo user!") != -1) {
			System.out.println("corpse error  :" + corpse.getSina_user()
					+ "  :" + corpse.getSina_pass());
			corpse.setActive(0);
			corpse.insertOrUpdateToDB();
		} else {
			e.printStackTrace();
		}
		// System.out.println("***********************");
		// System.out.println(e.getMessage());
		// System.out.println(e.getStatusCode());

	}

	private static Weibo getWeibo(Fcorpse fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}

	private static Weibo getWeibo(Fmember fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}
}
