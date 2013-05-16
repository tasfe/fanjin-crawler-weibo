package com.fanjin.utils;

/**
 * 存放上一次抓取的终点,作为这一次抓取的起点，采用单例模弄1�7
 * @author 北邮君君  weibobee@gmail.com   2011-9-10
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
			//如果文件不存圄1�7 创建
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
     * 把一个�1�7�存入文仄1�7
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
     * 从文件读取一个�1�7�1�7
     * @param key
     * @return key对应的�1�7�1�7
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
