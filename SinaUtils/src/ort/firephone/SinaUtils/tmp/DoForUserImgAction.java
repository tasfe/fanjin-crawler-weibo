package ort.firephone.SinaUtils.tmp;

import java.util.Hashtable;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.RunParam;
import ort.firephone.SinaUtils.db.UserIdAction;
import ort.firephone.SinaUtils.db.UserImgAction;

public class DoForUserImgAction {

	public static void main(String argv[]) {
		Env.initial();
		UserToUserImgAction();
	}

	public static void UserToUserImgAction() {
		int max = 50000;
		String sql_b = "select id ,screen_name,profile_image_url from user_1 ";
		RunParam img_num_param = RunParam
				.getByName(RunParam.USERIMG_ACTION_NUM);
		int base_offset = img_num_param.getValue_int();
		Vector<UserImgAction> userImgActions = new Vector<UserImgAction>();
		long t0 = 0L, t1 = 0L, t2 = 0L, t3 = 0L, tt0 = 0L, tt1 = 0L;
		for (int i = 0;; i++) {
			try {
				String sql = sql_b + " where raw_id >= "
						+ (base_offset + max * i) + "  limit " + max;
				t0 = System.currentTimeMillis();
				Vector<Hashtable> vhs = Env.getDb().query(sql);
				t1 = System.currentTimeMillis();
				int num = 0;

				if (vhs.size() == 0) {
					Log.log("finished ... ...   ");
					return;
				}
				Log.log("roll start....");
				t2 = System.currentTimeMillis();
				for (int j = 0; j < vhs.size(); j++) {
					Hashtable ht = vhs.get(j);
					String screen_name = ht.get("screen_name").toString();
					String profile_image_url = ht.get("profile_image_url")
							.toString();
					long user_id = Long.parseLong(ht.get("id").toString());
					UserImgAction userImgAction = new UserImgAction();
					userImgAction.setUser_id(user_id);
					userImgAction.setUser_name(screen_name);
					userImgAction.setUrl(profile_image_url);
					userImgAction.setAction_step(UserImgAction.STEP_RESERVE);
					//userImgAction.inserIntoDb();
					userImgActions.add(userImgAction);
					num++;
					if (num % 400 == 0) {
						tt0 = System.currentTimeMillis();
						UserImgAction.inserIntoDb(userImgActions);
						userImgActions.clear();
						tt1 = System.currentTimeMillis();
						Log.log("UserToUserImgAction: base_offset="
								+ (base_offset + max * i) + " num=" + num
								+ " t=" + (tt1 - tt0));
					}
				}
				UserImgAction.inserIntoDb(userImgActions);
				userImgActions.clear();
				t3 = System.currentTimeMillis();

				img_num_param.setValue_int(base_offset + max * i + max);
				img_num_param.updateToDb();
				Log.log("get_t=" + (t1 - t0) + " save_t=" + (t3 - t2));
				Log.log("wait.. you can stop now");
				Thread.sleep(5 * 1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
