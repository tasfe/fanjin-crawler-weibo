package com.fanjin.action;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.fanjin.utils.SecurityCode;
import com.fanjin.utils.SecurityImage;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author ����Ĵ���
 * ����������֤���action����
 *
 */
public class SecurityCodeImageAction extends ActionSupport implements SessionAware {

	//Struts2��Map���͵�session
	private Map<String,Object> session;
	//ͼƬ��
	private ByteArrayInputStream imageStream;
	
	
	public ByteArrayInputStream getImageStream() {
		return imageStream;
	}


	public void setImageStream(ByteArrayInputStream imageStream) {
		this.imageStream = imageStream;
	}
    
	public String execute() throws Exception{
		//��ȡĬ���ѶȺͳ��ȵ���֤��
		String securityCode =SecurityCode.getSecurityCode();
		imageStream=SecurityImage.getImageAsInputStream(securityCode);
		//����session
		session.put("SESSION_SECURITY", securityCode);
		return SUCCESS;
 
	}


	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session=session;
	}
}
