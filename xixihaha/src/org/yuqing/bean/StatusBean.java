package org.yuqing.bean;
/**
 *  用户关系：朋友列表
 *  @author 北邮君君  weibobee@gmail.com   2011-8-10
 */

import java.util.Date;

public class  StatusBean 
{
	public String   weiboId;  	  //微博id
	public String   id ;      	  //微博用户id
	public long     twNum;   	  //转发次数
	public long     comNum;		  //评论次数
	public String   createTime ;  //最后一次访问时间
	public String   status;       //微博内容   
   
public String toString() {
		return
   			  weiboId +", "
   		    + this.id +"," 
   		    + this.twNum +"," 
   		    + this.comNum +"," 
   		    + this.createTime +","
   		    + this.status;
   }
    
    
    public long getComNum() {
	return comNum;
}


public void setComNum(long comNum) {
	this.comNum = comNum;
}


public String getCreateTime() {
	return createTime;
}


public void setCreateTime(String createTime) {
	this.createTime = createTime;
}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getcreateTime() {
		return createTime;
	}

	public void setcreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
   public String getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	public long getTwNum() {
		return twNum;
	}

	public void setTwNum(long twNum) {
		this.twNum = twNum;
	}

};
