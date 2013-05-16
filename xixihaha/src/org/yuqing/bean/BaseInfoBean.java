package org.yuqing.bean;


import java.io.Serializable;
import java.util.Date;

/**
 * 微博用户的基本信息
 * @author 北邮君君  weibobee@gmail.com   2011-9-10
 */

public class BaseInfoBean implements Serializable
{
   
   public String 	  id;            //用户id
   public String	  name;			 //用户昵称
   public String	  province;      //省份
   public String	  city;          //城市
   public String 	  regTime;       //注册时间
   public String      collectTime;   //收录时间
   public String 	  gender;        //性别 m f
   public int         verified;      //是否认证 0 认证  1没有
   public long  	  friendsCount;  // 关注人数
   public long  	  followerCount; //粉丝数
   public long		  statusCount;   //微博数目
   public String	  bokeUrl;       //外部url
   public String 	  discription;   //描述

   public long getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(long statusCount) {
		this.statusCount = statusCount;
	}

   
   /*  String     followers;     //粉丝的id列表,名称类表 格式为{id,name}{id,name}
   String     friends;       //朋友的id列表,名称类表 格式为{id,name}{id,name}
   
   public String getFollowers() {
		return followers;
	}
	
	public void setFollowers(String followers) {
		this.followers = followers;
	}
	
	public String getFriends() {
		return friends;
	}
	
	public void setFriends(String friends) {
		this.friends = friends;
	}
	*/
   
	public void setFriendsCount(long friendsCount) {
		this.friendsCount = friendsCount;
	}
	
	public void setFollowerCount(long followerCount) {
		this.followerCount = followerCount;
	}


   
   public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String date) {
		this.regTime = date;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getVerified() {
		return verified;
	}

	public void setVerified(int b) {
		this.verified = b;
	}

	public long getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public String getBokeUrl() {
		return bokeUrl;
	}

	public void setBokeUrl(String bokeUrl) {
		this.bokeUrl = bokeUrl;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

   public String toString()
   {
   	 return
   			 id +", "
   		     +name+ ", "
   		    +province+ ", "
   		    +city+ ", "
   		    +regTime+ ", "
   		    +collectTime+ ", "
   		    +gender+ ", "
   		    +verified+ ", "
   		    +friendsCount+ ", "
   		    +followerCount+ ", "
   		    +statusCount+", "
   		    +bokeUrl+ ", " 
   		    +discription
   	  ;
   }

};
