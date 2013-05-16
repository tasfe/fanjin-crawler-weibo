package com.fanjin.utils;

/**
 * �����һ��ץȡ���յ�,��Ϊ��һ��ץȡ����㣬���õ���ģʽ
 * @author ����Ĵ���
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
			//����ļ������� ����
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
     * ��һ��ֵ�����ļ�
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
     * ���ļ���ȡһ��ֵ
     * @param key
     * @return key��Ӧ��ֵ
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
       StartPoint sp = new StartPoint("thread1.ini");
	   System.out.println(sp.get("id"));

   }
    
}
