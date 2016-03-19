
define(["tab-2/bdGeocoder","tab-2/GetPoi","tab-2/FilterPoi",
        "tab-2/GetGeom","tab-2/ConvertGeomAndInsertDB","tab-2/ExcuteAll","tab-2/CheckFile"],
		function(bdGeocoder,getPoi,filterPoi,getGeom,convertGeomAndInsertDB,excureAll,checkFile){
	
	var requestArgs = {};
    
	
	function whichBntClickEvent( whichBnt, finalCity ){
		var bntType = whichBnt.value;
		requestArgs.district = finalCity;
		bntType === "抓取POI" ?  getPoi( requestArgs ) : 
			bntType === "过滤POI" ? filterPoi( requestArgs ) : 
				bntType === "抓取Geom" ? getGeom( requestArgs )  : 
					bntType === "Geom坐标转换及入库" ? convertGeomAndInsertDB( requestArgs ) : 
						bntType === "综合执行" ? excureAll( requestArgs ) : checkFile(requestArgs); 
	}
	
	function getFinalCity( address,fun ){
		bdGeocoder.getPoint( address, function( point ){
			!point ? alert("城市输入有误！") :
				bdGeocoder.getLocation(point,function( addComp ){
					var province = addComp.province;
				    var city = addComp.city ;
				    var district = addComp.district;
					var finalCity = province === city && city.indexOf(address) ===0 ? 
							city :city.indexOf(address)===0 ? 
								province+"/"+city :  
							district.indexOf(address) ===0 && province === city ?
								city+"/"+district :
							district.indexOf(address) ===0 ? 
								province+"/"+city+"/"+district : "" ;
					console.log("最终的城市："+finalCity);
					fun( finalCity );
				});
		});
	}
	
	
	function checkInputInfo(){
		requestArgs.key   = $("#keyInput").val();
		requestArgs.level = $("#levelInput").val();
		requestArgs.mapType = $("#mapRadio input:radio:checked").val();
		requestArgs.noUpdate = true;

		var city = $("#cityInput").val();
		
		console.log("表单参数："+requestArgs.key+","+city+","+requestArgs.level+","+
				requestArgs.mapType);		
		return requestArgs.mapType === undefined ? "地图商没有选中！": 
			requestArgs.key === "" ? "请输入关键字！" :
				city ==="" ? "请输入城市名！" : city;
	}
	/**
	 * 
	 * tab-2 抓取数据点击按钮主入口方法
	 */
	function getWebData(whichBnt){
		/* 验证输入数据*/
		var city = checkInputInfo() ;
		/* 获取唯一的城市名 */
		city === "地图商没有选中！" || city === "请输入关键字！" || city === "请输入城市名！" ? alert(city) : 
			getFinalCity( city,function( finalCity ){
				
				finalCity === "" ?  alert("城市输入有误！"):!confirm("确定是"+finalCity+"?") ? false : 
					/* 执行哪个按钮的操作 */
					whichBntClickEvent( whichBnt, finalCity );
		});
	}
	return getWebData;
})