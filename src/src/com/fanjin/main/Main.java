package com.fanjin.main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import weibo4j.Weibo;
import com.fanjin.weibo.GrabWeiboThreadSina;
import com.fanjin.weibo.SinaWeiboLogin;





	public class Main  extends JFrame implements Runnable
	{
		List l = new ArrayList<JLabel>();
		List lt = new ArrayList<Thread>();
		
		
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


