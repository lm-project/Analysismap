
define(function(){
	
	
	var geocoder = null;
	
	function getGeoCoder(){
		geocoder = geocoder == null ? new BMap.Geocoder() : geocoder;
		return geocoder;
	}

	
	function getLocation(point, fun){
		getGeoCoder();
		geocoder.getLocation(point, function(rs){
			var addComp = rs.addressComponents;
			console.log("addressComponents(逆地址解析信息；)："+addComp.province+","+addComp.city+","+addComp.district);
			var addressComponents = { 
					province : addComp.province,
					city : addComp.city,
					district : addComp.district
			};
			fun(addressComponents);
		}); 
	}
	
	function getPoint( address, fun){
		getGeoCoder();
		geocoder.getPoint(address, function(point){
			console.log("地址解析坐标:"+point);
			fun( point );
		}, "全国");
		
	}
	
	return {getPoint:getPoint,getLocation:getLocation};
	
})
