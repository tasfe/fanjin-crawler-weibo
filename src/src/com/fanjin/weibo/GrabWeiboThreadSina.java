package com.fanjin.weibo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.security.pkcs11.Secmod.DbMode;
import weibo4j.Count;
import weibo4j.Paging;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.fanjin.DBUtil.DBConnectManager;
import com.fanjin.bean.BaseInfoBean;
import com.fanjin.bean.StatusBean;
import com.fanjin.utils.MyLogger;
import com.fanjin.utils.StartPoint;

/**
 *  微博抓取线程具体籄1�7
 *  @author 北邮君君  weibobee@gmail.com   2011-6-1
 *
 */
public  class GrabWeiboThreadSina extends GrabWeiboThread 
{
	//是否是试用版的1�7
	private boolean tryVersion = false;
	//存放用户基本信息
	private List<BaseInfoBean> list = null;
	//存放用户的微卄1�7
	private List<StatusBean>   list1 = null;
	//配置文件各1�7
	private String fileName = null;
	//数据库操佄1�7
	private DBConnectManager weibodb = null;
	//限制
	private int limit = 500;
	//剩下的次敄1�7
	private int remian = 100;
	
	private int tryLimit = 20000;
	Pattern p1 = Pattern.compile("[\"|'|\\\\]");
	
	private int index = 0;
	Weibo weibo = null;
	
	public GrabWeiboThreadSina(Object weibo,String name) 
	{
		this.weibo    = (Weibo)weibo;
		this.fileName = name;
		weibodb = new DBConnectManager();
		list = new LinkedList<BaseInfoBean>();
		
		//初始化list
	    initList();
	}
	
