package org.firephone.SinaUtil.cache;

import java.util.Vector;

import ort.firephone.SinaUtils.db.Fcorpse;

public class FcorpseCache {
	Vector<Fcorpse> all_members = null;
	int current_pos = 0;
	private static FcorpseCache ins = null;

	public static synchronized FcorpseCache getIns() {
		if (ins == null) {
			ins = new FcorpseCache();
		}
		return ins;
	}

	public FcorpseCache() {
	}

	public synchronized String getAtStr(int num) {
		String at_str = "";
		for (int i = 0; i < num; i++) {
			int n_id = (current_pos + i + 1) % all_members.size();
			at_str += " //@" + all_members.get(n_id).getOauth_name() + ":";
		}
		return at_str;
	}

	/**
	 * 循环使用每个一僵尸
	 * 
	 * @return 返回一个僵尸
	 */
	public synchronized Fcorpse getNextOne() {
		if (all_members == null) {
			all_members = Fcorpse.getAllActive();
			current_pos = randomInt(all_members.size() - 1);
		}
		if (current_pos < all_members.size()) {
			return all_members.get(current_pos++);
		} else {
			current_pos = 0;
			all_members = Fcorpse.getAllActive();
			if (all_members.size() == 0) {
				return null;
			}
			return all_members.get(current_pos++);
		}
	}

	public int randomInt(int max) {
		java.util.Random random = new java.util.Random();
		int random_num = random.nextInt(max);
		return random_num;
	}
}