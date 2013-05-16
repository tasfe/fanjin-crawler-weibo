package com.fanjin.thread;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.User;

import com.fanjin.DBUtil.DBConnectManager;
import com.fanjin.bean.BaseInfoBean;
import com.fanjin.bean.StatusBean;
import com.fanjin.utils.MyLogger;
import com.fanjin.utils.StartPoint;

public class GrabThreadDetial extends GrabThread{
	//是否是试用版本
	private boolean tryVersion = false;
	//存放用户基本信息
	private List<BaseInfoBean> list = null;
	//存放用户的微博数
	private List<StatusBean>   list1 = null;
	//配置文件名称
	private String fileName = null;
	//数据库操作
	private DBConnectManager weibodb = null;
	//限制
	private int limit = 5000;
	//剩下的次数
	private int remian = 100;
	private int tryLimit = 20000;
	Pattern p1 = Pattern.compile("[\"|'|\\\\]");	
	private int index = 0;
	Weibo weibo = null;
	public GrabThreadDetial(Object weibo,String name){
		this.weibo    = (Weibo)weibo;
		this.fileName = name;
		weibodb = new DBConnectManager();
		list = new LinkedList<BaseInfoBean>();		
		//初始化list
	    initList();
	}
	public void run(){
	
				
		
	}
	private void initList() {
		// TODO Auto-generated method stub
		
	}

	

}
