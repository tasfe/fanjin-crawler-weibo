package org.firephone.SinaUtil.err;

import ort.firephone.SinaUtils.Log;
import weibo4j.WeiboException;
import weibo4j.org.json.JSONException;

public class OauthErr {
	/**
	 * Oauth返回出错，建议重试一次类弄的错误，注意要变更僵尸
	 */
	final public static int TypeForRetry = 1;
	/**
	 * 此服务器IP请求超出限制，建议暂停一会
	 */
	final public static int TypeIpRateLimit = 2;
	/**
	 * 本次操作请求超出限制，建议暂停一会或暂停和本次相同的操作
	 */
	final public static int TypeActionRateLimit = 3;
	/**
	 * 目标不存在。
	 */
	final public static int TypeObjectNotExist = 4;
	/**
	 * 被黑名单,被拒绝
	 */
	final public static int TypeReject = 5;

	/**
	 * 未定义错误
	 */
	final public static int TypeUnknown = 11;

	public static int getErrType(WeiboException e) {
		try {
			String msg = e.getMessage();
			if (JSONException.class.isInstance(e.getCause())) {
				// 编码错误，重新取
				return TypeForRetry;
			} else if (msg
					.indexOf("401:Authentication credentials were missing") != -1) {
				// 认证出错，重新取
				return TypeForRetry;
			} else if (msg.indexOf("40107:Oauth Error: signature_invalid") != -1) {
				// 认证出错，重新取
				return TypeForRetry;
			} else if (

			msg.indexOf("40313:Error: invalid weibo user!") != -1) {
				// 用户不存在
				return TypeForRetry;
			} else if (msg.indexOf("40023:Error: User does not exists!") != -1) {
				// 所取用户不存在
				return TypeObjectNotExist;
			} else if (msg.indexOf("40028:fuid错误") != -1) {
				// fuid不存在
				return TypeObjectNotExist;
			} else if (msg.indexOf("40028:发评论太多啦，休息一会儿吧") != -1) {
				return TypeActionRateLimit;
			} else if (msg.indexOf("40028:请不要重复发送类同评论") != -1) {
				return TypeActionRateLimit;
			} else if (msg.indexOf("40028:请不要发布广告信息哦") != -1) {
				return TypeActionRateLimit;
			} else if (msg
					.indexOf("40304:Error: Social graph updates out of rate limit!") != -1) {
				return TypeActionRateLimit;
			} else if (msg.indexOf("你今天已经关注很多喽") != -1) {
				return TypeActionRateLimit;
			} else if (msg
					.indexOf("40312:Error: IP requests out of rate limit") != -1) {
				return TypeIpRateLimit;
			} else if (msg.indexOf("40028:根据对方的设置，你不能进行此操作") != -1) {
				// 黑名单
				return TypeReject;
			}
		} catch (Exception ee) {

		}
		// 还有很多出错问题。
		return TypeUnknown;

	}

	/**
	 * 在msg中是否含有key
	 * 
	 * @param msg
	 * @param key
	 * @return
	 */
	public boolean C(String msg, String key) {
		if (msg.indexOf(key) != -1) {
			return true;
		}
		return false;
	}

}
