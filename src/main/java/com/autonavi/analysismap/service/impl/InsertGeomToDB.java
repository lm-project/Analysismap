package com.autonavi.analysismap.service.impl;

import java.io.IOException;

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
import com.autonavi.analysismap.util.DateFormat;
@Service
public class InsertGeomToDB {
	Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private PolygonDaoImpl polygonDao;
	
	public ResponseStatus insertGeomToDbMain(DealFileInter dealFile,
			GrabArgsInfo gai) {
		boolean ProcessedFileExist = dealFile.existFile( gai.getProcessingPath() );
		if(ProcessedFileExist){
			String fileNames = haveProcessedFile(gai.getProcessingPath()+"",dealFile, gai);
				return fileNames.equals("获取文件名出错") ? new ResponseStatus(400,fileNames):
					fileNames.equals("没有转换geom,请先转换geom") ? new ResponseStatus(300,fileNames) : 
						fileNames.equals("出错") ? new ResponseStatus(400,"出错"):
							insertGeomToDB(dealFile, gai, fileNames);	
		}else{
			return new ResponseStatus(300,"你还没有poi数据，请先抓取poi！");
		}
	}
	private String haveProcessedFile(String path, DealFileInter dealFile, GrabArgsInfo gai){
		String fileNames = dealFile.getFilesFromMkdir(path);
		if(fileNames.equals("获取文件名出错")){//出错
			return fileNames;
		}
		if(!fileNames.contains("_convertPolygon")){//不存在 poi
			return "没有转换geom,请先转换geom";
		}
		if(fileNames.contains("_convertPolygon")){
			String[] files = fileNames.split(";");
			for(String f:files){
				if(f.contains("_convertPolygon")){
					return f;
				}
			}
		}
		return "出错";
	}
	
	private ResponseStatus insertGeomToDB(DealFileInter dealFile, GrabArgsInfo gai, String fileNames){
		PolygonCollection polygon = new PolygonCollection();
		try {
			log.info("开始插入geom到数据库,关闭通道");
			dealFile.reNameFile(gai.getProcessingPath(),gai.getDealPoiPolygonPath());
			String polygonJsonStr = dealFile.readDataFromTxt( gai.getDealPoiPolygonPath()+"/"+fileNames );
			JSONObject json = JSONObject.fromObject(polygonJsonStr);
			JSONArray keys = json.names();
			for(int i = 0; i<keys.size(); i++){
				log.debug(keys.get(i)+":"+json.get(keys.get(i)));
				polygon.setKey(keys.get(i).toString());
				polygon.setType(gai.getMapType());
				polygon.setContext("");
				polygon.setGeom(json.get(keys.get(i)).toString());
			    polygonDao.insert( polygon );
			}
			dealFile.reNameFile(gai.getDealPoiPolygonPath(),gai.getDataPath()+"/"+DateFormat.getStringCurrentDetialDate());
			log.debug("插入polygon完毕！，打开此次抓取通道！");
			return new ResponseStatus(100,"成功插入polygon");
		} catch (IOException e) {
			e.printStackTrace();
			log.debug("insertGeomToDB出错");
			return new ResponseStatus(400,"插入polygon错误");
		}
	}
}
