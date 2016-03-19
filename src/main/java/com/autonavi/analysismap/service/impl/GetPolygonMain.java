package com.autonavi.analysismap.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.service.GetDataByUrlInter;
import com.autonavi.analysismap.util.DateFormat;

import net.sf.json.JSONObject;
@Service
public class GetPolygonMain {
	Logger log = Logger.getLogger(getClass());
	
	public  ResponseStatus getPolygonMain(GrabArgsInfo gai,DealFileInter dealFile, GetDataByUrlInter getDataByUrl ) {
		boolean ProcessedFileExist = dealFile.existFile( gai.getProcessingPath() );
		if(ProcessedFileExist){
			String fileNames = haveProcessedFile(gai.getProcessingPath()+"",dealFile, gai);
				return fileNames.equals("获取文件名出错") ? new ResponseStatus(400,fileNames):
					fileNames.equals("不存在poi，请先抓取poi") ? new ResponseStatus(300,fileNames) : 
						fileNames.equals("不存在过滤的poi，请先过滤poi") ? new ResponseStatus(300,fileNames) : 
							fileNames.contains("已经处理过的文件有") ? new ResponseStatus(200,fileNames) :
								fileNames.equals("") ? new ResponseStatus(400,"出错"):
									getPolygon( gai, dealFile , getDataByUrl,fileNames);	
		}else{
			return new ResponseStatus(300,"你还没有poi数据，请先抓取poi！");
		}
	}
	/**
	 * 
	 * 获取文件名检测曾经是否抓取过poi
	 * @param path
	 * @param haveProcessedFile
	 * @param dealFile 
	 * @param gai 
	 * @return
	 */
	private String haveProcessedFile(String path, DealFileInter dealFile, GrabArgsInfo gai){
			String fileNames = dealFile.getFilesFromMkdir(path);
			if(fileNames.equals("获取文件名出错")){//出错
				return fileNames;
			}
			if(!fileNames.contains("_poi")){//不存在 poi
				return "不存在poi，请先抓取poi";
			}
			if(!fileNames.contains("_filterPoi")){
				return "不存在过滤的poi，请先过滤poi";
			}
			if(fileNames.contains("_polygon")&&gai.isNoUpdate()){
				return "已经处理过的文件有："+fileNames+"请点击取消并操作未完成的步骤或者点击确定重新抓取polygon" ;
			}
			if(fileNames.contains("_polygon")&&!gai.isNoUpdate()||!fileNames.contains("_polygon")){
				String[] files = fileNames.split(";");
				String filterPoiFile = "";
				for(String f:files){
					if(f.contains("_filterPoi")){
						filterPoiFile = f;
					}
					if(f.contains("_polygon")||f.contains("_convertPolygon")){
						dealFile.deleteFile(gai.getProcessingPath()+"/"+f);
					}
				}
				return filterPoiFile;
			}
			return "";
	}
	
	private ResponseStatus getPolygon(GrabArgsInfo gai,DealFileInter dealFile, 
						GetDataByUrlInter getDataByUrl, String filterPoiFileNames) {
		Map<String, String> poi_Polygon = null;
		String Path = "没有一条geom";
		try {	
			log.info("开始抓取geom,关闭通道");
			dealFile.reNameFile(gai.getProcessingPath(),gai.getPoiPolygonPath());
			Collection<String> uniqPoiNames = dealFile.readLineFromTxt( gai.getPoiPolygonPath()+"/"+filterPoiFileNames );
			poi_Polygon = getDataByUrl.getPolygonByUrl(gai.getMapType(), uniqPoiNames);
			if(poi_Polygon.size()>0){
				JSONObject poi_Polygon_json = JSONObject.fromObject(poi_Polygon);
				Path =  gai.getPoiPolygonPath()+"/"+
						DateFormat.getStringCurrentDetialDate()+"_"+poi_Polygon.size()+"_polygon.txt";
				dealFile.writeDataToTxt(poi_Polygon_json.toString(), Path, false);
			}
			dealFile.reNameFile(gai.getPoiPolygonPath(),gai.getProcessingPath());
			log.debug("抓取的polygon数量"+poi_Polygon.size());
			log.debug("polygon抓取完毕！，打开此次抓取通道！");
			return new ResponseStatus(100,Path);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseStatus(400,"getPolygon出错！");
		}
	}
}
