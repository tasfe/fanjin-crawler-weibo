package org.yuqing.weibo;

/**
 * 微博登录的抽象类
 *  @author 北邮君君  weibobee@gmail.com   2011-9-1
 *
 */
abstract class WeiboLogin 
{

	private String appkey=null;
	private String appsecret = null;
	
	public WeiboLogin(String appkey,String appsecret) 
	{
		this.appkey    = appkey;
		this.appsecret = appsecret;
	}
	
	abstract public Object getLoginWeibo(String username,String passwd);
	
	
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
}
