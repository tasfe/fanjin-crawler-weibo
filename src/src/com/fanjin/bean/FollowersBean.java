package com.fanjin.bean;
/**
 * 
 * @author ����Ĵ���
 * ��˿�б������Ϣ
 *
 */
public class FollowersBean {
	public String weiboUserID;         //�û�ID
	public String weiboFollowersID;//��˿ID	
	
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
