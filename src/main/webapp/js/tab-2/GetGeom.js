
define(["tab-2/Ajax"],function(ajax){
	
	function getGeom(requestArgs){
		/* 请求服务器  并处理返回数据*/
		ajax.post("getGeom", requestArgs);
	}
	
	return getGeom;
})