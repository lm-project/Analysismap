
define(["tab-2/Date"],function(date){
	
	var requestArgs = {},url = "";
	function reRequest(data){
    	if(confirm(data)){
    		requestArgs.noUpdate = false;
    		post( url , requestArgs );
		 }else{
			 $("#info").html("");
			 return ;
		 }
    }
	var str = '<table cellspacing="0px"><tr><th rowspan="2" >Id</th><th colspan="2">POI</th><th colspan="2">FilterPoi</th><th colspan="2">GEOM</th><th colspan="2">ConvertGeom</th></tr>'+
               '<tr><th>抓取日期</th><th>数量</th><th>抓取日期</th><th>数量</th><th>抓取日期</th><th>数量</th><th>抓取日期</th><th>数量</th></tr>';
	function resoleJson (content,file){
		var poi = content.poi;
		var filerPoi = content.filterPoi;
		var polygon = content.polygon;
		var convertPolygon = content.convertPolygon;
		var num = 0,date= "";
		for(var m in poi){
			num = m;
			date = poi[m];
			console.log(m+";"+poi[m]);
		}
		for(var m in filerPoi){
			num = m;
			date = filerPoi[m];
			console.log(m+";"+filerPoi[m]);
		}
		for(var m in polygon){
			num = m;
			date = polygon[m];
			console.log(m+";"+polygon[m]);
		}
		for(var m in convertPolygon){
			num = m;
			date = convertPolygon[m];
			console.log(m+";"+convertPolygon[m]);
		}
		str += '<tr><td>'+file+'</td><td>'+num+'</td><td>'+date+'</td><td>'+num+'</td><td>'+date+'</td><td>'+num+'</td><td>'+date+'</td><td>'+num+'</td><td>'+date+'</td><tr>';
	}
    function showBackInfo(data){
    	var code = data.code;
    	var description = data.description;
    	var restring = data.restring;
    	if(code == 100 ||code == 300 ) {//１００抓取成功　３００此条数据正在运行,不能执行此操作
    		if(restring!=null){
    			var jsonArr = jQuery.parseJSON(restring);
    			for(var key in jsonArr){
    				var files = jsonArr[key];
    				for(var file in files){
    					console.log("fileName:"+ file);
    					resoleJson(files[file],file);
//    					$("#taskTemplate").load(path + "/js/tab-2/template/fileInfoShow.htm",
//						function(datas) {
//							var scriptTemplate = Handlebars.compile(datas);
//							$("#checkResult").html(scriptTemplate(data));
//						});
    				}
    			}
    			$("#checkResult").html(str+"</table>");
    		}
    		$("#info").html(description);
    		alert(description);
    		return;
		}
    	if(code == 200){//确定在此处理？
    		reRequest(description);
    	}
    	if( code == 400){//程序出错
    		console.log("请求错误，错误为："+description);
    		alert("请求出错！");
    	}
    }
	
	
	var i = 0;
    function showInfo(){
    	i%6 === 0 ? $("#info").html("正在处理") : $("#info").append(".");
    	i++;
    }
	
	function post( urlStr , arguments){
		url = urlStr;
		requestArgs = arguments;
		console.log("请求的参数："+requestArgs.key+","+requestArgs.district+","+requestArgs.level+","+
				requestArgs.mapType+","+requestArgs.noUpdate+","+requestArgs.boundary);	
		var clearSetTime = setInterval(showInfo,2000);
		var requestBeforeTime = date.getTime();
		$.post( path+"/"+url,
				{requestArgs: $.toJSON(requestArgs)},
				function( data ){
					window.clearInterval(clearSetTime);
					var timeDiff = Math.floor((date.getTime() - requestBeforeTime)/1000);
					console.log("请求服务器返回的信息："+data.code+","+data.restring+","+data.description+";耗时："+timeDiff+"秒");
					showBackInfo(data);
				});
		
	}
	return {post:post};
})
