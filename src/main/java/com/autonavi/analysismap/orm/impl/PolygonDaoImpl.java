package com.autonavi.analysismap.orm.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.autonavi.analysismap.entity.PolygonCollection;
import com.autonavi.analysismap.orm.PolygonDao;

@Repository
public class PolygonDaoImpl extends BasicMyBatisDao implements PolygonDao {
	Logger log = Logger.getLogger(getClass());

	@Override
	public void insert(PolygonCollection geom) {
		try {
			List<PolygonCollection> dbs = super.getList("polygonMapper.getByTypeKey", geom);
			log.debug("数据库中存在此数据吗？"+dbs.size()+":"+dbs.isEmpty());
			if (dbs.isEmpty()) {
				super.save("ReferenceGeom.insert", geom);
			} else {
//				for(PolygonCollection db : dbs) {
//					
//					log.debug(db+"重复的数据："+db.getKey()+"   "+db.getType()+"    "+db.getId());
////					db.setGeom( geom.getGeom() );
//				}
				geom.setGeom( geom.getGeom() );
				geom.setContext( geom.getContext() );
				super.update("polygonMapper.update", geom);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void select(PolygonCollection geom) {
		
	}
	


}
