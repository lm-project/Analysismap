
define(function(){
	if(typeof(BMap) == "undefined"){
		alert("数据加载失败，请重新加载！");
		return ;
	}
	var ac = new BMap.Autocomplete({
		"input": "cityInput",
		"types" : "city"
	});
	ac.addEventListener("onhighlight", function(e){
		if (e.toitem.index > -1) {
			var _value = e.toitem.value;
			var value = _value.province +  _value.city +  _value.district;
			$("#cityAutoComplete").html(value);
		}    
		
	});
})
