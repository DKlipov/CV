<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="ru.connect.DeviceManager"%>
<%@ page import="ru.connect.Device"%>
			<aside>
				<nav>
					<ul class="asidemenu">
						<li data-block="devices">Устройства</li>
						<li data-block="charts">Графики</li>
						<li data-block="videos">Видеозаписи</li>
						<!--li data-block="statistic">Статистика</li!-->
					</ul>
				</nav>
			</aside>
			<jsp:directive.include file="/WEB-INF/views/deviceShortView.jsp" />
			<section id="charts" hidden="true">
				<select id="chartSelect"> Выберите показатель:
					<%
					Device dev = DeviceManager.getDevice("robot1");

					if (dev != null) {

						for (String s : dev.getListCharts()) {
							out.println("<option>" + s + "</option>");
						}
					}
				%>
				</select>
				<button onclick="printChart()">Отобразить график</button>
			</section>
			<section id="videos" hidden="true">
				<span id="list-rooms"> <jsp:directive.include
						file="/WEB-INF/views/video.jsp" />
				</span>
			</section>
			<section id="statistic" hidden="true">STATISTIC</section>