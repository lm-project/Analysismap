package com.autonavi.analysismap.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.util.ApiRequest;

@Service
public class PolygonCacheService {

	Log log = LogFactory.getLog(getClass());
	private final int SECOND = 1000;
	int m = 1;
	public synchronized Map<String, String> getPolygonByPoiName(final ApiRequest api, Collection<String> names) {
		Map<String, String> poi_geom = new HashMap<String, String>();
		try {
			long stime = -System.currentTimeMillis();
			Random r = new Random();
			final int sleepIntervel = 5;
			for (final String name : names) {
				int sleepTime = r.nextInt( sleepIntervel );
				if(m%500==0 ){
					int ok = 1;
					do {
						ResponseStatus rs = api.select("北京大学");
						if(StringUtils.isBlank(rs.getRestring())||rs.getRestring().toString().equals("error")){
							log.debug("请求过频繁，百度已经拒绝访问，延迟十分钟再试！");
							Thread.sleep(10*60 * SECOND);
						}else{
							ok = 0;
							log.debug("没有屏蔽或者屏蔽已经取消！");
						}
					}
					while(ok==1);
				}
				cacheData(poi_geom, name, api.select(name));
				Thread.sleep(sleepTime * SECOND);
				
			}
			log.info("update  ," + names.size() + " items spend time :"
					+ (System.currentTimeMillis() + stime) / SECOND + " second.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return poi_geom;
	}
	

	private void cacheData(final Map<String, String> poi_geom, final String name, ResponseStatus rs) {
		log.error("名称:"+name+"  Restring:"+rs.getRestring()+"  第"+ m++ +"条数据");
		try { 
			if( StringUtils.isBlank(rs.getRestring()) || rs.getRestring().toString().equals("error") ){
				return;
			}
			poi_geom.put(name,rs.getRestring());			
			
		} catch( Exception e) {
			e.printStackTrace();
			log.error( name+ ":"+rs.getRestring());
		}
	}
//	public void updateAll(final Collection<String> all) {
//		Calendar calendar = Calendar.getInstance();  
//		calendar.set(Calendar.HOUR_OF_DAY, 23);  
//		calendar.set(Calendar.MINUTE, 38);  
//		calendar.set(Calendar.SECOND, 0);
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				//updateAutonavi(all);
//				updateBdAll(all);
//				//updateTxAll(all);
//			}
////		},calendar.getTime());
//		},5 * 1000);
//	}
	
	static enum Type {
		baidu, qq, autonavi
	}

}

//if( ! type.equals(Type.autonavi)) {
//final boolean isSecond = type.equals(Type.qq)? false:true;
//final boolean isReverse = false;
//final String strGeo = type.equals(Type.baidu)? 
//		GetGeometry.getBaiduLngLat(rs.getRestring()):rs.getRestring();
//String geom = GetGeometry.getGeometry(strGeo, isReverse, GetGeometry.POLYGON, isSecond);
//nameGeomJsonStr.append("{\"name\":\""+name+"\",\"geom\":"+geom+"},");
//}	
