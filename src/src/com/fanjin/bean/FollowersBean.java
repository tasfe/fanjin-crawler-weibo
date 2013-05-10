package com.fanjin.bean;
/**
 * 
 * @author 砍柴的大叔
 * 粉丝列表基本信息
 *
 */
public class FollowersBean {
	public String weiboUserID;         //用户ID
	public String weiboFollowersID;//粉丝ID	
	
	public String getWeiboUserID() {
		return weiboUserID;
	}

	public void setWeiboUserID(String weiboUserID) {
		this.weiboUserID = weiboUserID;
	}

	public String getWeiboFollowersID() {
		return weiboFollowersID;
	}

	public void setWeiboFollowersID(String weiboFollowersID) {
		this.weiboFollowersID = weiboFollowersID;
	}

	public String toString(){
		return weiboUserID+","
				+weiboFollowersID;
	}
}
