var currentBlock = undefined;
var currentCell = {};
var farseId;

basex=110;
basey=368;
coordinate={
		x:-100,
		y:0,
		angle:0
};

function deviceStateChange(){
	var state=0;
	if ($('#deviceStateChangeBox').is(':checked')){
		state=1;
	}
	$.ajax({
		url : "control/devices",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"changeState" : state,
			"device" :"robot1"
		}
		}
	);
}

function changeEmail(){
	
	$.ajax({
		url : "mail",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"password" : $("#emailPass").val(),
			"login" : $("#emailLogin").val(),
			"action" : "addMain",
			"server" : $("#emailServer").val()
		},
		complete : function(data, textStatus) {  
			 $("#emailPass").val("");
			 $("#emailLogin").val("");
			 $("#emailServer").val("");
		}
		}
	);
}

function emailStateChange(){
	var state="setDisable";
	if ($('#emailStateChangeBox').is(':checked')){
		state="setEnable";
	}
	$.ajax({
		url : "mail",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"action" : state
		}
		}
	);
	
}

function dispathEmailAdd(){
	$.ajax({
		url : "mail",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"login" : $("#dispathEmail").val(),
			"action" : "addDispath",
		},
		complete : function(data, textStatus) {  
			 $("#dispathEmail").val("");
		}
		}
	);
}

function dispathEmailDel(){
	$.ajax({
		url : "mail",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"login" : $("#dispathEmail").val(),
			"action" : "delDispath",
		},
		complete : function(data, textStatus) {  
			 $("#dispathEmail").val("");
		}
		}
	);
}

function testDispath(){
	$.ajax({
		url : "mail",
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"action" : "sendTest"
		}
		}
	);
}

function addUser(){
	var admin=0;
	$("#butAddUser").prop("disabled", true);
	if ($('#newAdmin').is(':checked')){
	    admin=1;
	}
	$.ajax({
		url : "user",
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"password" : $("#newPass").val(),
			"admin" : admin,
			"action" : "add",
			"user" : $("#newLogin").val()
		},// тип загружаемых данных
		complete : function(data, textStatus) {  
			$("#butAddUser").prop("disabled", false);
			$("#newPass").val("");
			$("#newLogin").val("");
			$('#newAdmin').prop('checked', false);
		}
	});
}

function delUser(){
	$("#butDelUser").prop("disabled", true);
	$.ajax({
		url : "user",
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"action" : "delete",
			"user" : $("#delLogin").val()
		},// тип загружаемых данных
		complete : function(data, textStatus) {  
			$("#butDelUser").prop("disabled", false);
			$("#delLogin").val("");
		}
	});
}

function rightOnClick(){
	$("#valueRight").html("-");
	$.ajax({
		url : "user",
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"action" : "getRight",
			"device" : $("#changeRight2 option:selected").text(),
			"user" : $("#changeRight1 option:selected").text()
		},// тип загружаемых данных
		success : function(data, textStatus) { 
			var value="Нет доступа";
			if(data.value>1){value="Управление";}
			else if(data.value>0){value="Чтение";}
			$("#valueRight").html(value);
		}, 
		error: function(data, textStatus) {  
			$("#valueRight").html("error");
		}
	});
}

function rightSetOnClick(){
	$("#butSetRight").prop("disabled", true);
	$("#valueRight").html("-");
	$.ajax({
		url : "user",
		cache : false,// указываем URL и
		dataType : "text",
		data : {
			"action" : "change",
			"device" : $("#changeRight2 option:selected").text(),
			"value" : $("#changeRight3").val(),
			"user" : $("#changeRight1 option:selected").text()
		},
		complete: function(data, textStatus) {  
			$("#butSetRight").prop("disabled", false);
			rightOnClick();
		}
	});
}

function setPosition(ss){
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"setPosition" : ss,
			"device" : "robot1"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик 
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
		}
	});
}


function updateCoordinate(){
	/*var x=basex+data.x;
	var y=basey+data.y;
	x=x*mapScale/100;
	y=y*mapScale/100;
	y=y+20-mapMas[3]/2;*/
	
	if(coordinate.x<0){$("#arrow").hide();return;} else{$("#arrow").show();}
	var offset=$("#map").position();
	var x=offset.left+(coordinate.x*mapScale);
	var y=offset.top+($("#map").height()-coordinate.y*mapScale);
	//y=y+20-mapMas[3]/2;
	
	$("#arrow").offset({top:y,left:x});
	$("#arrow").css("transform","rotate(-"+coordinate.angle+"deg)");
}

