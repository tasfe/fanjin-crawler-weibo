package com.fanjin.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.fanjin.DBUtil.DBConnectManager;
import com.fanjin.bean.UserBean;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{
	private UserBean user;//控制层持有一个封装的url参数的javabean
	public String msg;
	//Struts2中Map类型的session
	private Map<String, Object> session;
	//接收客户端传来的验证码
	private String securityCode;
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}
	public void validate(){
		super.validate();
		if(("").equals(user.getUsername())||("").equals(user.getPassword())){
			this.addFieldError("user.getUsername()", "用户名或是密码不能为空");
		}
		if(user.getUsername().length()<6){
			this.addFieldError("user.getPassword()", "用户名长度应该大于6位");
		}
		if(user.getPassword().length()<6){
			this.addFieldError("user.getPassword()", "密码长度应该大于6位");
		}
		if(!user.getCode().equals((String)session.get("SESSION_SECURITY"))){
			this.addFieldError("user.", "验证码输入错误，请重新输入");
		}
	}
	public String execute()throws Exception{
		Connection conn=DBConnectManager.getConnection();
		Statement stmt;
		ResultSet rs;
		 String serverCode;
		 serverCode=(String)session.get("SESSION_SECURITY");
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
				 System.out.println(user.getUsername());
				 if(name.equals(user.username)&&password.equals(user.password)&&serverCode.equals(user.code)){
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
	
}

























