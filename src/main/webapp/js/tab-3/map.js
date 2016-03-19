// tab-3/map.js['msg']
define(['msg'],function (msg){
	  var map;
	  var bdary;
	  var init = function (){
		map = new BMap.Map("container");
		bdary = new BMap.Boundary();
		map.centerAndZoom(new BMap.Point(116.403765, 39.914850), 5);  
		var stdMapCtrl = new BMap.NavigationControl({type: BMAP_NAVIGATION_CONTROL_SMALL})  
		var top_left_navigation = new BMap.NavigationControl();  // 左上角，添加默认缩放平移控件
		map.addControl(top_left_navigation);  
		map.enableScrollWheelZoom();  
		map.enableContinuousZoom();  
		setTimeout(function(){
			   map.enableDragging();   // 两秒后开启拖拽
			   // map.enableInertialDragging(); //两秒后开启惯性拖拽
		}, 2000);
		
		map.clearOverlays();
		// 示例数据
		var datas = new Array("湖南-#ff0000","湖北-#ff5500","江西-#ffff00","重庆-#00ff00","贵州-#00ff55");  
		for(var i=0;i<datas.length;i++){  
		    getBoundary(datas[i]);  
		}
	  };

	  var getBoundary = function(data){  
		    if(data.indexOf("-") == -1) {
		    	data += "-#ff0000";
		    }
		    var province = data.split("-")[0];
		    bdary.get(province, function(rs){  
		    	if(rs.boundaries.length == 0){
		    		msg.info('未找到改城市!', 'error' , '#tab-3');
		    	}else {
		    		msg.info('搜索成功!', 'succes' , '#tab-3');
		    	}
		        var bounds;  
		        var maxNum = -1, maxPly;  
		          
		        var count = rs.boundaries.length;   
		        for(var i = 0; i < count; i++){  
		            var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 1, strokeOpacity:0.5,fillColor:data.split("-")[1],strokeColor: "#000000"});  
		            map.addOverlay(ply);  
		            var arrPts = ply.getPath();
		            if(arrPts.length > maxNum){  
		                maxNum = arrPts.length;  
		                maxPly = ply;  
		            }  
		        }  
		       
		       if(maxPly){
		    	    console.log(province);
		    	    var bounds = maxPly.getBounds();
		    	    var GD = bounds.Gd;
		    	    var FD = bounds.Fd;
		    	    var KD = bounds.Kd;
		    	    var JD = bounds.Jd;
		    	    var content =  $('#result-3').val();
		    	    content += '\r\n'+province +':'+ GD +','+ FD +';'+ KD +','+ JD;
		            $('#result-3').val(content)
		            map.setCenter(new BMap.Point(GD, FD))
		            // map.setViewport(maxPly.getPath());
		        }
		    });  
	  }
	  return {
		init: init,
		getBoundary : getBoundary
	  };
});