var sault=null;
$(document).ready(function(){  
	$("#loginbutton").click(loginFunction);
}
);

function loginFunction(){
	console.log("start");
	var login=$("#loginfield").val();
	var password=$("#passwordfield").val();
	$.ajax({
		    url: "login",    
		    method: "POST",// указываем URL и
		    dataType : "json",
		    data:  { "login": login,"pass":password },// тип загружаемых данных
		    success: function (data, textStatus) { // вешаем свой обработчик на функцию success
		    	console.log("all ok");
		    	console.log(data);
		    	if(data["status"]=="ok"){
		    		console.log("no error");
		    		$(location).attr('href',"home");
		    	}else{
		    		console.log("with error");
		    		console.log(data[status]);
		    	}
		    },
			error: function (data, textStatus) { // вешаем свой обработчик на функцию success
			console.log("loadError");
			console.log(data);
		    }
	});
	
}

function requestSault(){
	
}