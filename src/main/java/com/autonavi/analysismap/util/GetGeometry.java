package com.autonavi.analysismap.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.sun.jna.ptr.DoubleByReference;

/**
 * 计算、格式化空间数据，坐标反变形
 * 
 * @author huandi.yang
 * 
 */
public class GetGeometry {

	private static final int SENCOND = 3600;
	public static final String POLYGON = "POLYGON";
	public static final String LINESTRING = "LINESTRING";
	
	private static  HtmlPage page = null;
	private static Logger log = Logger.getLogger("GetGeomtry");
	/**
	 * 格式化geom数据
	 * 
	 * @param pointString
	 *            点串
	 * @param isReverse
	 *            是否反变形
	 * @param geomType
	 *            空间数据类型(多边形or折线)
	 * @param isSecond
	 *            坐标是否为秒级
	 * @return
	 */
	public static String getGeometry(String pointString, boolean isReverse, String geomType, boolean isSecond) {
		// 根据空间数据类型设置字符串开头
		// 拼接点值pair(秒级)
		String geoms = getPointPairString(pointString, isReverse, geomType, isSecond);
		if (POLYGON.equals(geomType) && StringUtils.isNotBlank(geoms)) {
			return "MULTIPOLYGON(((" + geoms + ")))";
		} else if (LINESTRING.equals(geomType) && StringUtils.isNotBlank(geoms)) {
			return "LINESTRING(" + geoms + ")";
		}

		return "";
	}

	/**
	 * 格式化点值pair(秒级)
	 * 
	 * @param pointString
	 *            点串
	 * @param isReverse
	 *            是否反变形
	 * @param geomType
	 * 			   空间数据类型(多边形or折线)
	 * @param isSecond
	 *            坐标是否为秒级
	 * @return
	 */
	protected static String getPointPairString(String pointString, boolean isReverse, String geomType, boolean isSecond) {
		String[] array = pointString.split(",");
		StringBuffer pointPair = new StringBuffer();
		
		if(array.length < 2) {
			return "";
		}
		final int firstIdx = 0;
		// 循环处理每个点
		for (int i = firstIdx; i < array.length; i += 2) {

			Double[] geom = getSecondCoord(array[i], array[i + 1], isReverse, isSecond);
			if (geom != null) {
				pointPair.append(StringUtils.join(geom, " "));
				if (i < array.length - 2) {
					pointPair.append(",");
				}
			}

		}
		if (POLYGON.equals(geomType)){
			pointPair.append(",").append(StringUtils.join(getSecondCoord(array[firstIdx], array[firstIdx + 1], isReverse, isSecond), " "));
		}
		return pointPair.toString();
	}

	
	/**
	 * 转换坐标到妙级
	 * 
	 * @param x_coord
	 * @param y_coord
	 * @param isReverse
	 * @param isSecond 
	 * @return
	 */
	protected static Double[] getSecondCoord(String x_coord, String y_coord, boolean isReverse, boolean isSecond) {
		double x = Double.valueOf(x_coord);
		double y = Double.valueOf(y_coord);
		
		if (isReverse) {
			Double[] coord = reverseCoord(x, y);
			x = coord[0];
			y = coord[1];
		} 
		
		if( ! isSecond){
			return new Double[] { x * SENCOND, y * SENCOND};
		}
		return new Double[] {x,y};
	}
	
	/*
	 * 
	 * 调用 百度api接口转百度坐标为秒级
	 */
	public static String getBaiduLngLat(String context) {
		try {
			return getPage().executeJavaScript("batchGeotoMct('"+context+"')").getJavaScriptResult().toString();
		} catch (Exception e) {
			log.debug("请求前台转换坐标出错："+e);
			return "";
		}
	}

	
	private static HtmlPage getPage() {
		if ( page != null) {
			return page;
		}
		try {
			WebClient webClient = new WebClient();
			JavaScriptEngine engine = new JavaScriptEngine(webClient);
			webClient.setJavaScriptEngine(engine);
			webClient.getOptions().setJavaScriptEnabled(true);     
			page = webClient.getPage("http://localhost:8080/Analysismap/reference");
		}catch(Exception e){
			log.debug("HtmlPage 异常："+e);
		}
		return page;
	}

	/**
	 * 坐标反变形
	 * 
	 * @param x_coord
	 * @param y_coord
	 * @return 反变形后坐标
	 */
	protected static Double[] reverseCoord(double x_coord, double y_coord) {
		DoubleByReference rx = new DoubleByReference();
		DoubleByReference ry = new DoubleByReference();

		try {
			Optmap.INSTANCE.reversemap(x_coord, y_coord, rx, ry);
			return new Double[] { Double.valueOf(rx.getValue()), Double.valueOf(ry.getValue()) };
		} catch (UnsatisfiedLinkError e) {
			e.getStackTrace();
		} catch (NoClassDefFoundError e) {
			e.getStackTrace();
		}
		return new Double[] { 0D, 0D };
	}

}
