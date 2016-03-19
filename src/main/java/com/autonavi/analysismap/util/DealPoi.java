package com.autonavi.analysismap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.service.PoiNameFilterInter;
@Service
public class DealPoi {
	@Autowired
	private PoiNameFilterInter poiNameFilter;
	Logger log =  Logger.getLogger(getClass());
	// 解析poi原始数据
	public Collection<String> filterPoi(String originalPoiStr){
		try {
			JSONArray originalPoiArr = JSONArray.fromObject(originalPoiStr);
			Collection<String> uniqPoiNames = new HashSet<String>();
			for(int i = 0, oriPoiSize = originalPoiArr.size(); i < oriPoiSize; i++){
				JSONObject oriPoiJson = JSONObject.fromObject(originalPoiArr.get(i));
				JSONArray poiNameArr = oriPoiJson.getJSONArray("uids");
				for(int m = 0; m< poiNameArr.size() ; m ++) {
					String poiName = poiNameArr.getJSONObject(m).getString("name");
					uniqPoiNames.add(poiNameFilter.delStuffStringByRegex(poiName));
				}
			}
			ArrayList<String> arrayList = new ArrayList<String>(uniqPoiNames);
			Collections.sort(arrayList);
			return arrayList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
}
////					uniqPoiNames.add(doit(filters.size() - 1, poiName, filters));
//List<PoiNameFilter> filters = Arrays.asList(new PoiNameFilter[]{
//		new PoiNameBuildingFilter(),
//		new PoiNameBuilding2Filter(),
//		new PoiNameRoadFilter(),
//		new PoiNameSymbalFilter()
//});

//
//private String doit(int idx, String poiName,List<PoiNameFilter> filters) {
//	if (idx < 0) {
//		return poiName;
//	}
//	return doit(idx - 1, filters.get(idx).delStuffStringByRegex(poiName),filters);
//}
//
