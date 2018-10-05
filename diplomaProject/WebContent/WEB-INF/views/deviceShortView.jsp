<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    	<%@ page import="java.util.Map" %>
	<%@ page import="ru.connect.DeviceManager" %>
    <section id="devices"hidden="true">
			<aside style="float: right;">
				<nav>
					<ul class="rightmenu">
					
						<%
	for(String s:DeviceManager.getDeviceNames()){
		out.println("<li data-url='control/devices' id='"+s+"' onclick='clickSubmenu(this)'>"+s+"</li>");
	}
	%>
								
					<!-- % 
					for(String s:DeviceManager.getDeviceNames()){
						out.print("<li data-url='control/devices' data-submenu='");
						out.print(s);
						out.print("'>");
						out.print(s);
						out.print("</li>");  			
					}
				      %!-->
				      </ul>
				</nav>
			</aside>
			<div>
				Камера<input type="checkbox" id="enableCamera"/>
				Карта<input type="checkbox" id="enableMap"/>
			</div>
					<div class="robot-images">
					
					<span id="cameraView" hidden="true">
					<img id="camera" style="-webkit-user-select: none;"  width="286" height="215">
					</span>
					<span id="mapView" hidden="true">
						<img id="map" src="room.png"  alt="Image">
						<img id="arrow" src="arrow.png">
						</span>
						<div>
											<span id="cameraButtons" hidden="true">
					<button id="changeCamera">Следующая камера</button>
					<button id="changeCameraState">Включить камеру</button>
					</span>
						<span id="mapButtons" hidden="true">
						<button id="mapBig">+</button>
						<button id="mapLittle">-</button>
						
									<%if (request.getSession().getAttribute("user") != null
					&& UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canWrite("robot1"))
				{ %>
						<button id="pathStart">Построить маршрут</button>
						<button id="pathStop" hidden="true">Завершить рисование</button>
						<% }%>
						<canvas id="canvas1"></canvas>
						</span>
	
					</div>
					

				</div>
				<div id="robot-top">
					<p>Статус: <span id="robot-status">Отключен</span></p>
					<div id="control-mode">
								<%if (request.getSession().getAttribute("user") != null
					&& UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canWrite("robot1"))
				{ %>
<jsp:directive.include file="/WEB-INF/views/autoControlView.jsp" />
<jsp:directive.include file="/WEB-INF/views/manualControlView.jsp" />
			<p>
			<input type="checkbox" id="deviceStateChangeBox" onchange="deviceStateChange()" 
			<% if(DeviceManager.getDevice("robot1").getAlarmState()){out.println("checked='checked'");}%>
			>Включить сторожевой режим</input></p>
					<%} %>	
					</div>
				</div>
				<div id="robot-data">
					<p>Координаты X:<span id="coordinatex">130</span>
					Y:<span id="coordinatey">370</span>
					A:<span id="coordinateangle">0</span>
					</p>
				<p>Уровень газа: <span id='updGO'>-</span></p>
				<p>Температура: <span id='updTB'>-</span></p>
				<p>Уровень влажности: <span id="updVV">-</span>%</p>
				<p>Уровень давления: <span id="updPA">-</span>мм</p>
				</div>
				
								<div id="robot-log">
					<!-- h3 id="resume">Критических ситуаций не выявлено</h3!-->
					<span id="log">
					<p>Устройство не подключено</p>
					</span>
				</div>
			</section>
				
