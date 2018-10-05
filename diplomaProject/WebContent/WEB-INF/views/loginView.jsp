<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="JavaScript/lib/JQuery.js"></script>
<script type="text/javascript" src="JavaScript/loginScript.js"></script>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>PAKKOvP</title>
<link rel="stylesheet" href="css/styles.css" type="text/css">
</head>
<body>
<jsp:directive.include file="/WEB-INF/blocks/header.jsp" />
<section class="login-input">
Логин:<input type="text" id="loginfield" placeholder="Логин"></input>
Пароль:<input type="password" id="passwordfield" placeholder="Пароль"></input>
<button id="loginbutton">Войти</button>
</section>
<jsp:directive.include file="/WEB-INF/blocks/footer.jsp" />
</body>
</html>