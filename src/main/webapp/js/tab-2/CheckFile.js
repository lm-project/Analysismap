
define(["tab-2/Ajax"],function(ajax){
	
	function checkFile( requestArgs ){
		/* 请求服务器  并处理返回数据*/
		ajax.post("checkFile", requestArgs);
	}
	
	return checkFile;
	
})
