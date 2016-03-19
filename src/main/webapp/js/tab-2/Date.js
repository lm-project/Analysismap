
define(function(){
	
	
	
	function getGeoCoder(){
		return  new Date();
	}

	
	function getTime(){
		//返回 1970 年 1 月 1 日至今的毫秒数。
		var date = getGeoCoder();
		var getMillisecond = date.getTime();
		console.log("1970 年 1 月 1 日至今的毫秒数:"+getMillisecond);
		return getMillisecond;
	}
	
	return {getTime:getTime};
	
})
