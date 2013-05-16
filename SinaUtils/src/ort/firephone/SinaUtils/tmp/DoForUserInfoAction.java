package ort.firephone.SinaUtils.tmp;

import java.util.Hashtable;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.RunParam;
import ort.firephone.SinaUtils.db.UserIdAction;
import ort.firephone.SinaUtils.db.UserInfoAction;

public class DoForUserInfoAction {

	public static void main(String argv[]) {
		Env.initial();
		UserToUserIdAction2();
	}

	public static void UserToUserInfoAction() {
		int max = 10000;
		String sql_b = "select id from user ";
		for (int i = 0;; i++) {
			try {
				String sql = sql_b + " limit " + (max * i) + ", " + max;
				Vector<String> vls = Env.getDb().queryListString(sql);
				if (vls.size() == 0) {
					Log.log("finished ... ...   ");
					break;
				}
				UserInfoAction.tryInsertToDb(vls, UserInfoAction.TYPE_NORMAL,
						UserInfoAction.STEP_RESERVE);
				Log.log("done ---");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void UserToUserIdAction2() {
		int max = 2000000;
		String sql_b = "select id ,followers_count , statuses_count from user_1 ";
		
		RunParam info_num_param = RunParam
				.getByName(RunParam.USERINFO_ACTION_NUM);
		int base_offset = info_num_param.getValue_int();
		long t1=0L,t2=0L,t3=0L;
		for (int i = 0;; i++) {
			try {
				String sql = sql_b + " limit " + (base_offset + max * i) + ", "
						+ max;
				System.out.println(sql);
				
				t1=System.currentTimeMillis()/1000L;
				System.out.println("last get="+(t2-t3) + " last insert="+(t1-t2));
				Vector<Hashtable> vhs = Env.getDb().query(sql);
				t2=System.currentTimeMillis()/1000L;
				t3=t1;
				if (vhs.size() == 0) {
					Log.log("finished ... ...   ");
					return;
				}
				Vector<String> one = new Vector<String>();
				for (int j = 0; j < vhs.size(); j++) {
					Hashtable ht = vhs.get(j);
					int followers_count = Integer.parseInt(ht.get(
							"followers_count").toString());
					int statuses_count = Integer.parseInt(ht.get(
							"statuses_count").toString());
					long id = Long.parseLong(ht.get("id").toString());

					if (id > 1000 && followers_count > 100
							&& statuses_count > 80) {
						one.add("" + id);
					}
					if (one.size() == 100) {
						int num = UserIdAction.tryInsertToDb(one,
								UserIdAction.TYPE_NORMAL,
								UserIdAction.STEP_RESERVE);
						Log.log("done " + (base_offset + max * i) + "--- num="
								+ num);
						one.clear();
					}
				}
				int num = UserIdAction.tryInsertToDb(one,
						UserIdAction.TYPE_NORMAL, UserIdAction.STEP_RESERVE);
				Log.log("done " + (base_offset + max * i) + "--- num=" + num);
				one.clear();
				info_num_param.setValue_int(base_offset + max * i + max);
				info_num_param.updateToDb();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void UserToUserIdAction() {
		int max = 20000;
		String sql_b = "select id from user  where followers_count>100 and statuses_count>80 and id > 10000 ";
		RunParam info_num_param = RunParam
				.getByName(RunParam.USERINFO_ACTION_NUM);
		int base_offset = info_num_param.getValue_int();
		for (int i = 0;; i++) {
			try {
				Log.log("started -- " + (base_offset + max * i));
				String sql = sql_b + " limit " + (base_offset + max * i) + ", "
						+ max;
				Vector<String> vls = Env.getDb().queryListString(sql);
				if (vls.size() == 0) {
					Log.log("finished ... ...   ");
					return;
				}
				for (int j = 0; j < vls.size() / 100; j++) {
					Vector<String> one = getOffset(vls, j * 100, j * 100 + 100);
					int num = UserIdAction
							.tryInsertToDb(one, UserIdAction.TYPE_NORMAL,
									UserIdAction.STEP_RESERVE);

					Log.log(com.skyzoo.Jutil.Sys.getTimeString(null)
							+ "  done " + (base_offset + max * i + j * 100)
							+ "--- num=" + num);
				}
				info_num_param.setValue_int(base_offset + max * i + max);
				info_num_param.updateToDb();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Vector<String> getOffset(Vector<String> all, int start,
			int max) {
		Vector<String> x = new Vector<String>();
		for (int i = start; i < start + max; i++) {
			if (i < all.size()) {
				x.add(all.get(i));
			}
		}
		return x;

	}

	public static void UserOmitToUserInfoAction() {
		int max = 10;
		String sql_b = "select sina_id as id from user_omit ";
		for (int i = 0;; i++) {
			try {
				String sql = sql_b + " limit " + (max * i) + ", " + max;
				// Log.log(sql);
				Vector<Long> vls = Env.getDb().queryListLong(sql);
				Vector<String> vss = new Vector<String>();
				for (int j = 0; j < vls.size(); j++) {
					vss.add(vls.get(j) + "");
				}
				if (vls.size() == 0) {
					Log.log("finished ... ...   ");
					break;
				}
				UserInfoAction.insertToDb(vss, UserInfoAction.TYPE_CORPSE,
						UserInfoAction.STEP_STATUS);
				if (i % 100 == 0)
					Log.log("done ---" + (i * max));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
