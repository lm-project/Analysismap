
define(["tab-2/Ajax"],function(ajax){
	
	function filterePoi( requestArgs ){
		/* 请求服务器  并处理返回数据*/
		ajax.post("filterPoi", requestArgs);
	}
	
	return filterePoi;
})
