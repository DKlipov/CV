<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="JavaScript/lib/JQuery.js"></script>
<script type="text/javascript" src="JavaScript/lib/Chart.js"></script>
<script type="text/javascript" src="JavaScript/lib/Chart.min.js"></script>
<script type="text/javascript">
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="ru.connect.Device" %>
<%@ page import="ru.beans.UserBeanManager" %>
<%@ page import="ru.main.TelemetryContainer" %>
<%@ page import="ru.connect.DeviceManager" %>
<%@ page import="java.util.concurrent.CopyOnWriteArrayList" %>


var input=<%

if (request.getSession().getAttribute("user") == null
|| !UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canRead("robot1"))
{
	out.println("Tou not have permissons");
	return;
}

Device dev=DeviceManager.getDevice(request.getParameter("device"));
	String s1=request.getParameter("type");
	TelemetryContainer container=dev.getContainer(s1);
	JsonObject obj=new JsonObject();
	 Gson gson = new Gson();
	 obj.add("type",gson.toJsonTree(s1));
	 obj.add("data",gson.toJsonTree(container.values));
	 obj.add("axis",gson.toJsonTree(container.xAxis));
out.println(obj.toString());
%>;
var label=input.type;
var data=input.data;
var labels=input.axis;




$(document).ready(
		function() {
	var ctx = document.getElementById("myChart").getContext('2d');
	var myChart = new Chart(ctx, {
		"type" : "line",
		"data" : {
			"labels" : labels,
			"datasets" : [ {
				"label" : label,
				"data": data,
				"fill" : false,
				"borderColor" : "blue",
				"lineTension" : 0.2
			} ]
		},
		"options" : {}
	});
	console.log("test");
});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div style="position:absolute; top:60px; left:10px; width:1000px; height:500px;">
	<canvas id="myChart" width="200" height="100"></canvas>
	</div>
	<script>
	
	</script>



</body>
</html>
