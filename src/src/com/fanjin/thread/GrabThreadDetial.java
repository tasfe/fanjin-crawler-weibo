package com.fanjin.thread;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import com.fanjin.DBUtil.DBConnectManager;
import com.fanjin.bean.BaseInfoBean;
import com.fanjin.bean.StatusBean;
import com.fanjin.utils.MyLogger;
import com.fanjin.utils.StartPoint;

public class GrabThreadDetial extends GrabThread{

	//存放用户基本信息
	private List<BaseInfoBean> list = null;
	//存放用户的微博数
	private List<StatusBean>   list1 = null;
	//数据库操作
	private DBConnectManager weibodb = null;
	//配置文件名
	private String fileName = null;

	Pattern p1 = Pattern.compile("[\"|'|\\\\]");	
	private int index = 0;
	UserWapper user=null;
	public GrabThreadDetial(Object user,String name){
		this.user    = (UserWapper)user;
		this.fileName = name;
		weibodb = new DBConnectManager();
		list = new LinkedList<BaseInfoBean>();		
		//初始化list
	    initList();
	}
	public void run(){
	
				
		
	}
	private void initList() {
		 String value = new StartPoint(this.fileName).get("id");
		 System.out.println("id = " + value);
		 try
		 {
			 if(value == null || value.equals("")) //第一次使用
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

	

}
