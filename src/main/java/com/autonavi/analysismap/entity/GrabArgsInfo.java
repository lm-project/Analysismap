

package com.autonavi.analysismap.entity;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GrabArgsInfo {
	private  JSONObject boundary;
	private  String district;
	private  double level;
	private  String key;
	private  String mapType;
	private  boolean noUpdate;
	private  String boxPath;
	private  String boxTilePath;
	private  String poiPath;
	private  String filterPoiPath;
	private  String poiPolygonPath;
	private  String dealPoiPolygonPath;
	private  String basePath;
	private  String processingPath;
	private  String dataPath;
	Logger log = Logger.getLogger(getClass());
	public GrabArgsInfo(){
		
	}
	/**
	 * 
	 * 
	 * @param requestArgs   json对象，包含客户端请求的参数，key如下
	 *      key city                  城市名称
	 *      key level                 地图级别
	 *      key key                   关键字
	 *      key mapType               地图商
	 *      key bntType               客户端执行的操作按钮
	 *      key noUpdate              是否重新抓取数据
	 *      key boundary		                城市最大最小坐标串
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public  GrabArgsInfo(JSONObject requstArgs) throws FileNotFoundException, IOException{
		this.boundary  = (JSONObject) requstArgs.get("boundary");
		this.key = requstArgs.getString("key");
		this.level = requstArgs.getDouble("level");
		this.mapType = requstArgs.getString("mapType");
		this.noUpdate = requstArgs.getBoolean("noUpdate");
		this.basePath = "../AnalysismapData/"+mapType+"/"+
				requstArgs.getString("district")+"/"+requstArgs.getString("level") +"/";

		this.boxPath = basePath + "box.txt";
		this.boxTilePath = basePath+"boxTile.txt";
		this.dataPath = basePath+key;
		
		this.processingPath = dataPath+"/0";
		this.poiPath = dataPath+"/poi";
		this.filterPoiPath = dataPath+"/filterPoi";
		this.poiPolygonPath = dataPath+"/geom";
		this.dealPoiPolygonPath = dataPath+"/covertGeomAndSaveDB";
		
	}
	public String getProcessingPath() {
		return processingPath;
	}
	public void setProcessingPath(String processingPath) {
		
		this.processingPath = processingPath;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public boolean isNoUpdate() {
		return noUpdate;
	}
	public void setNoUpdate(boolean noUpdate) {
		this.noUpdate = noUpdate;
	}
	public JSONObject getBoundary() {
		return boundary;
	}
	public void setBoundary(JSONObject boundary) {
		this.boundary = boundary;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double level) {
		this.level = level;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getBoxPath() {
		return boxPath;
	}
	public void setBoxPath(String boxPath) {
		this.boxPath = boxPath;
	}
	public String getBoxTilePath() {
		return boxTilePath;
	}
	public void setBoxTilePath(String boxTilePath) {
		this.boxTilePath = boxTilePath;
	}
	public String getPoiPath() {
		return poiPath;
	}
	public void setPoiPath(String poiPath) {
		this.poiPath = poiPath;
	}
	public String getFilterPoiPath() {
		return filterPoiPath;
	}
	public void setFilterPoiPath(String filterPoiPath) {
		this.filterPoiPath = filterPoiPath;
	}
	public String getPoiPolygonPath() {
		return poiPolygonPath;
	}
	public void setPoiPolygonPath(String poiPolygonPath) {
		this.poiPolygonPath = poiPolygonPath;
	}
	public String getDealPoiPolygonPath() {
		return dealPoiPolygonPath;
	}
	public void setDealPoiPolygonPath(String dealPoiPolygonPath) {
		this.dealPoiPolygonPath = dealPoiPolygonPath;
	}
	public String getMapType() {
		return mapType;
	}
	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	public String getDataPath() {
		return dataPath;
	}
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	
}
