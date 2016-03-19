package com.autonavi.analysismap.util;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.excutefile.DealFileInter;
/**
 * 1.请求url 获取 城市编码  -------------------requestUrlGetCityCode
 * 2.解析boxTile      -------------------resolveBoxTile
 * 3.组织url，请求获取POI-------------------requestURL
 * 4.以json的形式保存
 * @author zhentao.liu
 *
 */
@Service
public class GetPoiByUrl {
	private Logger log = Logger.getLogger(getClass());
	@Autowired
	private DealFileInter dealFile;
	private static final String SEARCH_POI_URL = "http://gss3.map.baidu.com/?"
			+ "newmap=1&reqflag=pcmap&biz=1&from=webmap"
			+ "&qt=bkg_data&c=#CITY#&ie=utf-8&wd=#SEARCH_KEY#&l=#ZOOM#"
			+ "&xy=#TITLE_POINT#"
			+ "&b=(#BBOX#)";
	/**
	 * @param boxTile   城市包含的box和Tile的json字符串
	 * @param key       检索到的关键字
	 * @param level     检索的级别
	 * @param city      城市名称
	 * @param path      保存数据的路径
	 * @return
	 * @throws InterruptedException 
	 */
	public Map<Collection<String>, Integer> getPoiByBoxTile(String boxTile,GrabArgsInfo gai) throws InterruptedException {
		String cityCode = getCityCode(gai.getDistrict());
		if(cityCode.equals("")){return null;}
		return resolveBoxTile( boxTile,  cityCode, gai);
	}
	/*
	 * 解析保存瓦片文件的json
	 * 组织url串
	 */
	Map<Collection<String>, Integer> poi_num =  new HashMap<Collection<String>, Integer>();
	Collection<String> poiCollection = null;
	int poiNum = 0;
	private Map<Collection<String>, Integer> resolveBoxTile(String boxTile, final String cityCode, 
			final GrabArgsInfo gai) throws InterruptedException {
		poiCollection = new HashSet<String>();
		JSONArray boxTileArray = JSONArray.fromObject(boxTile);
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		final int boxTileLenght  = boxTileArray.size();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(boxTileLenght/10+5, 10000, 60, TimeUnit.SECONDS, queue,new ThreadPoolExecutor.CallerRunsPolicy());
		final CountDownLatch done  = new CountDownLatch(boxTileLenght);
		log.info("boxTile length========="+boxTileLenght);
		for(int i = 0; i<boxTileLenght; i++){
			final JSONObject boxTileJson = boxTileArray.getJSONObject(i);
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						getPoi(boxTileJson, cityCode, gai);
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}finally{
						done.countDown();
					}
				}
			});
		}
		done.await();
		poi_num.put(poiCollection, poiNum);
		return poi_num;
		
	}
	
	private void getPoi(JSONObject boxTileJson,  String cityCode, GrabArgsInfo gai){
		try {
			String boxScope = boxTileJson.getString("boxXY");										//BOX范围
			JSONArray tileArray = JSONArray.fromObject(boxTileJson.getString("tileXY"));			//Tile范围
			for(int j = 0; j<tileArray.size(); j++){
				String tileXY = (String) tileArray.get(j);
				String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", gai.getKey())
						.replaceAll("#TITLE_POINT#",tileXY).replace("#CITY#", cityCode)
						.replace("#BBOX#",boxScope).replace("#ZOOM#", String.valueOf(gai.getLevel()));
				Document doc = requestURL(urlString);
				if(doc != null){
					JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
					if( json.getInt("uid_num" )>0){
						poiNum += json.getInt("uid_num");
						poiCollection.add(json.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//请求url获取城市编码
	 private String getCityCode(String city){
		 String urlString = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap"
					+ "&qt=s&da_src=pcmappg.searchBox.button&wd="
					+ city
					+ "&src=0&wd2=&sug=0&"
					+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t="
					+ DateFormat.get13Now();
		 try {
			 Document doc = requestURL(urlString);
			 JSONObject object = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
			 JSONObject current_city = object.getJSONObject("current_city");
			 String cityCode = String.valueOf(current_city.get("code"));
			 return cityCode;
		 } catch (Exception e) {
			e.printStackTrace();
			return "";
		 }
	 }
		//请求url获取poi
		private Document requestURL(String urlString) throws Exception {
			try {
				Document doc = Jsoup.parse(new URL(urlString), 10 * 1000);
				log.debug("正确的url:"+urlString);
				return doc;
			} catch (Exception e) {
				log.debug("错误url:"+urlString);
				return null;
			}
		}
	
}
