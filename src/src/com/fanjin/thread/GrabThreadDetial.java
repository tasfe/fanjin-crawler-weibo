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
	//�Ƿ������ð汾
	private boolean tryVersion = false;
	//����û�������Ϣ
	private List<BaseInfoBean> list = null;
	//����û���΢����
	private List<StatusBean>   list1 = null;
	//�����ļ�����
	private String fileName = null;
	//���ݿ����
	private DBConnectManager weibodb = null;
	//����
	private int limit = 5000;
	//ʣ�µĴ���
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
		//��ʼ��list
	    initList();
	}
	public void run(){
	
				
		
	}
	private void initList() {
		// TODO Auto-generated method stub
		
	}

	

}
