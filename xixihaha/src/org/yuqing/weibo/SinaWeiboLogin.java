package org.yuqing.weibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JOptionPane;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import weibo4j.util.BareBonesBrowserLaunch;


/**
 * sinaweibo登录的具体类
 *  @author 北邮君君  weibobee@gmail.com   2011-9-1
 *
 */
public class SinaWeiboLogin 
{
	private String appkey=null;
	private String appsecret = null;
	public SinaWeiboLogin(String appkey,String appsecret) 
	{
		this.appkey    = appkey;
		this.appsecret = appsecret;
	}
	
	
	
	public String getAppkey()
	{
		return appkey;
	}
	public void setAppkey(String appkey) 
	{
		this.appkey = appkey;
	}
	public String getAppsecret() 
	{
		return appsecret;
	}
	public void setAppsecret(String appsecret) 
	{
		this.appsecret = appsecret;
	}

	//登录微博。
	public Object getLoginWeibo(String username, String passwd)  {
		System.setProperty("weibo4j.oauth.consumerKey", this.getAppkey());
		System.setProperty("weibo4j.oauth.consumerSecret", this.getAppsecret());
		 Weibo weibo = new Weibo();
		try {			   
			RequestToken requestToken = weibo.getOAuthRequestToken();  
			System.out.println("Got request token.");
			System.out.println("Request token: "+ requestToken.getToken());
			System.out.println("Request token secret: "+ requestToken.getTokenSecret());
			AccessToken accessToken = null;

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (null == accessToken) {
				BareBonesBrowserLaunch.openURL(requestToken.getAuthorizationURL());
				//输入授权码
				String pin = JOptionPane.showInputDialog("输入网页上的授权码:");
				try{
					accessToken = requestToken.getAccessToken(pin.trim());
				} catch (WeiboException te) {
					if(401 == te.getStatusCode()){
						System.out.println("Unable to get the access token.");
					}else{
						te.printStackTrace();
					}
					
					return null;
				}
			}
			System.out.println("Got access token.");
			System.out.println("Access token: "+ accessToken.getToken());
			System.out.println("Access token secret: "+ accessToken.getTokenSecret());

			//获取授权码成功
			weibo.setToken(accessToken.getToken(), accessToken.getTokenSecret());
		} catch (WeiboException te) {
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit( -1);
			return null;
		}
	return weibo;
}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
         Weibo weibo = (Weibo)new SinaWeiboLogin(Weibo.CONSUMER_KEY,Weibo.CONSUMER_SECRET).getLoginWeibo(null, null);
        /* try {
        	 Paging p = new Paging();
        	 p.setCount(199);
        	 p.setPage(1);
			List list = weibo.getFriends(1097201945+"", p);
			for(int i = 0; i < list.size(); i++)
			{
				User u = (User)list.get(i);
				System.out.print(i+" "+ u.getName()+" " + u.getId());
			}
			
        	 p.setPage(2);
			 list = weibo.getFriends(1097201945+"", p);
			for(int i = 0; i < list.size(); i++)
			{
				User u = (User)list.get(i);
				System.out.print((i+199)+" "+ u.getName()+" " + u.getId());
			}
        	 

        	 
        	 
        	 
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
         
         try {
			User u = weibo.getUserDetail("46902765");
			System.out.println(u.getId() +" "+u.getName());
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
	}

}
