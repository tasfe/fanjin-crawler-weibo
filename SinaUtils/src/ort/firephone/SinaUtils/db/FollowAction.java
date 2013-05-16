package ort.firephone.SinaUtils.db;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.skyzoo.Jutil.DBConnections;

public class FollowAction extends Fdb {
	public static String default_table_name = "follow_action";

	public final static int OAUTH_TYPE_SINA = 1;

	/**
	 * 总是保证关注此类型用户
	 */
	public final static int ACTION_TYPE_FRIEND = 101;
	/**
	 * 期望粉的用户
	 */
	public final static int ACTION_TYPE_FOLLOWME = 201;
	/**
	 * 虽在队列上，但经分析，暂时认为放弃。
	 */
	public final static int ACTION_TYPE_NOT_FOLLOWME = 202;
	/**
	 * 黑名单，只是多次未成功的
	 */
	public final static int ACTION_TYPE_BLOCK_NOTFOLLOW = 301;
	/**
	 * 黑名单，骗子
	 */
	public final static int ACTION_TYPE_BLOCK_CHEATER = 302;
	/**
	 * 黑名单，恐怖分子，小心处理
	 */
	public final static int ACTION_TYPE_BLOCK_ANYWAY = 303;
	/**
	 * 黑名单，被黑名单了
	 */
	public final static int ACTION_TYPE_BLOCK_BLOCKME = 304;
	/**
	 * 黑名单,用名已删除
	 */
	public final static int ACTION_TYPE_BLOCK_DELETED = 305;
	/**
	 * 黑名单,不名原因
	 */
	public final static int ACTION_TYPE_BLOCK_UNKNOWN = 306;

	/**
	 * 未开始做操作,预备中
	 */
	public final static int ACTION_STEP_RESERVE = 0;
	/**
	 * 正在关注此用户，等待其FOLLOWME中
	 */
	public final static int ACTION_STEP_FOLLOWING = 1;
	/**
	 * 已经被此用户关注
	 */
	public final static int ACTION_STEP_FOLLOWED = 2;
	/**
	 * 已处理过，未成功
	 */
	public final static int ACTION_STEP_FOLLOW_FAIL = 3;

	/**
	 * 被主动骗粉过的
	 */
	public final static int ACTION_STEP_FOLLOW_CHEATED = 4;

	private String source_oauth_id;
	private int oauth_type;
	private String oauth_id;
	private String oauth_name;
	private String action_desc;
	private int action_type;
	private int action_step;
	private int at_step;
	private int action_num;
	private Date action_time;

	final public static String sql_select = "source_oauth_id,oauth_type,oauth_id,oauth_name,action_desc,action_type,action_step,at_step,action_num,UNIX_TIMESTAMP(action_time) as action_time";

	public static boolean isExist(String oauth_id) {
		String sql = "select oauth_id from " + default_table_name + "  where "
				+ item("oauth_id", oauth_id);

		Vector<Hashtable> vh = getDb().query(sql);
		if (vh.size() == 1) {
			return true;
		}
		return false;
	}

	public static Vector<Long> getAllId() {
		Vector<Long> all_ids = new Vector<Long>();
		String sql = "select oauth_id from " + default_table_name;
		all_ids = getDb().queryListLong(sql);
		return all_ids;
	}

