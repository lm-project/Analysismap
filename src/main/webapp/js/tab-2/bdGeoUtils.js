
var projection = null;
function initialize(){
	var baiduMap = new BMap.Map("container");
	baiduMap.centerAndZoom(new BMap.Point(121.491, 31.233), 11);  
	projection = baiduMap.getMapType().getProjection();
}

window.onload = initialize;

function geotoMct(mctXX, mctYY){
	var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	var mctXY = new BMap.Pixel(mctXX,mctYY);   
	var LngLat = projection.pointToLngLat(mctXY);
	lng = LngLat.lng, lat = LngLat.lat;
	var x = lng - 0.0065, y = lat - 0.006;
	var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	lng = z * Math.cos(theta);
	lat = z * Math.sin(theta);
	var lngs = Math.abs(parseFloat(lng) * 3600);
	var lats = Math.abs(parseFloat(lat) * 3600);
	return lngs+","+lats+ ",";
}
function batchGeotoMct( context ){
	var json = JSON.parse( context );
	var res = '{';
	for(key in json){
		var rs ='"'+key+'":"'
		var array = json[key].split(",");
		for(var  j = 0 ; j < array.length-1 ; j=j+2 ){
			rs += geotoMct(array[j], array[j+1]);
		}
		res += rs.substr(0, rs.length-1)+'",';
		
	}
	return res.substr(0, res.length-1)+'}';
}

