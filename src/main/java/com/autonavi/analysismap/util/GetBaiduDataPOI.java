//package com.autonavi.mapart.util;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URL;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//public class GetBaiduDataByPOI {
//
//	private static final String SEARCH_POI = "http://map.baidu.com/?newmap=1&reqflag=pcmap&"
//			+ "biz=1&from=webmap&qt=s&da_src=pcmappg.searchBox.button&wd=小区&c=131"
//			+ "&src=0&wd2=&sug=0&l=15&b=(12849780.03,4758298.57;13081648.69,4993877.7)"
//			+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t=1409818765064";
//	
//	
//	private static final String SEARCH_POI_URL = "http://gss3.map.baidu.com/?"
//			+ "newmap=1&reqflag=pcmap&biz=1&from=webmap"
//			+ "&qt=bkg_data&c=131&ie=utf-8&wd=#SEARCH_KEY#&l=17"
//			+ "&xy=#TITLE_POINT#"
//			+ "&b=(#BBOX#)";
//	
//	private Log log = LogFactory.getLog(this.getClass());
//	private static final double MAX_LNG = 13081648.69;
//	private static final double MAX_LAT = 4993877.7;
//	private static double MIN_LNG = 12849780.03;
//	private static final double MIN_LAT = 4758298.57;
//	private static int level = 17;
//	private static double xx = 1015 * Math.pow(2, 18 - level);
//	private static double yy = 521 * Math.pow(2, 18 - level);
//	public void  getBdPoiName(String key){
//		try {
//			int bbox_x = (int) Math.ceil(( MAX_LNG - MIN_LNG )/xx);
//			int bbox_y = (int) Math.ceil(( MAX_LAT - MIN_LAT )/yy);
//			log.info("box"+bbox_x+","+bbox_y);
//			double pixel =  Math.pow(2,17-18);
//			for(int bx = 0; bx < bbox_x; bx++){//bbox_x
//				int MIN_TILE_X = (int) Math.floor(MIN_LNG*pixel/256);
//				int MAX_TILE_X = (int) Math.floor((MIN_LNG+xx)*pixel/256);
//				double min_y = MIN_LAT;
//				for(int by = 0; by < bbox_y; by++){//bbox_y
//					String bbox = (String.format("%20.2f", MIN_LNG)+","+String.format("%20.2f", min_y)+";"
//							+String.format("%20.2f", MIN_LNG+xx)+","+String.format("%20.2f", min_y+yy)).replace(" ", "");
//					log.info(bbox);
//					int MIN_TILE_Y = (int) Math.floor(min_y*pixel/256);
//					int MAX_TILE_Y = (int) Math.floor((min_y+yy)*pixel/256);
//					log.info(MIN_TILE_X+","+MIN_TILE_Y+";"+MAX_TILE_X+","+MAX_TILE_Y);
//					for(int i = MIN_TILE_X; i<=MAX_TILE_X; i++){//tile_x
//						for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j++){//tile_y
//							String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", key)
//								   .replaceAll("#TITLE_POINT#",i+"_"+j)
//						           .replace("#BBOX#",bbox);
//							Document doc = requestURL(urlString);
//							if(doc!=null){
//								JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
//								JSONArray array = json.getJSONArray("uids");
//								for(int m = 0; m< array.size() ; m ++) {
//									JSONObject poi = array.getJSONObject(m);
//									savePoiData(poi.getString("name"));
//									log.info("==========="+poi.getString("name"));
//								}
//							}
//						}
//					}
//					min_y = min_y+yy;
//				}
//				MIN_LNG = MIN_LNG+xx;
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	private void savePoiData(String name){
//		
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("D:/baidu/bei_jing/poi.txt"));
//			String readLine = null;
//			while((readLine = br.readLine()) != null){
//				if(readLine.equals(name)){
//					br.close();
//					return;
//				}
//			}
//			br.close();
//			PrintWriter fw = new PrintWriter(new FileWriter("D:/baidu/bei_jing/poi.txt",true));
//			fw.println(name);
//			fw.flush();
//			fw.close();
//		} catch (IOException e) {
//			log.debug("noLnglatToSaveFile");
//			e.printStackTrace();
//		}
//		
//	}
//
//	private Document requestURL(String urlString) throws Exception {
//		try {
//			Document doc = Jsoup.parse(new URL(urlString), 10 * 1000);
//			log.info("正确的url:"+urlString);
//			return doc;
//		
//		} catch (Exception e) {
//			log.debug("错误url:"+urlString);
//			return null;
//		}
//	}
//	
//}
//