	public static Vector<FollowAction> getByAtStep(String source_oauth_id,
			int at_step, int max) {
		Vector<FollowAction> all = new Vector<FollowAction>();
		String sql = " select " + sql_select + " from " + default_table_name
				+ " where ";
		sql += item("source_oauth_id", source_oauth_id) + " and ";
		sql += item("at_step", at_step) + " and ";
		sql += item("action_type", ACTION_TYPE_FOLLOWME) + " ";
		sql += " limit " + max;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			FollowAction one = new FollowAction();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public static Vector<FollowAction> getByTypeAndStep(String source_oauth_id,
			int action_type, int action_step, int max) {
		Vector<FollowAction> all = new Vector<FollowAction>();
		String sql = " select " + sql_select + " from " + default_table_name
				+ " where ";
		sql += item("source_oauth_id", source_oauth_id) + " and ";
		sql += item("action_type", action_type) + " and ";
		sql += item("action_step", action_step) + " ";
		sql += " limit " + max;

		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			FollowAction one = new FollowAction();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public static Vector<FollowAction> getFollowReserve(String source_oauth_id,
			int max) {
		Vector<FollowAction> all = new Vector<FollowAction>();
		String sql = " select " + sql_select + " from " + default_table_name
				+ " where ";
		sql += item("source_oauth_id", source_oauth_id) + " and ";
		sql += item("action_type", FollowAction.ACTION_TYPE_FOLLOWME) + " and ";
		sql += item("action_step", FollowAction.ACTION_STEP_RESERVE) + " ";
		sql += " limit " + max;
		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			FollowAction one = new FollowAction();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public static Vector<FollowAction> getFollowingBySec(
			String source_oauth_id, int sec, int max) {
		Vector<FollowAction> all = new Vector<FollowAction>();
		Long time_l = System.currentTimeMillis() - sec * 1000L;
		String sql = " select " + sql_select + " from " + default_table_name
				+ " where ";
		sql += item("source_oauth_id", source_oauth_id) + " and ";
		sql += item("action_type", ACTION_TYPE_FOLLOWME) + " and ";
		sql += item("action_step", ACTION_STEP_FOLLOWING) + " and ";
		sql += item_less("action_time", new Date(time_l)) + "  ";
		sql += " limit " + max;

		Vector<Hashtable> vh = getDb().query(sql);
		for (int i = 0; i < vh.size(); i++) {
			FollowAction one = new FollowAction();
			one.fromHash(vh.get(i));
			all.add(one);
		}
		return all;
	}

	public void saveToDb() {
		saveToDb(default_table_name);
	}

	public void saveToDb(String this_table_name) {
		String sql = " update `" + this_table_name + "`  set ";
		sql += item("oauth_type", oauth_type) + " , ";
		sql += item("oauth_name", oauth_name) + " , ";
		sql += item("action_desc", action_desc) + " , ";
		sql += item("action_type", action_type) + " , ";
		sql += item("action_step", action_step) + " , ";
		sql += item("action_num", action_num) + " , ";
		sql += item("at_step", at_step) + " , ";
		sql += item("action_time", action_time) + "  where ";
		sql += item("source_oauth_id", source_oauth_id) + " and ";
		sql += item("oauth_id", oauth_id);
		getDb().executequery(sql);
	}

	public void insertToDb(String this_table_name) {
		String sql = " insert `" + this_table_name + "`  set ";
		sql += item("oauth_type", oauth_type) + " , ";
		sql += item("oauth_name", oauth_name) + " , ";
		sql += item("action_desc", action_desc) + " , ";
		sql += item("action_type", action_type) + " , ";
		sql += item("action_step", action_step) + " , ";
		sql += item("action_num", action_num) + " , ";
		sql += item("at_step", at_step) + " , ";
		sql += item("action_time", action_time) + " , ";
		sql += item("source_oauth_id", source_oauth_id) + " , ";
		sql += item("oauth_id", oauth_id);
		getDb().executequery(sql);
	}

	public void fromHash(Hashtable<String, String> ht) {
		source_oauth_id = ht.get("source_oauth_id");
		oauth_type = Integer.parseInt(ht.get("oauth_type"));
		oauth_id = ht.get("oauth_id");
		oauth_name = ht.get("oauth_name");
		action_desc = ht.get("action_desc");
		action_type = Integer.parseInt(ht.get("action_type"));
		action_step = Integer.parseInt(ht.get("action_step"));
		at_step = Integer.parseInt(ht.get("at_step"));
		action_num = Integer.parseInt(ht.get("action_num"));
		String action_time_str = ht.get("action_time");
		action_time = new Date();
		action_time.setTime(Long.parseLong(action_time_str + "000"));

	}

	public int getAt_step() {
		return at_step;
	}

	public void setAt_step(int at_step) {
		this.at_step = at_step;
	}

	public int getOauth_type() {
		return oauth_type;
	}

	public void setOauth_type(int oauthType) {
		oauth_type = oauthType;
	}

	public String getOauth_id() {
		return oauth_id;
	}

	public void setOauth_id(String oauthId) {
		oauth_id = oauthId;
	}

	public String getOauth_name() {
		return oauth_name;
	}

	public void setOauth_name(String oauthName) {
		oauth_name = oauthName;
	}

	public String getSource_oauth_id() {
		return source_oauth_id;
	}

	public void setSource_oauth_id(String source_oauth_id) {
		this.source_oauth_id = source_oauth_id;
	}

	public String getAction_desc() {
		return action_desc;
	}

	public void setAction_desc(String action_desc) {
		this.action_desc = action_desc;
	}

	public int getAction_type() {
		return action_type;
	}

	public void setAction_type(int actionType) {
		action_type = actionType;
	}

	public int getAction_step() {
		return action_step;
	}

	public void setAction_step(int actionStep) {
		action_step = actionStep;
	}

	public int getAction_num() {
		return action_num;
	}

	public void setAction_num(int actionNum) {
		action_num = actionNum;
	}

	public Date getAction_time() {
		return action_time;
	}

	public void setAction_time(Date actionTime) {
		action_time = actionTime;
	}

}