var mapScale=0.5;
function startUpdate(){
	farseId=setInterval(farse, 500);
}
function stopUpdate(){
	clearInterval(farseId);
}
var mapMas=[];

function getMouse(e){
	var canvas = document.querySelector("canvas");
	var x = e.pageX - e.target.offsetLeft,
    y = e.pageY - e.target.offsetTop;
	x=x/mapScale;
	y=mapMas[0]-(y/mapScale);
	patrolDraw.push([x,y]);
	patrolRedraw();
}

function scaleMap(){
	$("#map").height(mapMas[0]*mapScale);
	$("#map").width(mapMas[1]*mapScale);
	$("#arrow").height(mapMas[2]*mapScale);
	$("#arrow").width(mapMas[3]*mapScale);
	
	var canvas = document.querySelector("canvas");
	var context = canvas.getContext("2d");
	canvas.width  = mapMas[1]*mapScale;
	canvas.height = mapMas[0]*mapScale; 
	$("#canvas1").offset($("#map").position());
	updateCoordinate();
	patrolRedraw();
	
//canvas.style.width  = '1000px';
//canvas.style.height = '800px';
	  /*var canvas = document.querySelector("canvas");
	  var context = canvas.getContext("2d");
	  context.fillStyle = "red";
	  context.fillRect(10, 10, 100, 50);*/
}

var patrolDraw=[];
function startDrawPatrol(){
	$("#pathStart").hide();
	$("#pathStop").show();
	$("#canvas1").click(getMouse);
	patrolDraw=[];
	patrolRedraw();
	$("#arrow").css("z-index",1);
}

function endDrawPatrol(){
	$("#pathStop").hide();
	$("#pathStart").show();
	$("#canvas1").unbind('click');
	$("#arrow").css("z-index",3);
	
	var len=patrolDraw.length;
	if (len<2){patrolDraw=[];patrolRedraw();console.log("short patrol path");return;}
	var endLen=Math.sqrt(Math.pow(patrolDraw[0][0]-patrolDraw[len-1][0],2)+Math.pow(patrolDraw[0][1]-patrolDraw[len-1][1],2));
	endLen=endLen*mapScale;
	if(endLen<20){
		if (len<3){patrolDraw=[];patrolRedraw();console.log("short patrol path");return;}
		console.log("near points");
		patrolDraw[len-1][0]=patrolDraw[0][0];
		patrolDraw[len-1][1]=patrolDraw[0][1];
		patrolRedraw();
	}
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"patrolPath" : JSON.stringify(patrolDraw),
			"device" : "robot1"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик 
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			patrolDraw=[];patrolRedraw();console.log("server refuse reqest");return;
		}
	});
}


function patrolRedraw(){
	var radius=8;
	if(mapScale<0.5){radius=radius*(mapScale*2);}
	var canvas = document.querySelector("canvas");
	var context = canvas.getContext("2d");
	context.fillStyle = "yellow";
	context.clearRect(0, 0, canvas.width, canvas.height);
	var oldX,oldY;
	for(var i=0 ; i< patrolDraw.length;i++){
		var ar = patrolDraw[i];
		context.beginPath();
		x=ar[0]*mapScale;
		y=(mapMas[0]-ar[1])*mapScale;
		if(oldX!=undefined){
			context.moveTo(oldX,oldY);
			context.lineTo(x,y);
		}
		context.stroke();
		oldX=x;oldY=y;
	}
	for(var i=0 ; i< patrolDraw.length;i++){
		var ar = patrolDraw[i];
		context.beginPath();
		x=ar[0]*mapScale;
		y=(mapMas[0]-ar[1])*mapScale;
		context.arc(x, y, radius, 0, 2*Math.PI, true);
		context.fill();
		context.stroke();
	}
	
}

function bigMap(){
	mapScale+=0.1;
	scaleMap();
}
function littleMap(){
	mapScale-=0.1;
	scaleMap();
}

function startPatrol(){
	$("#autoPatrolStart").hide();
	$("#autoPatrolStop").show();
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"patrol" : "start",
			"device" : "robot1"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик 
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			stopPatrol();
			console.log(data);
		}
	});
	
}

