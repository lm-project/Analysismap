package com.autonavi.analysismap.service.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.service.DealDataInter;
import com.autonavi.analysismap.util.DealPoi;
import com.autonavi.analysismap.util.DealPolygon;
@Service
public class DealDataImpl implements DealDataInter {

	@Autowired 
	private DealPoi dealPoi;
	@Autowired 
	private DealPolygon dealPolygon;
	@Override
	public Collection<String> dealPoi(String poiOriginalStr) {
		
		return dealPoi.filterPoi(poiOriginalStr);	
	}

	@Override
	public Map<String, String> dealPolygon(String polygonJsonStr,String mapType) {
		
		return dealPolygon.dealPolygonMain(polygonJsonStr, mapType);
	}

}
