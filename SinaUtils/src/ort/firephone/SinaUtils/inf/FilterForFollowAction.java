package ort.firephone.SinaUtils.inf;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.FSuser;
import ort.firephone.SinaUtils.db.FollowAction;
import ort.firephone.SinaUtils.db.RunParam;

public class FilterForFollowAction {
	final public static String KEY_suser_num = "suser_num";
	int suser_num = 1;
	int once_max = 2000;

	public static void main(String argv[]) {
		Env.initial();
		FilterForFollowAction filterForFollowAction = new FilterForFollowAction();
		filterForFollowAction.roll_once();
	}

	public void roll_once() {
		Vector<FSuser> fsusers = new Vector<FSuser>();
		RunParam suser_num_param = RunParam.getByName(KEY_suser_num);
		suser_num = suser_num_param.getValue_int();
		fsusers = FSuser.getAllLimited(suser_num, once_max);
		suser_num_param.setValue_int(suser_num + once_max);
		suser_num_param.updateToDb();
		Vector<Long> all_ids = FollowAction.getAllId();
		Hashtable<Long, Integer> all_ids_h = new Hashtable<Long, Integer>();
		for (int i = 0; i < all_ids.size(); i++) {
			all_ids_h.put(all_ids.get(i), 1);
		}

		String table_name_base = "follow_action";
		while (fsusers.size() > 0) {
			Log.log("start == " + fsusers.size());
			int once_num = 0;
			for (int i = 0; i < fsusers.size(); i++) {
				try {
					String table_name = table_name_base + ((i % 3) + 1);
					FSuser one = fsusers.get(i);
					if (all_ids_h.containsKey(one.getId())) {
						continue;
					}
					/*
					 * if (FollowAction.isExist("" + one.getId())) { continue; }
					 */
					FollowAction followAction = new FollowAction();
					followAction.setAction_desc("");
					followAction.setAction_num(0);
					followAction
							.setAction_step(FollowAction.ACTION_STEP_RESERVE);
					followAction.setAction_time(new Date());
					followAction
							.setAction_type(FollowAction.ACTION_TYPE_FOLLOWME);
					followAction.setAt_step(0);
					followAction.setOauth_id("" + one.getId());
					followAction.setOauth_name(one.getScreen_name());
					followAction.setOauth_type(FollowAction.OAUTH_TYPE_SINA);
					followAction.setSource_oauth_id("" + 1);
					followAction.insertToDb(table_name);
					once_num++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Log.log("finished == " + once_num);
			suser_num = suser_num_param.getValue_int();
			fsusers = FSuser.getAllLimited(suser_num, once_max);
			suser_num_param.setValue_int(suser_num + once_max);
			suser_num_param.updateToDb();
		}
	}

	public void once() {
		Vector<FSuser> fsusers = new Vector<FSuser>();
		fsusers = FSuser.getAllLimited(0, 100);
		for (int i = 0; i < fsusers.size(); i++) {
			Log.log(fsusers.get(i).getName());
		}
	}
}
