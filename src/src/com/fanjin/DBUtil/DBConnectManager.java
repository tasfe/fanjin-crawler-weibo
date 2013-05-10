package com.fanjin.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectManager {
	
	public static Connection getConnection() throws SQLException{
		Connection DBCon=null;
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
}


























