package com.autonavi.analysismap.service.impl;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.service.DealDataInter;
import com.autonavi.analysismap.util.DateFormat;

@Service
public class FilterPoiMain {
	Logger log = Logger.getLogger(getClass());
	
	ResponseStatus filterPoiMain(DealFileInter dealFile, GrabArgsInfo gai,DealDataInter dealData) {
		boolean ProcessedFileExist = dealFile.existFile( gai.getProcessingPath() );
		if(ProcessedFileExist){
			String fileNames = haveProcessedFile(gai.getProcessingPath()+"",dealFile, gai);
				return fileNames.equals("获取文件名出错") ? new ResponseStatus(400,fileNames):
					fileNames.equals("不存在poi，请先抓取poi") ? new ResponseStatus(300,fileNames) : 
						fileNames.contains("已经处理过的文件有") ? new ResponseStatus(200,fileNames) :
							fileNames.equals("") ? new ResponseStatus(400,"出错"):
								filterPoi(dealFile, gai, dealData ,fileNames) ; 
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
			if(fileNames.equals("")||!fileNames.contains("_poi")){//不存在 poi
				return "不存在poi，请先抓取poi";
			}
			if(fileNames.contains("_filterPoi")&&gai.isNoUpdate()){
				return "已经处理过的文件有："+fileNames+"请点击取消并操作未完成的步骤或者点击确定重新过滤poi" ;
			}
			if(fileNames.contains("_filterPoi")&&!gai.isNoUpdate()||!fileNames.contains("_filterPoi")){
				String[] files = fileNames.split(";");
				String poiFile = "";
				for(String f:files){
					if(f.contains("_poi")){
						poiFile = f;
					}
					if(f.contains("_filterPoi")||f.contains("_polygon")||f.contains("_convertPolygon")){
						dealFile.deleteFile(gai.getProcessingPath()+"/"+f);
					}
				}
				return poiFile;
			}
			return "";
	}
	private ResponseStatus filterPoi(DealFileInter dealFile, GrabArgsInfo gai, DealDataInter dealData, String fileNames) {
		Collection<String> uniqPoiNames = null;
		try {
			log.debug("开始过滤poi,关闭通道");
			dealFile.reNameFile(gai.getProcessingPath(),gai.getFilterPoiPath());
			String poiStr = dealFile.readDataFromTxt( gai.getFilterPoiPath()+"/"+fileNames );
			uniqPoiNames = dealData.dealPoi(poiStr);
			String filterPoiPath =  gai.getFilterPoiPath()+"/"+
					DateFormat.getStringCurrentDetialDate()+"_"+uniqPoiNames.size()+"_filterPoi.txt";
			dealFile.writeDataToTxt(uniqPoiNames ,filterPoiPath, false);
			dealFile.reNameFile(gai.getFilterPoiPath(),gai.getProcessingPath());
			log.debug("过滤的poi数量"+uniqPoiNames.size());
			log.debug("poi过滤完毕！，打开此次抓取通道！");
			return new ResponseStatus(100,filterPoiPath);
		} catch (IOException e) {
			e.printStackTrace();
			log.debug("过滤出错！");
			return  new ResponseStatus(400,"filterPoi出错");
		}
		
	}
}
