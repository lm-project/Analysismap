
define(function(){
	/**
	 * 
	 * 创建行政区域边界范围对象
	 */
	var districtSearch = null;
	function createBoundary(){
		if ( districtSearch === null ) {
			districtSearch = new BMap.Boundary();
		}
		
	}
	
	/**
	 * 
	 * 计算边界城市范围最大最小值
	 */
	function getDistrictBoundary(district,fun){
		var MAX_LNG = 0,MAX_LAT = 0,MIN_LNG = 180,MIN_LAT = 100;
		districtSearch.get(district,function(bounds){
			for(var i in bounds){
				var boundsArr = bounds[i];	
				var boundsStr = boundsArr.toString();
				if(boundsStr===""){alert("你输入的城市名不存在box！");return;}
				var boundsStrArr = boundsStr.split(";");
				var l = boundsStrArr.length;
				for (var j=0;j<l;j++){
					var boundsMath = boundsStrArr[j].split(",");
					MAX_LNG = MAX_LNG < parseFloat(boundsMath[0]) ? parseFloat(boundsMath[0]) : MAX_LNG;
					MAX_LAT = MAX_LAT < parseFloat(boundsMath[1]) ? parseFloat(boundsMath[1]) : MAX_LAT;
					MIN_LNG = MIN_LNG > parseFloat(boundsMath[0]) ? parseFloat(boundsMath[0]) : MIN_LNG;
					MIN_LAT = MIN_LAT > parseFloat(boundsMath[1]) ? parseFloat(boundsMath[1]) : MIN_LAT;
				}
			}
			fun(MAX_LNG, MAX_LAT, MIN_LNG, MIN_LAT);
		});
	}
	/**
	 * 
	 *百度球面坐标转平面坐标
	 */
	function lngLatToPlane(cityLeftDownLngLat,cityRrightUpLngLat){
		var projection = new BMap.MercatorProjection();
		var cityMinLngLat = projection.lngLatToPoint(cityLeftDownLngLat);
		var cityMaxLngLat = projection.lngLatToPoint(cityRrightUpLngLat);
		var cityBoxArr = {
				cityMinLng: cityMinLngLat.x,
				cityMinLat: cityMinLngLat.y,
				cityMaxLng: cityMaxLngLat.x,
				cityMaxLat: cityMaxLngLat.y};
		console.log("城市左下角平面坐标:"+cityMinLngLat.x+","+cityMinLngLat.y);
		console.log("城市右下角平面坐标:"+cityMaxLngLat.x+","+cityMaxLngLat.y);
		return cityBoxArr;
	}
	function  getCityScope(district,fun){
		console.log("获取box的城市名："+district);
		createBoundary();
		getDistrictBoundary(district,function(MAX_LNG, MAX_LAT, MIN_LNG, MIN_LAT){
			console.log(district+"最小坐标："+MIN_LNG+","+MIN_LAT+";最大坐标："+MAX_LNG+","+MAX_LAT);
			var cityLeftDownLngLat = new BMap.Point(MIN_LNG,MIN_LAT);
			var cityRrightUpLngLat = new BMap.Point(MAX_LNG,MAX_LAT);
			var cityBoxArr = lngLatToPlane(cityLeftDownLngLat,cityRrightUpLngLat);
			fun(cityBoxArr);
		});
	}  
	
	return getCityScope;
	
})
