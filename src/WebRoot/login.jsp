<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">      
function changeValidateCode(obj) {      
/*** 
  *   ��ȡ��ǰ��ʱ����Ϊ�������޾�������    
  *   ÿ��������Ҫһ����ͬ�Ĳ�����������ܻ᷵��ͬ������֤��     
  *   ���������Ļ�������й�ϵ��Ҳ���԰�ҳ������Ϊ�����棬�����Ͳ�����������ˡ�   
  */  
var timenow = new Date().getTime();      
     
obj.src="SecurityCodeImageAction?d="+timenow;      
}      
</script>     
  </head>
  <title>����΢��ץȡϵͳ</title>
  <body>
  <center>����΢��ץȡϵͳ<font></font></center>
  <s:form method="post" action="login"> 
  		 <s:textfield name="user.username" label="�˺�"></s:textfield>
  		 <br/>
  		<s:textfield name="user.password" label="����"></s:textfield>
  	     <br>
       <s:textfield name="user.code" label="��֤��"></s:textfield><img src="SecurityCodeImageAction"  onclick="changeValidateCode(this)" title="���ͼƬˢ����֤��"/> 
         <br>
         <s:submit value="��½"></s:submit> <s:reset value="����"></s:reset>
         
  </s:form>  
  </body>
</html>
