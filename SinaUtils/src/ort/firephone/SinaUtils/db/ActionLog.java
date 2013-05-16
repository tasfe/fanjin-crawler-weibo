package ort.firephone.SinaUtils.db;

import java.util.Date;

import com.skyzoo.Jutil.DBConnections;

public class ActionLog extends Fdb {
	public static String table_name = "action_log";

	final public static int TYPE_REPOSE_TO_FAV = 10;
	final public static int TYPE_COMMENT_TO_FAV = 11;
	final public static int TYPE_UPLOAD_OF_MEM = 12;

	final public static int TYPE_REPOSE_TO_MEM = 20;
	final public static int TYPE_COMMENT_TO_MEM = 21;

	final public static int TYPE_STATUS_REPOSE_AND_AT = 30;

	/**
	 * TYPE_FOLLOWME_DOFOLLOW： 执行follow特定用户
	 */
	final public static int TYPE_FOLLOWME_DOFOLLOW = 41;
	/**
	 * TYPE_FOLLOWME_DOFOLLOW: 执行去除follow特定用户
	 */
	final public static int TYPE_FOLLOWME_UNFOLLOW = 42;
	/**
	 * TYPE_FOLLOWME_DOFOLLOWED: 处理以前互粉的
	 */
	final public static int TYPE_FOLLOWME_DOFOLLOWED = 43;
	/**
	 * TYPE_FOLLOWME_DOCHEATED: 处理以前互粉的
	 */
	final public static int TYPE_FOLLOWME_DOCHEATED = 44;
	final public static int STATUS_SUCCESSED = 101;
	final public static int STATUS_FAIL = 102;
	final public static int STATUS_UNKNOWN = 103;

	int id;
	String text;
	long src_id;
	long dest_id;
	Date time;
	int status;
	int type;

	public static String getTypeMsg(int type) {
		if (type == TYPE_REPOSE_TO_FAV) {
			return "转发关注的";
		} else if (type == TYPE_COMMENT_TO_FAV) {
			return "评论关注的";
		} else if (type == TYPE_UPLOAD_OF_MEM) {
			return "上传图片";
		} else if (type == TYPE_REPOSE_TO_MEM) {
			return "转发用户的";
		} else if (type == TYPE_COMMENT_TO_MEM) {
			return "评论用户的";
		} else if (type == TYPE_STATUS_REPOSE_AND_AT) {
			return "转发特定帖";
		} else if (type == TYPE_FOLLOWME_DOFOLLOW) {
			return "执行follow特定用户";
		} else if (type == TYPE_FOLLOWME_UNFOLLOW) {
			return "执行去除follow特定用户";
		} else if (type == TYPE_FOLLOWME_DOFOLLOWED) {
			return "处理以前互粉的";
		}

		return "unknown type";
	}

	public ActionLog() {

	}

	public ActionLog(long src_id, long dest_id, String text, int type,
			int status) {
		this.src_id = src_id;
		this.dest_id = dest_id;
		this.text = text;
		this.type = type;
		this.status = status;
		this.time = new Date();
	}

	public void insertToDb() {

		String sql = "insert into " + table_name + " set ";
		sql += item("text", text) + " , ";
		sql += item("src_id", src_id) + " , ";
		sql += item("dest_id", dest_id) + " , ";
		sql += item("status", status) + " , ";
		sql += item("time", time) + " , ";
		sql += item("type", type) + " ";
		getDb().executequery(sql);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addText(String text) {
		this.text += text;
	}

	public long getSrc_id() {
		return src_id;
	}

	public void setSrc_id(long srcId) {
		src_id = srcId;
	}

	public long getDest_id() {
		return dest_id;
	}

	public void setDest_id(long destId) {
		dest_id = destId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
