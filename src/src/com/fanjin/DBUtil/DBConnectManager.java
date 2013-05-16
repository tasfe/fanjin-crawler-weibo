package com.fanjin.DBUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.fanjin.bean.BaseInfoBean;
import com.fanjin.bean.StatusBean;


public class DBConnectManager {
	
	public static Connection DBCon = null;
	public static Statement stmt = null;
	public static Connection getConnection() throws SQLException{

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost/weibo?characterEncoding=GBK";
			String user="root";
			String password="sa";
			DBCon=DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException e1) {
			System.out.println("驱动程序加载错误");
		} catch(SQLException e2){
			System.out.println("数据库连接错误");
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return DBCon;		
	}
	public void close(Connection con){
		if(con!=null){
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	  * 向一个表中插入对象
	  * @param bib
	  * @param table
	  */
	public void insert(Object bib, String table)
	 {
		 String insertSql =  getInsertSql(bib,table);
		 try {
			stmt.clearBatch();
			stmt.execute(insertSql);
			}
			catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(insertSql);
			}
			finally
			{

			}
	 }	 
	 /**
	  * 
	  * @param查询table里的信息 条件是situation
	  * @param table 要插入的表
	  * @return 结果ResultSet对象
	  */
	 private ResultSet Query(String table,String situation)
	 {   
		 //取得对象每一个成员变量的名字 
		 //查询以条件为situation的结果
		 String sql = " select * from "+ table +"  "+situation +";";

		 ResultSet r = null;	
		try {
			stmt.clearBatch();
			r = stmt.executeQuery(sql);
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 return r;
	 }
	 
	 
	 /**
	  * 插入用户数据
	  * @param bib
	  * @param table
	  */
	 public void insertBaseinfo(BaseInfoBean bib)
	 {
		 String situation =  " where sinaID="+ bib.getWeiboUserID();
		 ResultSet r = Query("baseinfo", situation);
		 try 
		 {
			if(r.next() == false)
			  insert(bib,"baseinfo");	
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				if(r!=null)
				{ 
				  r.close();
				  r = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	 
	 }
	 
	 
	 /**
	  * 插入微博
	  * @param bib
	  * @param table
	  */
	 public void insertStatus(StatusBean bib)
	 {
		 String situation =  " where sinaDetialId="+ bib.getWeiboID();
		 ResultSet r = Query("status", situation);
		 try 
		 {
			 if(r.next() == false)
			    insert(bib,"status");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				if(r!=null)
				{ 
				  r.close();
				  r = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		 
		 
	 }
	 
	 /**
	  * 
	  * @param bib 要插入的对象
	  * @param table 要插入的表
	  * @return 返回一条插入语句
	  */
	 public String getInsertSql(Object bib, String table)
	 {   
		 String sql = "insert into "+ table+ " values ( ";

 		 Field[] field = bib.getClass().getFields();
		 for(int i = 0 ; i < field.length; i++)
		 {
			 try {
				// System.out.println(i + " " +field[i].get(bib));
			   if(i<field.length-1)
				   sql+= "\""+field[i].get(bib)+"\",";
			   else
					 sql+= "  \""+ field[i].get(bib) + "\");";
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		 }
		 
		 return sql;
	 }
	 
	 
	 /**
	  * 取得现在总共有多少信息
	  * @return
	  */
	 public int getTotal(String table)
	 {

		 String sql = "select count(*) from "+ table;
		 int ret = -1;
		 ResultSet r = null;	
		 try 
		 {
			stmt.clearBatch();
			r = stmt.executeQuery(sql);
			if(r.next())
				ret = r.getInt(1);
			return ret;
		  }
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
	 }
	
}


























