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
	private UserBean user;//���Ʋ����һ����װ��url������javabean
	public String msg;
	//Struts2��Map���͵�session
	private Map<String, Object> session;
	//���տͻ��˴�������֤��
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
			this.addFieldError("user.getUsername()", "�û����������벻��Ϊ��");
		}
		if(user.getUsername().length()<6){
			this.addFieldError("user.getPassword()", "�û�������Ӧ�ô���6λ");
		}
		if(user.getPassword().length()<6){
			this.addFieldError("user.getPassword()", "���볤��Ӧ�ô���6λ");
		}
		if(!user.getCode().equals((String)session.get("SESSION_SECURITY"))){
			this.addFieldError("user.", "��֤�������������������");
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
					 this.msg="��½�ɹ�";
					 return SUCCESS;
				 }else{
					 this.msg="��¼ʧ��";
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

























