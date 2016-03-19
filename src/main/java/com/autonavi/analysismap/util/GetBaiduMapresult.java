package com.autonavi.analysismap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.autonavi.analysismap.entity.ResponseStatus;

public class GetBaiduMapresult implements ApiRequest {
	
	private Log log = LogFactory.getLog(getClass());
	private final String SEARCH_POLYON_URL = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&qt=ext&uid="
			+ "#UID#"
			+ "&c="
			+ "#SEARCH_KEY#"
			+ "&ext_ver=new&tn=B_NORMAL_MAP&nn=0&ie=utf-8&l=16";
	
	
	StringBuffer sb = new StringBuffer();
	/**
	 * 查询关键字所在设施区域
	 * 
	 * @param key
	 * @return
	 */
	public ResponseStatus select(String key) {
		ResponseStatus response = null;
		String enkey = CommonUtil.encode(key);
		if (enkey != null) {
			response = request(enkey);
			// 如果查询无结果且查询关键字中包含特殊字符，如：（）()等， 则去除留中文继续再次搜索
			if (response.getCode() == 100
					&& ("".equals(response.getRestring()) || response
							.getRestring() == null)
					&& (key.indexOf("(") > -1 || key.indexOf("（") > -1)) {

				key = key.substring(0, key.length() - 1);
				String[] keys = key.indexOf("(") > -1 ? key.split("\\(") : key
						.split("\\（");
				enkey = CommonUtil.encode(keys[0] + keys[1]);
				response = request(enkey);
			}
		} else {
			response = new ResponseStatus(200, "error", "查询参数错误！");
		}
		return response;
	}
	
	private ResponseStatus request(String enkey) {
		ResponseStatus status = null;
		// 组织URL 串
		String urlString = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap"
				+ "&qt=s&da_src=pcmappg.searchBox.button&wd="
				+ enkey
				+ "&src=0&wd2=&sug=0&"
				+ "&from=webmap&tn=B_NORMAL_MAP&nn=0&ie=utf-8&t="
				+ DateFormat.get13Now();
		// 解析返回html页面元素 code : 100 - 请求正常 ； 200 - 请求异常 ； 300 -目标服务器拒绝访问
		status = processPolygonURL(1, urlString);
		if (status.getCode() == 100) {
			//FIXME 数组越界
			String result = status.getRestring();
			String[] arrays = result.split(",");
			if( arrays.length < 2) {
				return status;
			}
			urlString = SEARCH_POLYON_URL.replaceAll("#UID#", arrays[0]).replaceAll("#SEARCH_KEY#", arrays[1]);
			status = processPolygonURL(2, urlString);
			
		}
		return status;
	}

	// 模拟浏览器请求url
	public ResponseStatus processPolygonURL(int type, String urlString) {
		try {
			Document doc = requestURL(urlString);
			String result = executeDom(type, CommonUtil.Unicode2GBK(doc.text()));
			return new ResponseStatus(100, result==null ? "" : result , "success");
		} catch (Exception e) {
			return new ResponseStatus(300, "error", "访问被拒绝!"+e.getMessage());
		}
		// 返回解析值
	}

	private Document requestURL(String urlString) throws Exception {
		try {
			return Jsoup.parse(new URL(urlString), 10 * 1000);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 解析DOM元素
	private String executeDom(int type, String dom) {
		try{
			JSONObject object = JSONObject.fromObject(dom);
			// bd搜索分为2步
			if (type == 1) {
				// 第一步，获取城市代码c和用户uid
				JSONObject current_city = object.getJSONObject("current_city");
				String c = String.valueOf(current_city.get("code"));

				JSONObject result = object.getJSONObject("result");
				String uid = String.valueOf(result.get("profile_uid"));
				bdCityStr += "\"cityCode\":\""+c+"\"},";
				return uid + "," + c;

			} else {
				// 第二部，获取返回数据
				JSONObject content = object.getJSONObject("content");
				String geo = content.getString("geo");
				if (!"".equals(geo)) {
					String str = geo.split("\\|")[2].substring(2);
					return str.substring(0, str.lastIndexOf(";"));
				}
			}
		} catch( Exception e) {
			e.printStackTrace();
			log.error(e);
			return null;
		}
		return null;
	}
	static String bdCityStr = "";
	static String fin = "";
	public static void main(String[] args) throws IOException {
		File file = new File("d:/cityList.json");
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		String cityListStr = new String (filecontent, "utf-8");
		JSONObject jsonObj = JSONObject.fromObject(cityListStr);
		JSONArray jsonArr = JSONArray.fromObject(jsonObj.getString("district"));
		bdCityStr = "";
		for(int i=0; i<jsonArr.size(); i++){
			JSONObject jsonOb = JSONObject.fromObject(jsonArr.get(i));
			String name = jsonOb.getString("districtName");
			bdCityStr += "{\"districtName\":\""+name+"\",\"districtCity\":[";
			JSONArray jsonArrs =  JSONArray.fromObject(jsonOb.getString("districtCity"));
			for(int j = 0; j< jsonArrs.size(); j++){
				JSONObject	cityJson = JSONObject.fromObject(jsonArrs.get(j));
				String city = cityJson.getString("city");
				bdCityStr += "{\"cityName\":\""+city+"\",";
				new GetBaiduMapresult().select(city);
			}
			fin += bdCityStr.substring(0, bdCityStr.length()-1)+"]},";
		}
		
		PrintWriter pw = new PrintWriter("d:/baiduCidyList.txt");
		pw.write("["+fin.substring(0,fin.length()-1)+"]");
		pw.flush();
		pw.close();
	}
}
