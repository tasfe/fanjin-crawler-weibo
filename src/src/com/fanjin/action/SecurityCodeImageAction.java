package com.fanjin.action;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.fanjin.utils.SecurityCode;
import com.fanjin.utils.SecurityImage;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author 砍柴的大叔
 * 用于输入验证码的action请求
 *
 */
public class SecurityCodeImageAction extends ActionSupport implements SessionAware {

	//Struts2中Map类型的session
	private Map<String,Object> session;
	//图片流
	private ByteArrayInputStream imageStream;
	
	
	public ByteArrayInputStream getImageStream() {
		return imageStream;
	}


	public void setImageStream(ByteArrayInputStream imageStream) {
		this.imageStream = imageStream;
	}
    
	public String execute() throws Exception{
		//获取默认难度和长度的验证码
		String securityCode =SecurityCode.getSecurityCode();
		imageStream=SecurityImage.getImageAsInputStream(securityCode);
		//放入session
		session.put("SESSION_SECURITY", securityCode);
		return SUCCESS;
 
	}


	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session=session;
	}
}
