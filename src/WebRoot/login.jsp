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
  *   获取当前的时间作为参数，无具体意义    
  *   每次请求需要一个不同的参数，否则可能会返回同样的验证码     
  *   这和浏览器的缓存机制有关系，也可以把页面设置为不缓存，这样就不用这个参数了。   
  */  
var timenow = new Date().getTime();      
     
obj.src="SecurityCodeImageAction?d="+timenow;      
}      
</script>     
  </head>
  <title>新浪微博抓取系统</title>
  <body>
  <center>新浪微博抓取系统<font></font></center>
  <s:form method="post" action="login"> 
  		 <s:textfield name="user.username" label="账号"></s:textfield>
  		 <br/>
  		<s:textfield name="user.password" label="密码"></s:textfield>
  	     <br>
       <s:textfield name="user.code" label="验证码"></s:textfield><img src="SecurityCodeImageAction"  onclick="changeValidateCode(this)" title="点击图片刷新验证码"/> 
         <br>
         <s:submit value="登陆"></s:submit> <s:reset value="重置"></s:reset>
         
  </s:form>  
  </body>
</html>
