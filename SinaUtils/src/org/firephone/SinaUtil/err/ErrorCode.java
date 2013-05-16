package org.firephone.SinaUtil.err;

public class ErrorCode {
	final public static int USER_NOT_EXIST = 40023;
	final public static int UNKNOWN = -1;

	public static int getErrorCode(Exception e) {
		String msg = e.getMessage();
		if (msg.contains("40023:Error: User does not exists!")) {
			return USER_NOT_EXIST;
		}
		return UNKNOWN;
	}
	
	

}