function stopPatrol(){
	$("#autoPatrolStop").hide();
	$("#autoPatrolStart").show();
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"patrol" : "stop",
			"device" : "robot1"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик 
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
		}
	});
}

function sendAction( s){
	$.ajax({
		url : "control/devices",
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"action" : s,
			"device" : "robot1"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик 
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
		}
	});
}


var camera ={
		nextState:function (){
			var action="";
			if(this.state==1){
				action="stopCamera";
				this.state=0;
			} else{
				action="startCamera";
				this.state=1;
			}
			
			sendAction( action);
		},
		nextCamera:function(){
			if(this.camera==1){
				$("#camera").attr("src","camera?device=robot1");
				this.camera=0;
			} else{
				$("#camera").attr("src","camera?device=robot1&direct=true");
				this.camera=1;
			}
		},
		closeCamera:function(){
			$("#camera").attr("src","null");
			this.camera=-1;
		},
		camera:-1,
		state:0
}
function printChart(){
	var n = document.getElementById("chartSelect").options.selectedIndex;
	var val = document.getElementById("chartSelect").options[n].value;
	 var win = window.open("chart.jsp?device=robot1&type="+val, '_blank');
	  win.focus();
}

function clickSubmenu(el){
	console.log(el.id);
	var id= el.id
	if (wbw != 0) {
		return;
	}
	wbw = 1;
	device=id;
	var parent = $(this).parents("section").first();
	// if($(this).attr("class")=="active"){return 0;}
	$(this).parent().children().removeAttr("class");
	$(this).attr("class", "active");
	$(parent).hide();
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "html",
		data : {
			"device" : device
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			console.log(data+"right");
			setConnectionStatus(true);
			//$("li").find('[data-submenu='+device+']').css("color","white");
			//insertJSON(parent, data);
			$(parent).show();
			startUpdate();
			// setTimeout(far, 900);
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			//$("li").find('[data-submenu='+device+']').css("color","red");
		}
	});
}


$(document).ready(
		function() {
			$("#autoPatrolStart").click(startPatrol);
			$("#changeCamera").click(camera.nextCamera);
			$("#changeCameraState").click(camera.nextState);
			$("#autoPatrolStop").click(stopPatrol);
			$("#pathStart").click(startDrawPatrol);
			$("#pathStop").click(endDrawPatrol);
			$("#mapBig").click(bigMap);
			$("#mapLittle").click(littleMap);
			$('#enableCamera').change(
				    function(){
				        if ($(this).is(':checked')) {
				        	camera.nextCamera();
				            $("#cameraView").show();
				            $("#cameraButtons").show();
				        }else{
				        	$("#cameraView").hide();
				        	$("#cameraButtons").hide();
				        	camera.closeCamera();}
				        if ($("#enableMap").is(':checked')){scaleMap();
				        }
				    });
			$('#enableMap').change(
				    function(){
				        if ($(this).is(':checked')) {
				        	var URL = "control/devices";
				        	$.ajax({
				        		async:false,
				        		url : URL,
				        		cache : false,// указываем URL и
				        		dataType : "json",
				        		data : {
				        			"patrolPath" : "get",
				        			"device" : "robot1"
				        		},// тип загружаемых данных
				        		success : function(data, textStatus) { // вешаем свой обработчик 
				        			patrolDraw=JSON.parse(data.path);
				        			
				        			patrolRedraw();
				        		},
				        		error : function(data, textStatus) { // вешаем свой обработчик на
				        			patrolDraw=[];patrolRedraw();
				        		}
				        	});
				        	
				        	
				        	$("#mapButtons").show();
				            $("#mapView").show();
				            if(!mapMas[0]>0){
				    			mapMas[0]=$("#map").height();
				    			mapMas[1]=$("#map").width();
				    			mapMas[2]=$("#arrow").height();
				    			mapMas[3]=$("#arrow").width();
				            }
				            scaleMap();
				        }else{$("#mapView").hide();$("#mapButtons").hide();}
				    });
			coordinate.x=-500;
			coordinate.y=-500;
			updateCoordinate();
			$("#control-mode").find("button#manual-mode").click(toManual);
			$("#control-mode").find("button#auto-mode").click(toAuto);
			$("#topmenu").find("button").click(topMenuClick);
			/*$("div#control").find("section#devices").find(".rightmenu").find(
					"li").click(rightMenuClick);*/
			$(".asidemenu").find("li").click(asideMenuClick);
			$("a").click(function(event) {
				event.preventDefault();
				alert("This hiperlink is unusable");
			});
			$("section").find("auto").find("button").click(moveFunction);
			var child1 = $("#block").children();
			var i = 0;
			$("#block").children().find(".asidemenu").children(":first-child")
					.trigger('click');
			while (i in child1) {
				child2 = $(child1[i]).children("section");
				currentCell[$(child1[i]).attr("id")] = $(child1[i]).children(
						"section").attr("id");
				i++;
			}
			$(".login-bar").find("button").click(function() {
				$.ajax({
					url : "login",
					method : "POST",// указываем URL и
					dataType : "text",
					data : {
						"action" : "logout"
					},// тип загружаемых данных
					success : function(data, textStatus) { // вешаем свой
						// обработчик на
						// функцию success
						$(location).attr('href', "login");
					},
					error : function(data, textStatus) { // вешаем свой
						// обработчик на
						// функцию success
						console.log("authError");
					}
				});

			});

			// загрузку HTML кода из файла example.html
			// $("#topmenu").load('topmenu');
			$("html").keydown(keyDown);
			$("html").keyup(keyUp);
			$("#topmenu").find("button").first().trigger('click');
			$("div#control").find("section#devices").find(".rightmenu").find(
					"li").click();
			//$("#control-mode").focusout(toAuto);
			$("#control-mode").find("#manual").find("button").unbind('click');
			$("#control-mode").find("#manual").find("button").bind("mousedown",
					manualModeControlDown);
			$("#control-mode").find("#manual").find("button").bind("mouseup",
					manualModeControlUp);
		});
