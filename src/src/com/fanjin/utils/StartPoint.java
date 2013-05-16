package com.fanjin.utils;

/**
 * 瀛樻斁涓婁竴娆℃姄鍙栫殑缁堢偣,浣滀负杩欎竴娆℃姄鍙栫殑璧风偣锛岄噰鐢ㄥ崟渚嬫ā寮�
 * @author 鍖楅偖鍚涘悰  weibobee@gmail.com   2011-9-10
 */
import java.util.*;
import java.io.*;

public class StartPoint
{
    private Properties		 p = null;
    private String     		 fileName = null; 
    private FileInputStream  myin;
    private FileOutputStream myout;
    
   
    public StartPoint(String fileName) 
    {
		this.fileName = fileName;
		p = new Properties();
		
		File file= new File(fileName);
		try 
		{
			//濡傛灉鏂囦欢涓嶅瓨鍦� 鍒涘缓
			if(!file.exists())
				file.createNewFile();
			
			myin  = new FileInputStream(file);
			//myout = new FileOutputStream(file);
		    p.load(myin);
		   
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return ;
		}
	}
    

    /**
     * 鎶婁竴涓�煎瓨鍏ユ枃浠�
     * @param key
     * @param value
     * @return
     */
    public boolean save(String key,String value)
    {
    	p.setProperty(key, value);
    	try 
    	{
    		File file= new File(fileName);
    		myout = new FileOutputStream(file);
			p.store(myout, fileName);
			myout.flush();
		}
    	catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
    	
    	return true;
    }
    
    /**
     * 浠庢枃浠惰鍙栦竴涓��
     * @param key
     * @return key瀵瑰簲鐨勫��
     */
    public String get(String key)
    {
    	String value = null;

    	try 
    	{
    		 value = p.getProperty(key);
		}
    	catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
    	
    	return value;
    }
   
   public static void main(String [] args)
   {
/*	   StartPoint.getInstance().save("text", "123456");
	   StartPoint.getInstance().save("text1", "12345");
	   StartPoint.getInstance().save("text2", "12346");
	   */
	   StartPoint sp = new StartPoint("thread1.ini");
	  // sp.save("id", "12345");
	   System.out.println(sp.get("id"));

   }
    
}