	public void run() 
	{
	
	   	List lfr = null;
		List lfo = null;
		int count = 0;
		int totalcount = 0;
		//每一次获取的用户数目
		Paging p = new Paging();
   	    p.setCount(198);
   	    
   	    this.getSituation();
   	    int total = weibodb.getTotal("baseinfo");
	    totalcount = 0;
	 
	     
		 if(tryVersion && (total == -1 || total > tryLimit)) 
		 {
			 System.out.println("您好 数据库已经有"+total+"条数据，超过 试用版weibobee 限制"+tryLimit+"条！霄1�7要完整功能版，请联系作�1�7�！");
			 sleep(3600000);
			 return;
		 }
		 
		while(true)
		{
			try
			{
				 //取得丄1�7个用戄1�7
				 index = (int) new Date().getTime() % list.size();
				 if(index < 0) index=-index;
				 
				 BaseInfoBean baseInfoBean = (BaseInfoBean)list.get(index);
				 list.remove(index);
				
				 list1 =  new LinkedList<StatusBean>();
				 
        	     p.setPage(1);
				 lfr = weibo.getFriendsStatuses(baseInfoBean.getWeiboUserID(), p);
				 
				 p.setPage(1);
				 lfo = weibo.getFollowersStatuses(baseInfoBean.getWeiboUserID(),p);
				 
				// baseInfoBean = baseinfoPefect(baseInfoBean,lfr,lfo);
				
			    //如果队列太小了加入到其中
				for(int i = 0; i < lfr.size(); i++)
				{
					User u = (User)lfr.get(i);
					BaseInfoBean b = this.User2BaseInfo(u);
					StatusBean   s = this.User2Status(u);
					if(b != null)
						this.list.add(b);
					if(s != null)
					    this.list1.add(s);
					//else System.out.println("status == null :" + u.getScreenName()+" ,"+u.getStatusText());
				}
				
				for(int i = 0; i < lfo.size(); i++)
				{
					User u = (User)lfo.get(i);
					BaseInfoBean b = this.User2BaseInfo(u);
					StatusBean   s = this.User2Status(u);
					if(b != null)
						this.list.add(b);
					if(s != null)
					    this.list1.add(s);
					//else System.out.println("status == null :" + u.getName()+" ,"+u.getScreenName()+" ,"+u.getStatusText());
				}			
				
			 
				//获取转发数和评论敄1�7
				//this.list1 = this.StatusPerfect(this.list1);
				 
				
				//用户信息存入数据庄1�7
				for(int i = 0; i < this.list.size(); i++)
				{
					try
					{
						BaseInfoBean b = (BaseInfoBean)list.get(i);
						weibodb.insertBaseinfo(b);				
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				
				//微波信息存入数据庄1�7
				for(int i = 0; i < this.list1.size(); i++)
				{
					try
					{
						StatusBean s = (StatusBean)list1.get(i);
						weibodb.insertStatus(s);	
					}
					catch(Exception e)
					{
						e.printStackTrace();
						this.list1.clear();
					}
				}
				
				//如果队列太长了则删除
				if(this.list.size() - limit > 1000)
				{	
					int count1 = this.list.size() - limit;
					for(int i = 0; i < count1; i++)
					 ((LinkedList)list).removeFirst();
				}
				
				 //保存起点
				 if(baseInfoBean.getWeiboFollwersCount() > 1000)
				 {
					
					 new StartPoint(this.fileName).save("id",baseInfoBean.getWeiboUserID()+"");
				 }
				 
				 
				 System.out.println(new Date().toLocaleString()+" "+this.fileName +"     remains: "+ this.remian);
				 lfr = null;
				 lfo = null;
				 list1 =  null;
				 
				 //申请收集垃圾
				 System.gc();
				 if(this.remian <= 50)
					 this.sleep(60000); 
				 else
					 sleep(50);
				 
				 this.getSituation();
				 
				 //是否是试用版的1�7
				 if(this.tryVersion && totalcount++ >= 10)
				 {
					 total = weibodb.getTotal("baseinfo");
					 totalcount = 0;
					 
					 if(total == -1 || total > tryLimit)
					 {
						 System.out.println("您好 数据库已经有"+total+"条数据，超过 试用版weibobee 限制"+tryLimit+"条！霄1�7要完整功能版，请联系作�1�7�！");
						 sleep(3600000);
						 return;
					 }
					 
				 }
				 
			}
			catch(Exception e)
			{
				e.printStackTrace();
				MyLogger.getInstance().log(e.toString());
				System.out.println("meet exception sleep 2 minute");
			    sleep(120000);
			}
		}
			
		
	}
	
	/**
	 * 初始化list
	 */
	void initList()
	{
		 String value = new StartPoint(this.fileName).get("id");
		 System.out.println("id = " + value);
		 try
		 {
			 if(value == null || value.equals("")) //第一次使甄1�7
			 {		
				System.out.println("startPoint  failed");
				List l = weibo.getFollowers();
				
				if(l!=null)
					for(int i = 0; i < l.size(); i++)
					{
						User u = (User)l.get(i);
						BaseInfoBean b = this.User2BaseInfo(u);
						
						if(b != null)
							this.list.add(b);
					}
				
				l = weibo.getFriends();
				
				if(l!=null)
					for(int i = 0; i < l.size(); i++)
					{
						User u = (User)l.get(i);
						BaseInfoBean b = this.User2BaseInfo(u);
						
						if(b != null)
							this.list.add(b);
					}
				l = null;
			 }
			 else   
			 {      System.out.println("startPoint  ok");
				 	List l = weibo.getFriends();
				 	
				 	if(l != null)
						for(int i = 0; i < l.size(); i++)
						{
							User u = (User)l.get(i);
							BaseInfoBean b = this.User2BaseInfo(u);
							this.list.add(b);
						}
					l = null;
			 }
		 
		 } 
		 catch (WeiboException e) 
		 {		
				e.printStackTrace();
		 }
	}
	
	/**
	 * 将user 转换丄1�7 BaseInfoBean
	 * @param u
	 * @return
	 */
	private BaseInfoBean User2BaseInfo(User u)
	{
		if(u == null)
			return null;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BaseInfoBean baseinfo = new BaseInfoBean();
		baseinfo.setWeiboUserID(u.getId()+"");
		baseinfo.setWeiboName(u.getName());
		//baseinfo.setRegTime(format.format(u.getCreatedAt()));
		//baseinfo.setCollectTime(format.format(new Date()));
		baseinfo.setWeiboGender(u.getGender());
		baseinfo.setWeiboFollwersCount(u.getFollowersCount());
		baseinfo.setWeiboFriendsCount(u.getFriendsCount());
		baseinfo.setWeiboUrl(u.getURL()+"");
	
		baseinfo.setWeiboCount((long)u.getStatusesCount());
		
		String discr = u.getDescription();
		if(discr != null)
		{
			Matcher m = p1.matcher(discr);
			discr = m.replaceAll(" ");
			baseinfo.setWeiboDesciption(discr);
		}
		else baseinfo.setWeiboDesciption("");
		
		//是否认证
		if(u.isVerified())
			baseinfo.setWeiboV(0);
		else baseinfo.setWeiboV(1);
		//设置省份和城帄
		 String location = u.getLocation();
		 String[] split = location.split(" ");
		 if(split.length>=1)
			 baseinfo.setWeiboProvince(split[0]);
		 else  baseinfo.setWeiboProvince(null);
		 
		 if(split.length>=2)
			 baseinfo.setWeiboCity(split[1]);
		 else   baseinfo.setWeiboCity(null);
		return baseinfo;
		
	}
	
	/**
	 * user 转换丄1�7 status
	 * @param u
	 * @return
	 */
	private StatusBean User2Status(User u)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StatusBean s = new StatusBean();
		s.setWeiboUserID(u.getStatusId()+"");
		s.setWeiboUserID(u.getId()+"");

		
		String discr = u.getStatusText();
		if(discr != null)
		{
			Matcher m = p1.matcher(discr);
			discr = m.replaceAll(" ");
			s.setWeiboContent(discr);
			
		}
		
		
		
		if(s.getWeiboUserID().equals("-1") || s.getWeiboContent() == null )
			return null;
	
		return s;
	}
	
	/**
	 * 批量获取这个转发数和评论敄1�7 
	 * @param l
	 * @return
	 */
	private List StatusPerfect(List<StatusBean> l)
	{
		String ids = "";
		for(int i = 0;   i < l.size(); i++)
		{
			StatusBean b = l.get(i);
			if(i >= l.size()-1)
				ids += b.getWeiboUserID();
			else 
				ids += b.getWeiboUserID()+",";
		}
		System.out.println("perfect:");
		System.out.println(ids);
		
		List<Count> ll;
	
		try 
		{
			ll = weibo.getCounts(ids);
			
			if(l.size() != ll.size())
				return l;
			for(int i = 0; i < ll.size(); i++)
			{
				//评论数和转发
				((StatusBean)l.get(i)).setWeiboCommentsCount(ll.get(i).getRt());
				((StatusBean)l.get(i)).setWeiboTranimitsCount(ll.get(i).getComments());				
				
			}
			
		}		
		catch (WeiboException e)
		{
			e.printStackTrace();
			return l;
		}
		
		System.out.println("lsize :ll size " + l.size() +"  " + ll.size());
		return l;
		
	}
	
	/**
	 * sleep
	 */
	private void sleep( int time)
	{
		if(time <= 0 ) time = 500;
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void getSituation()
	{  
		try 
		{
		    this.remian  = weibo.getRateLimitStatus().getRemainingHits();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public void main(String [] args)
	{
			
		Pattern p1 = Pattern.compile("[\"|']");
		
		String str1 = " \" hello \"\" dsf \" \\sadf ";
		Matcher m = p1.matcher(str1);
		str1 = m.replaceAll(" ");

		
		System.out.println(str1);

		
	}
	
}
