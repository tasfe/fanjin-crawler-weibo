package com.fanjin.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import weibo4j.Comments;
import weibo4j.Favorite;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Favorites;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.org.json.JSONObject;

import com.fanjin.bean.BaseInfoBean;

/**
 * 
 * @author ����Ĵ���
 * ץȡһ��΢������ϸ��Ϣ
 *
 */
public class Main {

	public static String  access_token="2.00v5kaoCPopJNB17f90533efeTY2CC";
	public static String uid="2579840641";
	public static String id = "2579840641";
	/**
	 * @param args
	 */
	BaseInfoBean weibobean=new BaseInfoBean();
	public static void main(String[] args)  {
		writeDiskInfo();
		//��ȡ�û�
		Users um = new Users();
		um.client.setToken(access_token);
		//��ȡ����
		Comments cm =new Comments();
		cm.client.setToken(access_token);
		//��ȡ����
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		//��ȡ�ղ�
		Favorite ft = new Favorite();
		ft.client.setToken(access_token);
	    //΢���б�
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			User user = um.showUserById(uid);
			System.out.println("微博账号信息");
			System.out.println("用户id："+user.getId());
			System.out.println("用户名称："+user.getName());
			System.out.println("用户地区："+user.getLocation());
			System.out.println("用户描述："+user.getDescription());
			System.out.println("用户创建时间："+user.getCreatedAt());
			System.out.println("用户性别："+user.getGender());
			System.out.println("用户粉丝数："+user.getFollowersCount());
			System.out.println("用户好友数："+user.getFriendsCount());
			System.out.println("用户微博数："+user.getStatusesCount());
			System.out.println("用户收藏数："+user.getFavouritesCount());
			System.out.println("用户最新的状态："+user.getStatus());
			String screen_name ="起来自虫";
			UserWapper users1 = fm.getFriendsByID(id);
			for(User u : users1.getUsers()){
				System.out.println("好友列表："+u.toString());
			}
			System.out.println(users1.getNextCursor());
			System.out.println(users1.getPreviousCursor());
			System.out.println(users1.getTotalNumber());
			UserWapper users = fm.getFollowersByName(screen_name);
			for(User u : users.getUsers()){
				System.out.println("粉丝列表："+u.toString());
			}
			System.out.println(users.getNextCursor());
			System.out.println(users.getPreviousCursor());
			System.out.println(users.getTotalNumber());
			List<Favorites> favors = ft.getFavorites();
			for(Favorites s : favors){
			System.out.println("收藏列表"+s.toString());
			}
			StatusWapper status = tm.getUserTimeline();
			for(Status s : status.getStatuses()){
				System.out.println("最新微博列表"+s.toString());
			}
			JSONObject ids = tm.getUserTimelineIdsByUid(args[1]);
			Log.logInfo(ids.toString());
			Status status1 = tm.showStatus(ids.toString());
			Log.logInfo(status1.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		

	}
	public static void writeDiskInfo() {
		String dirName = "D:\\disk\\";
		String fileName = "weibo.txt";
		File file = new File(dirName + fileName);
	
		if (!file.getParentFile().exists()) {
		file.getParentFile().mkdirs();
		}
		try {
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		PrintStream printStream = new PrintStream(fileOutputStream);
		System.setOut(printStream);
		//System.out.println("world");
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
	
}
