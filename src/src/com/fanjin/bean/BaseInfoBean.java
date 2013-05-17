package com.fanjin.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author ����Ĵ���
 * @version V1.0
 * ����һ��΢���˺ŵĻ���Ϣ��
 */
public class BaseInfoBean implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = -2332719160811058040L;
		public String weiboUserID;
		public String weiboName;
		public String weibocreateTime;	
		public int weiboFavouritiesCount;//收藏数
		public int weiboV;				//����0�Ǵ�V��1����ͨ�û�
		public long weiboCount;         //����΢����Ŀ
		public long weiboFollwersCount;  //��˿��Ŀ
		public long weiboFriendsCount;	 //��ע�ĺ�����
		public int weiboLevel;			 //΢���ȼ�
		public String weiboProvince;
		public String weiboCity;
		public String weiboGender;
		public String weiboGenderTrend;
		public String weiboEmotion;
		public Date weiboBirthday;
		public String weiboEmail;
		public int weiboQQ;
		public String weiboUniversity;
		public String weiboCompany;
		public String weiboDesciption;
		public String weiboUrl;	
		
		public String getWeiboUserID() {
			return weiboUserID;
		}

		public void setWeiboUserID(String weiboUserID) {
			this.weiboUserID = weiboUserID;
		}

		public String getWeiboName() {
			return weiboName;
		}

		public void setWeiboName(String weiboName) {
			this.weiboName = weiboName;
		}
		public int getWeiboFavouritiesCount() {
			return weiboFavouritiesCount;
		}

		public void setWeiboFavouritiesCount(int weiboFavouritiesCount) {
			this.weiboFavouritiesCount = weiboFavouritiesCount;
		}

		public String getWeibocreateTime() {
			return weibocreateTime;
		}

		public void setWeibocreateTime(String weibocreateTime) {
			this.weibocreateTime = weibocreateTime;
		}

		public int getWeiboV() {
			return weiboV;
		}

		public void setWeiboV(int weiboV) {
			this.weiboV = weiboV;
		}

		public long getWeiboCount() {
			return weiboCount;
		}

		public void setWeiboCount(long weiboCount) {
			this.weiboCount = weiboCount;
		}

		public long getWeiboFollwersCount() {
			return weiboFollwersCount;
		}

		public void setWeiboFollwersCount(long weiboFollwersCount) {
			this.weiboFollwersCount = weiboFollwersCount;
		}

		public long getWeiboFriendsCount() {
			return weiboFriendsCount;
		}

		public void setWeiboFriendsCount(long weiboFriendsCount) {
			this.weiboFriendsCount = weiboFriendsCount;
		}

		public int getWeiboLevel() {
			return weiboLevel;
		}

		public void setWeiboLevel(int weiboLevel) {
			this.weiboLevel = weiboLevel;
		}

		public String getWeiboProvince() {
			return weiboProvince;
		}

		public void setWeiboProvince(String weiboProvince) {
			this.weiboProvince = weiboProvince;
		}

		public String getWeiboCity() {
			return weiboCity;
		}

		public void setWeiboCity(String weiboCity) {
			this.weiboCity = weiboCity;
		}

		public String getWeiboGender() {
			return weiboGender;
		}

		public void setWeiboGender(String weiboGender) {
			this.weiboGender = weiboGender;
		}

		public String getWeiboGenderTrend() {
			return weiboGenderTrend;
		}

		public void setWeiboGenderTrend(String weiboGenderTrend) {
			this.weiboGenderTrend = weiboGenderTrend;
		}

		public String getWeiboEmotion() {
			return weiboEmotion;
		}

		public void setWeiboEmotion(String weiboEmotion) {
			this.weiboEmotion = weiboEmotion;
		}

		public Date getWeiboBirthday() {
			return weiboBirthday;
		}

		public void setWeiboBirthday(Date weiboBirthday) {
			this.weiboBirthday = weiboBirthday;
		}

		public String getWeiboEmail() {
			return weiboEmail;
		}

		public void setWeiboEmail(String weiboEmail) {
			this.weiboEmail = weiboEmail;
		}

		public int getWeiboQQ() {
			return weiboQQ;
		}

		public void setWeiboQQ(int weiboQQ) {
			this.weiboQQ = weiboQQ;
		}

		public String getWeiboUniversity() {
			return weiboUniversity;
		}

		public void setWeiboUniversity(String weiboUniversity) {
			this.weiboUniversity = weiboUniversity;
		}

		public String getWeiboCompany() {
			return weiboCompany;
		}

		public void setWeiboCompany(String weiboCompany) {
			this.weiboCompany = weiboCompany;
		}

		public String getWeiboDesciption() {
			return weiboDesciption;
		}

		public void setWeiboDesciption(String weiboDesciption) {
			this.weiboDesciption = weiboDesciption;
		}

		public String getWeiboUrl() {
			return weiboUrl;
		}

		public void setWeiboUrl(String weiboUrl) {
			this.weiboUrl = weiboUrl;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public String toString(){
			return  weiboUserID+","
					+weiboName + ","
					+weiboV +","
					+weiboCount +","
					+weiboFollwersCount +","
					+weiboFriendsCount +","
					+weiboLevel +","
					+weiboProvince +","
					+weiboCity +","
					+weiboGender +","
					+weiboGenderTrend +","
					+weiboEmotion +","
					+weiboBirthday +","
					+weiboEmail +","
					+weiboQQ +","
					+weiboUniversity +","
					+weiboCompany +","
					+weiboDesciption +","
					+weiboUrl +",";
		}
}
