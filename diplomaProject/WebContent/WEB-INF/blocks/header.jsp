<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
		<header>
			<a href="/"><img src="images/logo.png" alt="PAK logo"></a>
			<div class="login-bar">
			<a href="/user/" id="login">
			<% 
			if(request.getSession().getAttribute("user")!=null){
				out.print(request.getSession().getAttribute("user"));
				out.print("</a><button>Выход</button>");
			}else{
				out.print("Не подключен</a>");	
			}	
			%>

			</div>
		</header>
