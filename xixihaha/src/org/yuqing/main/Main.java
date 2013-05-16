package org.yuqing.main;

/**
 * 主程序入口
 * @author 北邮君君  weibobee@gmail.com   2011-9-10
 */
import java.awt.GridLayout;
import java.util.*;
import javax.swing.*;
import org.yuqing.weibo.GrabWeiboThread;
import org.yuqing.weibo.GrabWeiboThreadSina;
import org.yuqing.weibo.SinaWeiboLogin;
import weibo4j.Weibo;

public class Main  extends JFrame implements Runnable
{
	List l = new ArrayList<JLabel>();
	List lt = new ArrayList<Thread>();
	
	
	@Override
	public void run() 
	{
		int count = 2;
		
		try
		{   String ret = JOptionPane.showInputDialog("请输入账户个数");
			if(ret == null )
			{
				count = 0;
			}else if(Integer.valueOf(ret.trim()) > 20)
			{
				count = 20;
			}
			else count = Integer.valueOf(ret.trim());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			count = 0;
		}
			
		
		for(int i = 0; i < count; i++)
		{
			//输入帐户名称，和密码进行授权
			Weibo weibo = (Weibo)new SinaWeiboLogin(Weibo.CONSUMER_KEY,Weibo.CONSUMER_SECRET).getLoginWeibo(null, null);
			if(weibo == null)
				continue;
			
			GrabWeiboThreadSina t = new GrabWeiboThreadSina(weibo,"config/thread"+i+".ini");
			Thread thread = new Thread(t,"thread"+i);
			
			//启动一个线程开始抓取
			thread.start();
			lt.add(t);
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Main m = new Main();
		new Thread(m).start();

	}

}