/*
 * //function createTopMenu (data){
 * 
 * 
 * return html; });
 */
/*
function updateChart(){
	var ctx = document.getElementById("myChart").getContext('2d');

	var myChart = new Chart(ctx, {
		"type" : "line",
		"data" : {
			"labels" : [ "20:00","21:00","22:00","23:00","00:00","01:00","02:00" ],
			"datasets" : [ {
				"label" : "My First Dataset",
				"data": [20,15],
				"fill" : false,
				"borderColor" : "green",
				"lineTension" : 0.2
			} ]
		},
		"options" : {}
	});
	console.log("test");
}*/
var timer = {
	numKeys : 0,
	req : 0,
	intervalID : 0,
	intervalAjaxID : 0,
	status:0,
	keys : {},
	add : function(a) {
		if (timer.keys['a' + a] == null) {
			timer.keys['a' + a] = 1;
		} else {
			return 0;
		}
		timer.numKeys = timer.numKeys + 1;
		if (timer.numKeys >= 1&&timer.intervalID==0) {
			timer.intervalID = setInterval(timer.next, 100);
		}
	},
	del : function(a) {
		delete (timer.keys['a' + a]);
		timer.numKeys -= 1;
		if (timer.numKeys <= 0) {
			clearInterval(timer.intervalID);
			timer.intervalID=0;
			controlObj.move=0;
		}

	},
	next : function() {
		controlObj.move=0;
		if(status==0){return;}
		for (a in timer.keys) {
			if (a == 'a' + 87) {
				controlObj.move = 1;
			}
			if (a == 'a' + 83) {
				controlObj.move = 2;
			}
			if (a == 'a' + 65) {
				controlObj.turn += 10;
			}
			if (a == 'a' + 68) {
				controlObj.turn -= 10;
			}
			if (a == 'a' + 81) {
				controlObj.speed -= 10;
			}
			if (a == 'a' + 69) {
				controlObj.speed += 10;
			}
		}
		if (controlObj.speed > 100) {
			controlObj.speed = 100
		}

		if (controlObj.speed < 0) {
			controlObj.speed = 0
		}

		$("#manualControlSpeed").html(controlObj.speed);
		if (controlObj.turn > 50) {
			controlObj.turn = 50
		}

		if (controlObj.turn < -50) {
			controlObj.turn = -50
		}

		$("#manualControlTurn").html(controlObj.turn);

	},
	setManual:function(){
		$.ajax({
			url : "control/manual",
			cache : false,// указываем URL и
			dataType : "json",
			data : {
				"set" : "manual",
				"device" : "robot1"
			},// тип загружаемых данных
			success : function(data, textStatus) { // вешаем свой обработчик на
				if(data['status']=="ok"){status=1;
				timer.intervalAjaxID = setInterval(timer.aj, 100);
				}else{console.log("little error wit manual");toAuto();};
			},
			error : function(data, textStatus) { // вешаем свой обработчик на
				console.log("big error wit manual");
				toAuto();
			}
		});
	},
	aj : function() {
		if (timer.req != 0) {
			return 0;
		}
		timer.req = 1;
		$.ajax({
			url : "control/manual",
			cache : false,// указываем URL и
			dataType : "json",
			data : {
				"use" : "robot1",
				"move" : controlObj.move,
				"speed" : controlObj.speed,
				"turn" : 50-controlObj.turn,
			},// тип загружаемых данных
			success : function(data, textStatus) { // вешаем свой обработчик на
				// функцию success
				timer.req = 0;
			},
			error : function(data, textStatus) { // вешаем свой обработчик на
				// функцию success
				timer.req = 0;
			}
		});
	}
}

