package com.fanjin.bean;

public class StatusBean {
	public String   weiboUserID;        //�û�id
	public String   weiboID ;           //΢��id
	public long     weiboTranimitsCount; //ת������
	public long     weiboCommentsCount;	 //���۴���
	public String   weiboContent;        //΢����ͬ
	
	public String getWeiboUserID() {
		return weiboUserID;
	}

	public void setWeiboUserID(String weiboUserID) {
		this.weiboUserID = weiboUserID;
	}

	public String getWeiboID() {
		return weiboID;
	}

	public void setWeiboID(String weiboID) {
		this.weiboID = weiboID;
	}

	public long getWeiboTranimitsCount() {
		return weiboTranimitsCount;
	}

	public void setWeiboTranimitsCount(long weiboTranimitsCount) {
		this.weiboTranimitsCount = weiboTranimitsCount;
	}

	public long getWeiboCommentsCount() {
		return weiboCommentsCount;
	}

	public void setWeiboCommentsCount(long weiboCommentsCount) {
		this.weiboCommentsCount = weiboCommentsCount;
	}

	public String getWeiboContent() {
		return weiboContent;
	}

	public void setWeiboContent(String weiboContent) {
		this.weiboContent = weiboContent;
	}

	public String toString(){
		return weiboUserID+","
			   +weiboID+","
			   +weiboTranimitsCount+","
			   +weiboCommentsCount+","
			   +weiboContent;
			   
	}
}
