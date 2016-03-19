package com.autonavi.analysismap.service.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.service.GetDataByUrlInter;
import com.autonavi.analysismap.service.PolygonCacheService;
import com.autonavi.analysismap.util.GetAmapresult;
import com.autonavi.analysismap.util.GetBaiduMapresult;
import com.autonavi.analysismap.util.GetPoiByUrl;
import com.autonavi.analysismap.util.GetQQMapresult;
/**
 * 实现抓取数据
 * 1.抓取poi---------------getPoiByUrl
 * 2.抓取geometry ---------getGeomByUrl
 * @author zhentao.liu
 *
 */

@Service
public class GetDataByUrlImpl implements GetDataByUrlInter {
	@Autowired
	private GetPoiByUrl getPoi;
	@Autowired
	private PolygonCacheService polygonCache;
	@Override
	public Map<Collection<String>, Integer>  getPoiByUrl(String boxTile, GrabArgsInfo gai) throws InterruptedException {
		 return getPoi.getPoiByBoxTile(boxTile, gai);
	}
	@Override
	public Map<String, String> getPolygonByUrl(String mapType, Collection<String> uniqPoiNames) {
		
		if(mapType.equals("baidu")){
			
			return polygonCache.getPolygonByPoiName(new GetBaiduMapresult(), uniqPoiNames);
		}else if(mapType.equals("qq")){
			
			return polygonCache.getPolygonByPoiName(new GetQQMapresult(), uniqPoiNames);
		}else{
			
			return polygonCache.getPolygonByPoiName(new GetAmapresult(), uniqPoiNames);
		}
	}

}
