<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="ru.connect.DeviceManager"%>
<%@ page import="ru.beans.UserBeanManager"%>
<%@ page import="ru.connect.Device"%>
<%@ page import="ru.beans.Mailer"%>
<!doctype html>
<html>
<head>
<script type="text/javascript" src="JavaScript/lib/JQuery.js"></script>
<script type="text/javascript" src="JavaScript/lib/Chart.js"></script>
<script type="text/javascript" src="JavaScript/lib/Chart.min.js"></script>
<script type="text/javascript" src="JavaScript/homeScript.js"></script>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>PAKKOvP</title>
<link rel="stylesheet" href="css/styles.css" type="text/css">
</head>
<body>
	<jsp:directive.include file="/WEB-INF/blocks/header.jsp" />
	<nav id="topmenu">
		<button data-block="control">Управление</button>
		<%
			if (request.getSession().getAttribute("user") != null
					&& UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).isAdmin) {
				out.println("<button data-block=\"admin\">Администрирование</button>");
			}
		%>
		<!--button data-block="settings">Настройки</button!-->
		<!--button data-block="update">Обновление</button!-->
	</nav>
	<div id="heading">
		<span>Управление</span><span> > Устройства</span>
	</div>
	<div id="block">
		<div id="control" hidden="true">
			<%if (request.getSession().getAttribute("user") != null
					&& UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canRead("robot1"))
				{ %>
			<jsp:directive.include file="/WEB-INF/views/deviceView.jsp" />
			<%} else{
				out.println("Нет доступных для управления устройств");
			}%>
		</div>

		<div id="admin" hidden="true">
			<aside>
				<nav>
					<ul class="asidemenu">
						<li data-block="users">Пользователи</li>
						<li data-block="userRights">Права доступа</li>
						<li data-block="alarm">Оповещения</li>
					</ul>
				</nav>
			</aside>
			<section id="users" hidden="true">
			Логин:<input id="newLogin"></input>Пароль:<input id="newPass"></input>Администратор:<input type="checkbox"id="newAdmin"></input>
			<p><button id="butAddUser" onclick="addUser()">Добавить пользователя</button></p>
			Логин:<input id="delLogin"></input>
			<p><button id="butDelUser" onclick="delUser()">Удалить пользователя</button></p>
			<p>Список пользователей:</p>
				<span id="list-users">
					<%
 	for (String s : UserBeanManager.getUserList()) {
 		out.println(" <p>" + s + "</p>");
 	}
 %>
				</span>
			</section>
			<section id="userRights" hidden="true">
				<span id="list-devices"> 
				<p>
				Пользователь:
				<select id="changeRight1" onchange="rightOnClick()">
											<%
 	for (String s : UserBeanManager.getUserList()) {
 		out.println(" <option>" + s + "</option>");
 	}
 %>
				</select>
				</p>
				<p>Устройство:
				<select id="changeRight2" onchange="rightOnClick()">
							<%
 	for (String s : DeviceManager.getDeviceNames()) {
 		out.println(" <option>" + s + "</option>");
 	}
 %>
				</select>
	</p>
	<p>Право доступа: <span id="valueRight">-</span></p>
	
	<select id="changeRight3" onchange="rightOnClick()">
	<option value="0">Нет доступа</option>
	<option value="1">Чтение</option>
	<option value="2">Управление</option>
	</select>
			<button id="butSetRight" onclick="rightSetOnClick()">Установить</button>
				</span>
			</section>
			<section id="alarm" hidden="true">
			Логин:<input id="emailLogin"></input>Пароль:<input id="emailPass"></input>SMTP сервер:<input id="emailServer"></input>
			
			<p><button onclick="changeEmail()">Сменить адрес почты (на отправку)</button></p>
			<p>
			<input type="checkbox" id="emailStateChangeBox" onchange="emailStateChange()" 
			<% if(!Mailer.isCanSend()){out.println("disabled='true'");}%>
			<% if(Mailer.state){out.println("checked='checked'");}%>
			
			>Включить отправку оповещений на email</input></p>
			Логин:<input id="dispathEmail"></input>
			<p><button onclick="dispathEmailAdd()">Добавить адрес рассылки</button>
			<button onclick="dispathEmailDel()">Удалить адрес рассылки</button>
			</p>
					<p>Список адресов рассылки:</p>
				<span id="list-users">
					<%
 	for (String s : Mailer.getMailList()) {
 		out.println(" <p>" + s + "</p>");
 	}
 %>
				</span>
				<p><button onclick="testDispath()">Отправить тестовое сообщение</button></p>
			</section>
		</div>
		<!--div id="settings" hidden="true">
			<aside>
				<nav>
					<ul class="asidemenu">
						<li data-block="sitesettings">Сайт</li>
					</ul>
				</nav>
			</aside>
			<section id="sitesettings">
				<h1>IT IS settings</h1>
			</section>
		</div!-->
		<!--div id="update" hidden="true">
			<aside>
				<nav>
					<ul class="asidemenu">
						<li data-block="update">Обновления</li>
					</ul>
				</nav>
			</aside>
			<section id="update" hidden="true">
				<h1>IT IS update</h1>
			</section>
		</div!-->
	</div>
	<jsp:directive.include file="/WEB-INF/blocks/footer.jsp" />
</body>
</html>
