package com.autonavi.analysismap.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.service.CalculateBoxTileInter;
import com.autonavi.analysismap.service.GetDataByUrlInter;
import com.autonavi.analysismap.util.DateFormat;
@Service
public class GetPoiMain {
	
	@Autowired
	private  CalculateBoxTileInter calcuBoxTile;
	@Autowired
	private  GetDataByUrlInter getDataByUrl;
	@Autowired
	private  DealFileInter dealFile;

	private  GrabArgsInfo gai;
	Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 * 抓取poi主方法！
	 * @param getDataByUrl  请求url获取数据的接口对象
	 * @param gai           保存请求参数的实体类对象
	 * @param dealFile      处理文本文件的接口对象
	 * @return
	 */
	
	public ResponseStatus getPoiMain( GrabArgsInfo gai){
		this.gai = gai;
		boolean ProcessedFileExist = dealFile.existFile( gai.getProcessingPath() );
		String fileNames = ProcessedFileExist ? getFileName(gai.getProcessingPath(),true) : getFileName(gai.getDataPath(),false);
		return !fileNames.equals("")&&gai.isNoUpdate() ? new ResponseStatus(200,fileNames) : 
					fileNames.equals("") || !gai.isNoUpdate() ? getPoi(gai,dealFile,getDataByUrl,fileNames) : 
						new ResponseStatus(400,fileNames);
						
	}
	/**
	 * 
	 * 获取文件名检测曾经是否抓取过poi
	 * @param path
	 * @param haveProcessedFile
	 * @return
	 */
	private String getFileName(String path,boolean haveProcessedFile){
		if(haveProcessedFile){
			String fileNames = dealFile.getFilesFromMkdir(path);
			return fileNames.equals("获取文件名出错") ? fileNames : fileNames.equals("") ? "" : !gai.isNoUpdate()?
								dealFile.deleteFile(gai.getProcessingPath()):
									"该文件下有如下文件："+fileNames+"请点击取消并操作未完成的步骤或者点击确定重新抓取poi";
		}else{
			String isOk = dealFile.createMkdir(gai.getProcessingPath());
			if(isOk.equals("目录创建失败！")) return isOk;
			String fileNames = dealFile.getFilesFromMkdir(path);
			return fileNames.equals("0;") ? "" :
				fileNames.equals("获取文件名出错") ? fileNames : 		
					"所有的步骤都已存在，最近的一次的抓取时间为："+fileNames.split(";")[fileNames.split(";").length-1]+
						"请点击取消退出或者点击确定重新抓取poi";
		}
	}
	/**
	 * 
	 * 抓取poi
	 * @param gai
	 * @param dealFile
	 * @param getDataByUrl
	 * @param fileNames 
	 * @return
	 */
	
	private ResponseStatus getPoi(GrabArgsInfo gai, DealFileInter dealFile,
			GetDataByUrlInter getDataByUrl, String fileNames){
	
		ResponseStatus response = null;
		try {
			log.debug("抓取poi开始，关闭此次抓取通道！");
			dealFile.reNameFile(gai.getProcessingPath(),gai.getPoiPath());
			 // 获取box和Tile 
			getBoxTile(gai,dealFile);
			String boxTileStr = dealFile.readDataFromTxt(gai.getBoxTilePath());
			//根据boxTile进行抓取。。。
			Map<Collection<String>, Integer> poiNumMap = getDataByUrl.getPoiByUrl(boxTileStr,gai);
			Collection<String> poiContent = null;
			int poiNum  = 0;
			for(Entry<Collection<String>, Integer> map : poiNumMap.entrySet()){
				poiContent = map.getKey();
				poiNum = map.getValue();
			}
			if(poiContent.size()>0){
				String poiPath = gai.getPoiPath()+"/"+DateFormat.getStringCurrentDetialDate()+"_"+poiNum+"_poi.txt";
				dealFile.createFile( poiPath );
				dealFile.writeDataToTxt( poiContent.toString(), poiPath , false);
				response = new ResponseStatus(100, "poi数据已经抓取完毕:"+poiPath);
			}
			dealFile.reNameFile(gai.getPoiPath(),gai.getProcessingPath());
			log.debug("抓取的数据数量"+poiContent.size()+" 总共的数量"+poiNum);
			log.debug("抓取poi结束，打开此次抓取通道！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 
	 * 获取box
	 * @param gai
	 * @param dealFile
	 */
	private void getBoxTile(GrabArgsInfo gai, DealFileInter dealFile){
		try {
			/* 检测box和tile是否存在 */
			boolean boxTileExist = dealFile.existFile(gai.getBoxTilePath());
			if(boxTileExist){
				return;
			}
			String box =  calcuBoxTile.calculateBox(gai);
			dealFile.createFile( gai.getBoxPath() );
			dealFile.writeDataToTxt(box, gai.getBoxPath(), false);
			log.info("保存box数据到："+gai.getBoxPath());
			String boxTile = calcuBoxTile.calculateTile( box, gai );
			dealFile.createFile( gai.getBoxTilePath() );
			dealFile.writeDataToTxt(boxTile, gai.getBoxTilePath(), false);
			log.info("保存boxTile数据到："+gai.getBoxTilePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
