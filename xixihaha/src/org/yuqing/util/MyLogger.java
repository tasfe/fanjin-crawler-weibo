package org.yuqing.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * 封装的log类，存放到config/log.txt中
 * @author 北邮君君  weibobee@gmail.com   2011-9-10
 */
public class MyLogger 
{
	private Logger logger = null;
    private static MyLogger aftplogger  = null;
    private String logfile = "config/log.txt";
    private MyLogger() 
    {
    	checkFile();
    	FileHandler fhandler;
		try {
			fhandler = new FileHandler(this.logfile,true);	 //handle to log to a file
			fhandler.setFormatter(new SimpleFormatter());   //set formmattor
			this.logger = Logger.getLogger("aftp");         // get a default logger    
			this.logger.addHandler(fhandler);               //and the handler 
			this.logger.setLevel(Level.ALL);                //set log level
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * 
     * @return the single handle of AFtpLogger
     */
    static public MyLogger getInstance()
    {
    	if(aftplogger == null)
    		aftplogger = new MyLogger();
    	return aftplogger;
    }
    
    
    /**
     * 
     * check if the file exists ,if not create one 
     */
    private void checkFile()
    {
        File file = new File(logfile);
        if(!file.exists())
        {	
        	try 
        	{
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    
    /**
     * log msg to logfile
     * @param msg
     */
    public synchronized void log(String msg)
    {
    	if(msg == null || msg.length() <=0)
    		return;
    	
        this.logger.log(Level.INFO, msg);
    }
    
    
    public  static void main(String [] args)
    {
    	for(int i = 0; i < 1000; i++)
    	{
    	  MyLogger.getInstance().log(i+ " loger info");
    	}
    }
}