function keyDown(event) {
	if (mode != "manual") {
		return 0;
	}
	;
	var code = event.which;
	$("#control-mode").find("#manual").find("button#" + code).mousedown();
}
function keyUp(event) {
	if (mode != "manual") {
		return 0;
	}
	;
	var code = event.which;
	$("#control-mode").find("#manual").find("button#" + code).mouseup();
}
function manualModeControlDown() {
	if (mode != "manual") {
		return 0;
	}
	;
	$(this).css('border', '1px solid red');
	timer.add($(this).attr("id"));

}
function manualModeControlUp() {
	if (mode != "manual") {
		return 0;
	}
	;
	$(this).css('border', '0px');
	timer.del($(this).attr("id"));
}

function topMenuClick() {
	var block = $(this).data("block");
	$(this).parent().children().removeAttr("disabled");
	$(this).attr("disabled", "true");
	$(this).parent().children().removeAttr("class");
	$(this).attr("class", "active");
	$("#block").children().attr("hidden", true);
	$("#block").children("#" + block).attr("hidden", false);
	currentBlock = block;
	$($("#heading").children()[0]).html($(this).html());
	$($("#heading").children()[1]).html(
			" > "
					+ $("#block").children("#" + currentBlock).find(
							".asidemenu").children(".active").html());
}

function asideMenuClick() {
	var block = $(this).data("block");
	if (block == currentCell[currentBlock]) {
		return 0;
	}
	var section = $(this).parents("div").children("section#" + block);
	$(this).parent().children().removeAttr("disabled");
	$(this).attr("disabled", "true");
	$(this).parent().children().removeAttr("class");
	$(this).attr("class", "active");
	$(this).parents("div").children("section").hide();
	if ($(section).data("ajax") == "html") {
		loadBlock(section);
	}
	if ($(section).data("ajax") == "json") {
		updateBlock(section);
	}
	$(section).show();
	if (typeof (currentBlock) == "string") {
		currentCell[currentBlock] = block;
	}
	$($("#heading").children()[1]).html(" > " + $(this).html());
}
var wbw = 0;

/*
 * function far(){ var src=$("#camera").data("ip")+'?'+Math.random();
 * $("#camera").attr("src",src); setTimeout(far,900); }
 */



