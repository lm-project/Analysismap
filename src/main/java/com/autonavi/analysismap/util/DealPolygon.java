package com.autonavi.analysismap.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
/**
 * 处理抓取的坐标（坐标转化）
 * 
 * @author zhentao.liu
 *
 */
@Service
public class DealPolygon {
	Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 * @param polygonJsonStr  抓取的poi_polygon 字符串
	 * @param mapType         地图类型
	 * @return                map<poi,处理后的polygon>
	 */
	public Map<String, String> dealPolygonMain(String polygonJsonStr, String mapType){
		try {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject polygonJson = mapType.equals("baidu") ? 
					JSONObject.fromObject( GetGeometry.getBaiduLngLat(polygonJsonStr)):
						JSONObject.fromObject( polygonJsonStr );
			JSONArray polygonNames  = polygonJson.names();
			for(int i = 0, l = polygonNames.size(); i<l; i++){
				String name = (String) polygonNames.get(i);
				final boolean isSecond = mapType.equals("qq")? false:true;
				final boolean isReverse = false;
				final String strGeo = (String) polygonJson.get(name);
				String geom = GetGeometry.getGeometry(strGeo, isReverse, GetGeometry.POLYGON, isSecond);
				map.put(name, geom);
			}
			return map;
		} catch (Exception e) {
			log.debug("dealPolygonMain错误"+e);
			return null;
		}
		
		
		
	}
	
}
