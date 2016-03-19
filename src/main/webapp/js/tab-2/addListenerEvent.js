
define(["tab-2/getWebData"],function(getWebData){
	$("#t2_grabBnts").on("click","input",function(){
		getWebData(this);
	})
})
