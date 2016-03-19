package com.autonavi.analysismap.orm;

import com.autonavi.analysismap.entity.PolygonCollection;

public interface PolygonDao {
	public void insert(PolygonCollection geom);
	
	public void  select(PolygonCollection geom);
}
