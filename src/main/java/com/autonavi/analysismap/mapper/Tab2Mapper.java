package com.autonavi.analysismap.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.orm.impl.PolygonDaoImpl;
import com.autonavi.analysismap.service.CalculateBoxTileInter;
import com.autonavi.analysismap.service.DealDataInter;
import com.autonavi.analysismap.service.GetDataByUrlInter;
import com.autonavi.analysismap.service.GetDataStepInter;


@Controller

public class Tab2Mapper {
	@Autowired
	private  DealFileInter dealFile;
	@Autowired
	private  CalculateBoxTileInter calcuBoxTile;
	@Autowired
	private  GetDataByUrlInter getDataByUrl;
	@Autowired
	private  DealDataInter dealData;
	@Autowired
	private  GetDataStepInter getDataStep;
	
	@Autowired
	private PolygonDaoImpl polygonDao;
	
	private  GrabArgsInfo gai;
	Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 * 请求reference页面，进行坐标转换
	 * @return
	 */
	@RequestMapping("/reference")
	public String reference() {
		return "reference";
	}
	
	
	/**
	 * 
	 * 1. 抓取poi 
	 * @param requestArgs
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	@RequestMapping(value = "/getPoi", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus getPoi(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		return !fileName.equals("") ? new ResponseStatus(300,fileName) : getDataStep.getPoi(gai);
	}
	/**
	 * 2. 过滤poi
	 * @param requestArgs
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/filterPoi", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus filterPoi(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		if(!fileName.equals("")){
			return new ResponseStatus(100,"有人正在"+fileName+"，请稍后执行其他操作！");
		}
		ResponseStatus response = getDataStep.filterPoi( dealFile, gai, dealData);
		log.debug("抓取poi返回的信息："+response.toString());
		return response;
	}
	/**
	 * 3. 抓取polygon 
	 * @param requestArgs
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/getGeom", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus getGeom(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		if(!fileName.equals("")){
			return new ResponseStatus(100,"有人正在"+fileName+"，请稍后执行！");
		}
		ResponseStatus response = getDataStep.getPolygon( gai, dealFile,  getDataByUrl);
		log.debug("抓取poi返回的信息："+response.toString());
		return response;
	}
	/**
	 * 4.转换polygon 为高德坐标 并将Geom存入数据库
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/convertGeomAndInsertDB", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus convertGeomAndInsertDB(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		if(!fileName.equals("")){
			return new ResponseStatus(100,"有人正在"+fileName+"，请稍后执行！");
		}
		ResponseStatus response = getDataStep.convertPolygon( gai, dealFile, dealData);
//		if(response.getCode()==100){
//			 response = getDataStep.insertDB( gai, dealFile);
//		}
		log.debug("抓取poi返回的信息："+response.toString());
		return response;
		
	}
	/**
	 * 5. 综合抓取  
	 * @param requestArgs
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/excuteAll", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus excuteAll(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		if(!fileName.equals("")){
			return new ResponseStatus(100,"有人正在"+fileName+"，请稍后执行！");
		}
		ResponseStatus response1 = getDataStep.getPoi(gai);
		if(response1.getCode()!= 100){
			return response1;
		}
		ResponseStatus response2 = getDataStep.filterPoi( dealFile, gai, dealData);
		if(response2.getCode()!= 100){
			return response2;
		}
		ResponseStatus response3 = getDataStep.getPolygon( gai, dealFile,  getDataByUrl);
		if(response3.getCode()!= 100){
			return response3;
		}
		ResponseStatus response4 = getDataStep.convertPolygon( gai, dealFile, dealData);
//		if(response4.getCode() != 100){
//			return response4;
//		}
//		 getDataStep.insertDB( gai, dealFile);
		return new ResponseStatus(100,"所有步骤执行完毕！");
	}
	@RequestMapping(value = "/checkFile", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus checkFile(@RequestParam("requestArgs") String requestArgs, 
			 HttpServletRequest request) throws FileNotFoundException, IOException{
		setRequestArgs( requestArgs );
		String fileName = getProcessingFileName();
		if(!fileName.equals("")){
			return new ResponseStatus(100,"有人正在"+fileName+"，请稍后执行！");
		}
		return getDataStep.checkFile(gai, dealFile);
	}
	/**
	 * 设置请求参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void setRequestArgs( String requestArgs ) throws FileNotFoundException, IOException{
		JSONObject json = JSONObject.fromObject(requestArgs);
		log.info("抓取POI参数信息："+json.toString());
		gai = new GrabArgsInfo(json);	
	}
	
	/**
	 * 
	 * 判断请求是否在运行
	 * @return
	 */
	private String getProcessingFileName(){
		return dealFile.existFile(gai.getPoiPath()) ? "有人正在抓取poi，请稍后执行其他步骤！" : 
			   dealFile.existFile(gai.getFilterPoiPath()) ? "有人正在过滤Poi，请稍后执行其他步骤！" :  
			   dealFile.existFile(gai.getPoiPolygonPath()) ? "有人正在抓取geom，请稍后执行其他步骤！" : 
			   dealFile.existFile(gai.getDealPoiPolygonPath()) ? "有人正在坐标转换并存入数据库，请稍后执行其他步骤！" : "";
	
	}
}
