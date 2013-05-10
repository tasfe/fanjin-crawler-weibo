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
			String sql="select * from user";
			rs=stmt.executeQuery(sql);
			System.out.println(rs.toString());
			while(rs.next()){
				 name=rs.getString(2);
				 System.out.println(name);
				 password=rs.getString(3);
				 System.out.println(password);
				 System.out.println(this.getUsername());
				 if(name.equals(this.username)&&password.equals(this.password)){
					 this.msg="登陆成功";
					 return SUCCESS;
				 }else{
					 this.msg="登录失败";
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
		return msg;
	}
	public void validate(){
		if(("").equals(this.username)||("").equals(this.password)){
			this.addFieldError("msg", "用户名或是密码不能为空");
		}
		if(this.username.length()<6){
			this.addFieldError("msg", "用户名长度应该大于6位");
		}
		if(this.password.length()<6){
			this.addFieldError("msg", "密码长度应该大于6位");
		}
	}
}

