function farse() {
	// console.log("update");
	device="robot1";
	var URL = "control/devices";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		data : {
			"device" : device
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			setConnectionStatus(true);
			$("li").find('[data-submenu='+device+']').css("color","white");
			//insertJSON(parent, data);;
			coordinate.x=data.x;
			coordinate.y=data.y;
			coordinate.angle=data.angle;
			updateCoordinate();
			$("#updGO").html(data.updGO);
			$("#updPA").html(data.updPA);
			$("#updTB").html(data.updTB);
			$("#updVV").html(data.updVV);
			$("#coordinatex").html(data.x);
			$("#coordinatey").html(data.y);
			$("#coordinateangle").html(data.angle);
			$("#temp1").html(data.temp1);
			$("#temp2").html(data.temp2);
			$("#robot-status").html(data.status);
			if(data.log.length>0){
			var copy1 = $("#log").children().first().clone();
			$("#log").html(" ");
			for (var i = 0; i < data.log.length; i++) {
				$("#log").prepend($(copy1).html(data.log[i]));
				copy1 = copy1.clone();
			}
			}
			//console.log(data==$("#coordinate1").html());
			//console.log(data);
			//$(parent).show();
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			$("#coordinate1").html(data);
			$("li").find('[data-submenu='+device+']').css("color","red");
		}
	});
}
function setConnectionStatus(a){
	if(a){
		$("a#login").css("color","white");
	}else{$("a#login").css("color","red");}
}
function moveFunction() {
	var block = $(this).data("block");
	var URL = "control/devices";
	var device="robot1";
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "text",
		data : {
			"move" : block
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success

			$.ajax({
				url : URL,
				cache : false,// указываем URL и
				dataType : "json",
				data : {
					"device" : device
				},// тип загружаемых данных
				success : function(data, textStatus) { // вешаем свой
					// обработчик на функцию
					// success
					console.log(data);
					//insertJSON($("div#control").find("section#devices"), data);
					$(parent).show();
					setConnectionStatus(true);
					$("li").find('[data-submenu='+device+']').css("color","white");
				},
				error : function(data, textStatus) { // вешаем свой
					// обработчик на функцию
					// success
					$("li").find('[data-submenu='+device+']').css("color","red");
				}
			});

		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			$("li").find('[data-submenu='+device+']').css("color","red");
		}
	});
}

function updateBlock(block) {
	var URL = $(block).parents("div").first().attr("id") + "/"
			+ $(block).attr("id");
	$.ajax({
		url : URL,
		cache : false,// указываем URL и
		dataType : "json",
		// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			// $("div#control").find("section#devices").html(data);
			//insertJSON(block, data);
			//$("#coordinate").html(data);
			//console.log(data);
			// $("#wrapper1").attr("hidden",false);
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			console.log("error for update element");
			console.log(URL);
			console.log(data);
		}
	});
	$(block).data("ajax","null");
}

function loadBlock(block) {
	var URL = $(block).parents("div").first().attr("id") + "/"
			+ $(block).attr("id");
	$.ajax({
		url : URL,
		method : "POST",// указываем URL и
		dataType : "html",
		data : {
			"answer" : "html"
		},// тип загружаемых данных
		success : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			// $("div#control").find("section#devices").html(data);
			$(block).html(data).data("ajax", "json");
			updateBlock(block);
			// $("#wrapper1").attr("hidden",false);
		},
		error : function(data, textStatus) { // вешаем свой обработчик на
			// функцию success
			console.log("error for load element");
			console.log(URL);
			console.log(data);
		}
	});
}

function insertJSON(node, JSON) {
	if (!Object.keys(JSON).length > 0) {
		return 0;
	}
	for ( var id in JSON) {
		if (typeof (JSON[id]) != "object") {
			$(node).find("#" + id).html(JSON[id]);
		} else if ("length" in JSON[id]) {
			var copy = $(node).find("#" + id).children().first().clone();
			$(node).find("#" + id).html(" ");
			for (var i = 0; i < JSON[id].length; i++) {
				$(node).find("#" + id).append($(copy).html(JSON[id][i]));
				copy = copy.clone();
			}
		} else {
			insertJSON($(node).children("#" + id), JSON[id]);
		}

	}
}
var mode = "auto";
var controlObj = {
	speed : 100,
	turn : 0,
	move : 0
};
function toManual() {
	console.log("man");
	timer.setManual();
	mode = "manual";
	$("#control-mode").find("#auto").hide();
	$("#control-mode").find("#manual").show();
	$("#control-mode").find("button#auto-mode").show();
	$("#control-mode").find("#manual").show();
	$("#control-mode").find("button#manual-mode").hide();
}
function toAuto() {
	console.log("auto");
	clearInterval(timer.intervalAjaxID);
	mode = "auto";
	$("#control-mode").find("#manual").hide();
	$("#control-mode").find("#auto").show();
	$("#control-mode").find("button#auto-mode").hide();
	$("#control-mode").find("button#manual-mode").show();
}
/*
 * json {key: element} element {key: string} element {key: element} elenemt
 * {[element],[element],[element]}
 */