package com.autonavi.analysismap.util;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class GetBaiduDataPOI {

	
	
	private static final String SEARCH_POI_URL = "http://gss3.map.baidu.com/?"
			+ "newmap=1&reqflag=pcmap&biz=1&from=webmap"
			+ "&qt=bkg_data&c=131&ie=utf-8&wd=#SEARCH_KEY#&l=15"
			+ "&xy=#TITLE_POINT#"
			+ "&b=(#BBOX#)";
	
	
	private Log log = LogFactory.getLog(getClass());

	
	/*
	 * 
	 * 分割box
	 */
	/*public @ResponseBody
	ResponseStatus searchProjectByPage(@RequestParam("page") Integer page, @RequestParam("projectname") String projectname,
			HttpServletRequest request) {

		
	}*/
	@RequestMapping(value = "/splitCityToBox", method = RequestMethod.POST)
	public void  splitCityToBox(@RequestParam("city") String city, @RequestParam("level") Integer level,
			@RequestParam("platePoint") String platePoint, HttpServletRequest request){
		System.out.println("============================================");
		System.out.println(city);
		System.out.println(level);
		/*
		 * 18级下每个box横向  间隔1015个单位平面坐标
		 * 18级下每个box纵向  间隔521个单位平面坐标
		 * 每小一级在此基础上*2；
		 */
//		try {
//			final double box_xx = 1015 * Math.pow(2, 18 - level);
//			final double box_yy = 521 * Math.pow(2, 18 - level);
//			final int box_num_x = (int) Math.ceil(( CITY_MAX_LNG - CITY_MIN_LNG )/box_xx);
//			final int box_num_y = (int) Math.ceil(( CITY_MAX_LAT - CITY_MIN_LAT )/box_yy);
//			log.info("box_scope:"+box_num_x+","+box_num_y);
//			double min_box_x = CITY_MIN_LNG;
//			String box = "";
//			for(int bx = 0; bx < box_num_x; bx++){//bbox_x
//				double min_box_y = CITY_MIN_LAT;
//				
//				for(int by = 0; by < box_num_y; by++){//bbox_y
//					box += (String.format("%20.2f", min_box_x)+","+String.format("%20.2f", min_box_y)+";"
//				          +String.format("%20.2f", min_box_x+box_xx)+","
//						  +String.format("%20.2f", min_box_y+box_yy)).replace(" ", "")+"\n";
//					log.info("boxed:"+box);
//					
//					min_box_y = min_box_y + box_yy;
//				}
//				min_box_x = min_box_x + box_xx;
//			}
//			saveBoxToText(box);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
//	private void saveBoxToText(String box) {
//		System.out.println("========"+box);
//	}
	
	
//	
//	public void getTileXY(Collection<String> boxCollection){
//		 JSONArray _json=JSONArray.fromObject(boxCollection);
//		 double pixel = Math.pow(2,15-18);
//		 String JsonText = "";
//		 for(int m = 0; m<_json.size(); m++){
//			 String[] boxArr = _json.get(m).toString().split(";");
//			 String[] boxLeftDownPoi = boxArr[0].split(",");
//			 String[] boxRightUpPoi = boxArr[1].split(",");
//			 int MIN_TILE_X = (int) Math.floor(Double.parseDouble(boxLeftDownPoi[0])*pixel/256);
//			 int MIN_TILE_Y = (int) Math.floor(Double.parseDouble(boxLeftDownPoi[1])*pixel/256);
//			 int MAX_TILE_X = (int) Math.floor((Double.parseDouble(boxRightUpPoi[0])+xx)*pixel/256);
//			 int MAX_TILE_Y = (int) Math.floor((Double.parseDouble(boxRightUpPoi[1])+yy)*pixel/256);
//			 log.info("tile_xy:"+MIN_TILE_X+","+MIN_TILE_Y+";"+MAX_TILE_X+","+MAX_TILE_Y);
//			 String tileJsonText="{\"box\":\""+_json.get(m)+"\",tile:[";
//				 for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j++){//tile_y
//					 for(int i = MIN_TILE_X+1; i<=MAX_TILE_X; i++){
//					 tileJsonText+="{\"xy\":\""+i+"_"+j+"\"},";
//				 }
//			 }
//			JsonText +=  tileJsonText.substring(0,tileJsonText.length()-1)+"]},";
//		 }
//		 requestURLByArg(JsonText);
//		 
//	}
	
//	private void requestURLByArg(String JsonText){
//		JSONArray c_json=JSONArray.fromObject("["+JsonText.substring(0,JsonText.length()-1)+"]");
//		for(int i = 0; i<c_json.size(); i++){
//			JSONObject json = JSONObject.fromObject(c_json.get(i));
//			JSONArray array = json.getJSONArray("tile");
//			for(int j = 0; j<array.size(); j++){
//				JSONObject jsonxy = JSONObject.fromObject(array.get(j));
//				String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", "小区")
//						   .replaceAll("#TITLE_POINT#",jsonxy.getString("xy"))
//				           .replace("#BBOX#",json.getString("box"));
//				getPoiData(urlString);
//			}
//		}
//	}
//	int cout = 1;
//	private void getPoiData(String urlString){
//		try {
//			Document doc = requestURL(urlString);
//			if(doc!=null){
//				JSONObject json = JSONObject.fromObject(CommonUtil.Unicode2GBK(doc.text()));
//				JSONArray array = json.getJSONArray("uids");
//				for(int m = 0; m< array.size() ; m ++) {
//					JSONObject poi = array.getJSONObject(m);
//					savePoiData(poi.getString("name"));
//					log.info(poi.getString("name")+cout++);
//					
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	private Document requestURL(String urlString) throws Exception {
//		try {
//			Document doc = Jsoup.parse(new URL(urlString), 10 * 1000);
//			log.info("正确的url:"+urlString);
//			return doc;
//		} catch (Exception e) {
//			//e.printStackTrace();
//			log.debug("错误url:"+urlString);
//			return null;
//		}
//	}
//	
//	private void savePoiData(String name){
//		
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("D:/baidu/bei_jing/poi.txt"));
//			String readLine = null;
//			while((readLine = br.readLine()) != null){
//				if(readLine.equals(name)){
//					br.close();
//					return;
//				}
//			}
//			br.close();
//			PrintWriter fw = new PrintWriter(new FileWriter("D:/baidu/bei_jing/poi.txt",true));
//			fw.println(name);
//			fw.flush();
//			fw.close();
//		} catch (IOException e) {
//			log.debug("noLnglatToSaveFile");
//			e.printStackTrace();
//		}
//		
//	}
//	/*
//	 * 
//	 * 方式二： 以瓦片为主得到box
//	 */
//	public void getPOIName(){
//		int MIN_TILE_X = 6274;
//		int MAX_TILE_X = 6387;
//		int MIN_TILE_Y = 2323;
//		int MAX_TILE_Y = 2438;
//		double pixel = Math.pow(2,15-18);
//		for(int j = MIN_TILE_Y; j<=MAX_TILE_Y; j = j+2){
//			double MIN_BOX_Y = MIN_TILE_X * 256/pixel;
//			double MAX_BOX_Y = ((MIN_TILE_X + 1) > MAX_TILE_Y ? MAX_TILE_Y : (MIN_TILE_X + 1))*256/pixel;
//			
//			for(int i = MIN_TILE_X; i<=MAX_TILE_X; i = i+5){
//				double MIN_BOX_X = MIN_TILE_X * 256/pixel;
//				double MAX_BOX_X = ((MIN_TILE_X + 4) > MAX_TILE_X ? MAX_TILE_X : (MIN_TILE_X + 4))*256/pixel;
//				
//				String bbox = (String.format("%20.2f", MIN_BOX_X)+","+String.format("%20.2f", MIN_BOX_Y)+";"
//				          +String.format("%20.2f", MAX_BOX_X)+","+String.format("%20.2f", MAX_BOX_Y)).replace(" ", "");
//				
//				System.out.println(bbox);
//			}
//		}
//		
//		
//	}
//	
//	public  void containTileNum() throws Exception{
//		for(int i = 2336; i<=2338; i++){
//			for(int j = 6281;j<=6285; j++){
//				String urlString = SEARCH_POI_URL.replaceAll("#SEARCH_KEY#", "小区")
//						   .replaceAll("#TITLE_POINT#",j+"_"+i)
//				           .replace("#BBOX#","12863967.84,4784840.98;12872087.84,4789008.84");
//				requestURL(urlString);
//			}
//		}
//	}
	
}

 

