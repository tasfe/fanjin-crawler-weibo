package com.fanjin.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fanjin.DBUtil.DBConnectManager;
import com.opensymphony.xwork2.ActionSupport;

public class Login extends ActionSupport{
	public String username;
	public String password;
	public String msg;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String execute()throws Exception{
		Connection conn=DBConnectManager.getConnection();
		Statement stmt;
		ResultSet rs;
		try {
			String name;
			String password;
			stmt=conn.createStatement();
			String sql="select * from user where username=? and userpassword=?";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				 name=rs.getString(2);
				 password=rs.getString(3);
				 if(name==this.username&&password==this.password){
					 this.msg="µÇÂ½³É¹¦";
					 return SUCCESS;
				 }else{
					 this.msg="µÇÂ¼Ê§°Ü";
					 return ERROR;
				 }
			}
			if(rs!=null){
				rs.close();
			}else if(stmt!=null){
				stmt.close();
			}else {
				conn.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "OK!";
	}
}

























