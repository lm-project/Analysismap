package com.autonavi.analysismap.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
/**
 * 1.将城市拆分为若干box，以json形式返回-----------------spliteBdBox
 * 2.把每个box拆分为若干瓦片，以json形式返回--------------splitBoxToTile
 * @author zhentao.liu
 *
 */
@Service
public class CalculateBdBoxTile {
	private  Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 * @param cityMaxMinPoint  城市的最大最小平面经纬度
	 * @param level            城市级别
	 * 18级下box经度（平面坐标）之间的差为1015
	 * 18级下box纬度（平面坐标）之间的差为521
	 * 其他级别box之间的差是18级下2的（18-level）倍，
	 *    如：15级，box之间的差为：
	 *    横向：1015*2^(18-15)
	 *    纵向：521*2^（18-15）
	 * @return
	 */
	public  String spliteBdBox( GrabArgsInfo gai ){
		JSONObject boundary = gai.getBoundary();
		final double CITY_MIN_LNG = boundary.getDouble("cityMinLng");// 城市最小经度（以平面坐标显示）
		final double CITY_MIN_LAT = boundary.getDouble("cityMinLat");// 城市最小纬度（以平面坐标显示）
		final double CITY_MAX_LNG = boundary.getDouble("cityMaxLng");// 城市最大经度（以平面坐标显示）
		final double CITY_MAX_LAT = boundary.getDouble("cityMaxLat");// 城市最大纬度（以平面坐标显示）
		double bigZoom = Math.pow(2, 18 - gai.getLevel());	
		log.debug("城市的边界顶点坐标:"+CITY_MIN_LNG+","+CITY_MIN_LAT+";"+CITY_MAX_LNG+","+CITY_MAX_LAT);
		String box = "";
		int boxSize = 0;
		try {
			final double box_xx = 1015 * bigZoom;
			final double box_yy = 521 * bigZoom;
			for(double bx = CITY_MIN_LNG; bx < CITY_MAX_LNG; bx = bx+box_xx){
				double bxx = (bx+box_xx) > CITY_MAX_LNG ? CITY_MAX_LNG : (bx+box_xx);
				for(double by = CITY_MIN_LAT; by < CITY_MAX_LAT; by = by+box_yy){
					double byy = (by+box_yy) > CITY_MAX_LAT ? CITY_MAX_LAT : (by+box_yy);
					box += "\""+(String.format("%20.2f", bx)+","+String.format("%20.2f", by)+";"
				          +String.format("%20.2f", bxx)+","+String.format("%20.2f", byy)).replace(" ", "")+"\",";
					boxSize++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("拆分为box的数量："+boxSize);
		return "["+box.substring(0,box.length()-1)+"]";
	} 
	
	/*
	 * 解析box
	 * 计算每个box瓦片的最大最小坐标
	 * 
	 */
	/**
	 * @param boxStr           城市所有的box json字符串
	 * @param level            级别
	 *瓦片的坐标 =平面坐标/2^（18-level）/256
	 * 一个box包含5*3个瓦片
	 * 计算每个box瓦片的最大最小，循环输出，得到所有的box
	 * @return
	 */
	public  String splitBoxToTile(String boxStr, GrabArgsInfo gai){
		String tileJsonText = "";
		try {
				double pixel = Math.pow(2, 18 - gai.getLevel());	
				JSONArray boxJson = JSONArray.fromObject(boxStr);				
				for(int i = 0, t = boxJson.size(); i<t; i++){
					String boxMinMaxStr = boxJson.get(i).toString();
					String[] boxMinMaxLngLat = boxMinMaxStr.split(";");
					String[] boxMinLngLat = boxMinMaxLngLat[0].split(",");
					String[] boxMaxLngLat = boxMinMaxLngLat[1].split(",");
					int MIN_TILE_X = (int) Math.floor(Double.parseDouble(boxMinLngLat[0])/pixel/256);
					int MIN_TILE_Y = (int) Math.floor(Double.parseDouble(boxMinLngLat[1])/pixel/256);
					int MAX_TILE_X = (int) Math.floor(Double.parseDouble(boxMaxLngLat[0])/pixel/256);
					int MAX_TILE_Y = (int) Math.floor(Double.parseDouble(boxMaxLngLat[1])/pixel/256);
					MAX_TILE_Y = MIN_TILE_Y == MAX_TILE_Y ? MAX_TILE_Y+1 : MAX_TILE_Y;
					MAX_TILE_X = MIN_TILE_Y == MAX_TILE_X ? MAX_TILE_X+1 : MAX_TILE_X;
					tileJsonText += getTileByBox(boxMinMaxStr,MIN_TILE_X,MIN_TILE_Y,MAX_TILE_X,MAX_TILE_Y);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		log.info("拆分box为Tile完毕！");
		return "["+tileJsonText.substring(0,tileJsonText.length()-1)+"]";
	}
	//根据最大最小瓦片循环输出每个瓦片
	private  String  getTileByBox(String boxMinMaxStr, int MIN_TILE_X, int MIN_TILE_Y,
			int MAX_TILE_X, int MAX_TILE_Y) {
		     String box_tile_Str = "{\"boxXY\":\""+boxMinMaxStr+"\",\"tileXY\":[";
			 for(int j = MIN_TILE_Y; j < MAX_TILE_Y; j++){//tile_y
				 for(int i = MIN_TILE_X; i < MAX_TILE_X; i++){
					 box_tile_Str += "\""+i+"_"+j+"\",";
				 }
			 }
			 return box_tile_Str.substring(0, box_tile_Str.length()-1)+"]},";
	}
	
	
}
