
define(["tab-2/getDistrictBoundary","tab-2/Ajax"],function(getDistrictBoundary,ajax){
	
	function getPoi(requestArgs){
		/* 获取城市的box */
		getDistrictBoundary(requestArgs.district,function( cityBoxArr ){
			requestArgs.boundary = cityBoxArr;
			/* 请求服务器  并处理返回数据*/
			ajax.post("getPoi", requestArgs);
		});
	}
	
	return getPoi;
})
