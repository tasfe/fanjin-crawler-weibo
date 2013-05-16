package ort.firephone.SinaUtils.inf;

import java.util.List;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Weibo;

public class GetOneUserAllStatus {

	public static void main(String argv[]) {
		Env.initial();
		GetOneUserAllStatus getOneUserAllStatus = new GetOneUserAllStatus();
		getOneUserAllStatus.rollone(1851524785L);
	}

	public void rollone(long user_id) {

		Fcorpse fc = FcorpseCache.getIns().getNextOne();
		Weibo w = getWeibo(fc);
		try {
			List<Status> statuses = w.getUserTimeline("" + user_id, new Paging(
					1, 200));
			Log.log("" + statuses.size());
			for (int i = 2; statuses.size() > 0; i++) {
				statuses = w.getUserTimeline("" + user_id, new Paging(i, 200));
				Log.log("" + statuses.size());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private static Weibo getWeibo(Fcorpse fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}
}
