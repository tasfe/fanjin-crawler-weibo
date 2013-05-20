package com.fanjin.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

import com.fanjin.DBUtil.DBConnectManager;
import com.fanjin.bean.BaseInfoBean;
import com.fanjin.bean.StatusBean;
import com.fanjin.main.Main;
import com.fanjin.utils.StartPoint;

public class GrabThreadDetial extends GrabThread{

	public static String  access_token="2.00v5kaoCPopJNB17f90533efeTY2CC";
	public static String uid="2579840641";
	public static String id = "2579840641";
	public static int tag=0;
	//用户信息列表
	private List<BaseInfoBean> list = null;
	//用户微博列表
	private List<StatusBean>   list1 = null;
	//微博数据库管理
	private DBConnectManager weibodb = null;
	//配置文件
	private String fileName = null;

	Pattern p1 = Pattern.compile("[\"|'|\\\\]");	
	private int index = 0;
	private Weibo weibo=null;
	UserWapper user=null;
	
	
	public void run(){
	
				
		
	}
	private void initList() {
		 String value = new StartPoint(this.fileName).get("id");
		 System.out.println("id = " + value);
		 try
		 {
			 if(value == null || value.equals("")) //第一次读
			 {		
				System.out.println("startPoint  failed");
				List l = user.getUsers();
				System.out.println(l.toString());
				
				if(l!=null)
					for(int i = 0; i < l.size(); i++)
					{
						User u = (User)l.get(i);
						BaseInfoBean b = this.User2BaseInfo(u);
						
						if(b != null)
							this.list.add(b);
					}
				
			 }
		 }catch(Exception e){
			 
		 }
		
	}
	private BaseInfoBean User2BaseInfo(User u) {
		// TODO Auto-generated method stub
		return null;
	}
	//通过id抓取好友列表
	private List getFriendsByID(String id) throws WeiboException{
		Friendships fm = new Friendships();
		fm.client.setToken(Main.access_token);
		String[] uid = fm.getFriendsIdsByUid(id);
		
		List list=new ArrayList();
		for(String u : uid){
			System.out.println("关注列表："+u.toString());
			list.add(u);
		}
		System.out.println("关注列表:"+list.toString());
		return list;
	}
	private UserWapper getFollowersActive(String id) throws WeiboException{
		Friendships fm = new Friendships();
		fm.client.setToken(Main.access_token);
		UserWapper users = fm.getFollowersActive(id);
	
		for(User u : users.getUsers()){
			System.out.println("粉丝列表："+u.toString());
			tag++;
			
		}
	
		return users;
		
	}
	private BaseInfoBean showUserById(String id){
		BaseInfoBean userinfo=new BaseInfoBean();
		
		return null;
		
	}
	private BaseInfoBean user2BaseInfo(User user) throws WeiboException{
		
	    if(user==null){
	    	return null;
	    }
		else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BaseInfoBean userinfo=new BaseInfoBean();
			userinfo.setWeiboUserID(user.getId());
			userinfo.setWeiboName(user.getName());
			userinfo.setWeiboCity(user.getLocation());
			String discr = user.getDescription();
			if(discr != null)
			{
				Matcher m = p1.matcher(discr);
				discr = m.replaceAll(" ");
				userinfo.setWeiboDesciption(discr);
			}
			else userinfo.setWeiboDesciption("");
			userinfo.setWeibocreateTime(format.format(user.getCreatedAt()));
			userinfo.setWeiboGender(user.getGender());
			userinfo.setWeiboFollwersCount(user.getFollowersCount());
			userinfo.setWeiboFriendsCount(user.getFriendsCount());
			userinfo.setWeiboFavouritiesCount(user.getFavouritesCount());
			//是否认证
			if(user.isVerified())
				userinfo.setWeiboV(0);
			else userinfo.setWeiboV(1);
			return userinfo;
		}
		
	}
	public static void main(String[] args) {
		
		GrabThreadDetial grab=new GrabThreadDetial();
		try {
			grab.getFriendsByID(id);			
			grab.getFollowersActive(id);
			Users um = new Users();
			um.client.setToken(access_token);
			
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	

}
