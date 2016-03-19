package com.autonavi.analysismap.service;

import java.util.Collection;
import java.util.Map;
/**
 * 处理数据的接口
 * 过滤抓取的poi-----------------------------------------dealPoi( String poiOriginalStr );
 * 转化抓取的polygon 为 高德坐标-----------------------------dealPolygon( String polygonJsonStr, String mapType );
 * @author zhentao.liu
 *
 */
public interface DealDataInter {
	public Collection<String> dealPoi( String poiOriginalStr );
	public Map<String, String> dealPolygon( String polygonJsonStr, String mapType );
}
