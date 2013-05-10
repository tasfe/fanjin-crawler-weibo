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
  </head>
  <title>新浪微博抓取系统</title>
  <body>
  <center>新浪微博抓取系统<font></font></center>
  <form method="get" action="login"> 
  		 用户名:
  		 <input type="text" id="username" name="username"/>
    <br>
    <br>
  	     密码：
  	 <input type="password" id="password" name="password"/>
  	 <br>
              验证码：
              <s:textfield name="code"></s:textfield>
 
 <br/>
  	 <input type="submit" value="登录"/>
  	 <input type="reset" value="重置"/>"
  
  </form>
   
  </body>
</html>
