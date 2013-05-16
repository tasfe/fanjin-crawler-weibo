package ort.firephone.SinaUtils.inf;

import java.util.Hashtable;
import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Ffavorite_user;
import ort.firephone.SinaUtils.db.Ffollowers;
import ort.firephone.SinaUtils.db.Ffriends;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class GetFollowerIds extends Thread {

	/**
	 * 获取用户粉丝对象uid列表
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Env.initial();
			GetFollowerIds getFollowerIds = new GetFollowerIds();
			getFollowerIds.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Vector<Ffavorite_user> f_u = Ffavorite_user.getAll();
		for (int i = 0; i < f_u.size(); i++) {
			Fcorpse fc = FcorpseCache.getIns().getNextOne();
			String ret = doFriendsOnce(f_u.get(i).getSina_id(), fc);
			Log.log(Log.Severity_Informational, f_u.get(i).getName() + "  "
					+ ret);
			fc = FcorpseCache.getIns().getNextOne();
			ret = doFollowsOnce(f_u.get(i).getSina_id(), fc);
			Log.log(Log.Severity_Informational, f_u.get(i).getName() + "  "
					+ ret);
		}
		Log.log("GetFollowerIds end........");
	}

	public static String doFollowsOnce(long source_id, Fcorpse fm) {
		String ret = "";
		int addNum = 0;
		Vector<Long> v_this = getFollowerIds(source_id, fm);
		ret += "get by (" + fm.getSina_user() + ")from weibo.com(" + source_id
				+ ") count=" + v_this.size() + "\n";
		addNum = Ffollowers.tryAddToDb(source_id, v_this);
		ret += "new_id count=" + addNum + "\n";
		return ret;
	}

	public static String doFriendsOnce(long source_id, Fcorpse fm) {
		String ret = "";
		int addNum = 0;
		Vector<Long> v_this = getFriendsIds(source_id, fm);
		ret += "get by (" + fm.getSina_user() + ")from weibo.com(" + source_id
				+ ") count=" + v_this.size() + "\n";
		addNum = Ffriends.tryAddToDb(source_id, v_this);
		ret += "new_id count=" + addNum + "\n";
		return ret;
	}

	public static Vector<Long> getFollowerIds(long source_id, Fcorpse fm) {
		Vector<Long> follower_ids = new Vector<Long>();
		try {
			long[] ids = getWeibo(fm).getFollowersIDs((int) source_id, 0)
					.getIDs();
			long[] ids2 = new long[0];
			if (ids.length == 5000) {
				ids2 = getWeibo(fm).getFollowersIDs((int) source_id, 4999)
						.getIDs();
			}
			for (int i = 0; i < ids.length; i++) {
				follower_ids.add(new Long(ids[i]));
			}
			for (int i = 0; i < ids2.length; i++) {
				follower_ids.add(new Long(ids2[i]));
			}

		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return follower_ids;
	}

	public static Vector<Long> getFriendsIds(long source_id, Fcorpse fm) {
		Vector<Long> friend_ids = new Vector<Long>();
		try {
			long[] ids = getWeibo(fm).getFriendsIDs((int) source_id, 0)
					.getIDs();
			long[] ids2 = new long[0];
			if (ids.length == 5000) {
				ids2 = getWeibo(fm).getFriendsIDs((int) source_id, 4999)
						.getIDs();
			}
			for (int i = 0; i < ids.length; i++) {
				friend_ids.add(new Long(ids[i]));
			}
			for (int i = 0; i < ids2.length; i++) {
				friend_ids.add(new Long(ids2[i]));
			}

		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return friend_ids;
	}

	private static Weibo getWeibo(Fcorpse fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}
}
