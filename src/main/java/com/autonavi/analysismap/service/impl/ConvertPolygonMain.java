package com.autonavi.analysismap.service.impl;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.PolygonCollection;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.orm.impl.PolygonDaoImpl;
import com.autonavi.analysismap.service.DealDataInter;
import com.autonavi.analysismap.util.DateFormat;
@Service
public class ConvertPolygonMain {
	@Autowired
	private PolygonDaoImpl polygonDao;
	
	Logger log = Logger.getLogger(getClass());
	public ResponseStatus convertPolygonMain(DealFileInter dealFile,GrabArgsInfo gai,DealDataInter dealData) {
		boolean ProcessedFileExist = dealFile.existFile( gai.getProcessingPath() );
		if(ProcessedFileExist){
			String fileNames = haveProcessedFile(gai.getProcessingPath()+"",dealFile, gai);
				return fileNames.equals("获取文件名出错") ? new ResponseStatus(400,fileNames):
					fileNames.equals("没有geom,请先抓取geom") ? new ResponseStatus(300,fileNames) : 
						fileNames.equals("出错") ? new ResponseStatus(400,"出错"):
							dealPolygon(gai,dealFile,dealData, fileNames);	
		}else{
			return new ResponseStatus(300,"你还没有poi数据，请先抓取poi！");
		}
	}
	
	private String haveProcessedFile(String path, DealFileInter dealFile, GrabArgsInfo gai){
		String fileNames = dealFile.getFilesFromMkdir(path);
		if(fileNames.equals("获取文件名出错")){//出错
			return fileNames;
		}
		if(!fileNames.contains("_polygon")){//不存在 poi
			return "没有geom,请先抓取geom";
		}                     
		if(!fileNames.contains("_convertPolygon")){
			String[] files = fileNames.split(";");
			for(String f:files){
				if(f.contains("_polygon")){
					return f;
				}
			}
		}
		return "出错";
	}
	private ResponseStatus dealPolygon( GrabArgsInfo gai, DealFileInter dealFile, DealDataInter dealData, String fileNames) {
		Map<String, String> deal_poi_Polygon = null ;
		try {
			log.info("开始转换geom,关闭通道");
			dealFile.reNameFile(gai.getProcessingPath(),gai.getDealPoiPolygonPath());
			String polygonJsonStr = dealFile.readDataFromTxt( gai.getDealPoiPolygonPath()+"/"+fileNames );
			deal_poi_Polygon = dealData.dealPolygon( polygonJsonStr,gai.getMapType() );
			JSONObject deal_poi_Polygon_json = JSONObject.fromObject( deal_poi_Polygon );
			String converPolygonPath =  gai.getDealPoiPolygonPath()+"/"+
					DateFormat.getStringCurrentDetialDate()+"_"+deal_poi_Polygon.size()+"_convertPolygon.txt";
			dealFile.writeDataToTxt( deal_poi_Polygon_json.toString(), converPolygonPath, false );
			
			PolygonCollection polygon = new PolygonCollection();
			JSONObject json = JSONObject.fromObject( polygonJsonStr );
			JSONArray keys = json.names();
			for(int i = 0; i<keys.size(); i++){
				String key = keys.getString(i);
				log.debug(key+":"+json.get(key));
				polygon.setKey(key);
				polygon.setType(gai.getMapType());
				polygon.setContext(json.getString(key));
				polygon.setGeom(deal_poi_Polygon.get(key));
			    polygonDao.insert( polygon );
			}
			
			dealFile.reNameFile(gai.getDealPoiPolygonPath(),gai.getDataPath()+"/"+DateFormat.getStringCurrentDetialDate());
//			dealFile.reNameFile(gai.getDealPoiPolygonPath(),gai.getProcessingPath());
			log.debug("转换的polygon的数量:"+deal_poi_Polygon.size());
			log.debug("转换polygon完毕！，打开此次抓取通道！");
			return new ResponseStatus(100,"转换polygon并插入成功！");
//			return new ResponseStatus(200,"前台请求成功");
		} catch (Exception e) {
			log.debug("转换polygon异常："+e);
			return  new ResponseStatus(400,"转换Polygon出错");
		}
	}
}
