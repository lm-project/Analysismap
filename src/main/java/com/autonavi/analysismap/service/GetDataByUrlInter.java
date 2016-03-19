package com.autonavi.analysismap.service;

import java.util.Collection;
import java.util.Map;

import com.autonavi.analysismap.entity.GrabArgsInfo;

/**
 * 
 *抓取数据接口
 *1.抓取Poi------------------------------getPoiByUrl
 *2.抓取GEOM-----------------------------getGeomByUrl
 * @author zhentao.liu
 *
 */
public interface GetDataByUrlInter {
	/**
	 * 
	 * @param boxTile   城市包含的box和Tile的json字符串
	 * @param key       检索到的关键字
	 * @param level     检索的级别
	 * @param city      城市名称
	 * @param path      保存数据的路径
	 * @return
	 */
	public Map<Collection<String>, Integer> getPoiByUrl(String boxTile,GrabArgsInfo gai) throws InterruptedException ;
	public Map<String, String> getPolygonByUrl(String mapType, Collection<String> uniqPoiNames);
}
