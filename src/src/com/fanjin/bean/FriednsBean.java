package com.fanjin.bean;
/**
 * 
 * @author ����Ĵ���
 * ��ע���ѵ���Ϣ
 *
 */
public class FriednsBean {
	public String weiboUserID;	//�û���id
	public String weiboFriendsID;	//���ѵ�id
	
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
