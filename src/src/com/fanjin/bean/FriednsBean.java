package com.fanjin.bean;
/**
 * 
 * @author 砍柴的大叔
 * 关注好友的信息
 *
 */
public class FriednsBean {
	public String weiboUserID;	//用户的id
	public String weiboFriendsID;	//好友的id
	
	public String getWeiboUserID() {
		return weiboUserID;
	}

	public void setWeiboUserID(String weiboUserID) {
		this.weiboUserID = weiboUserID;
	}

	public String getWeiboFriendsID() {
		return weiboFriendsID;
	}

	public void setWeiboFriendsID(String weiboFriendsID) {
		this.weiboFriendsID = weiboFriendsID;
	}

	public String toString(){
		return weiboUserID+","
			   +weiboFriendsID;
	}
}
