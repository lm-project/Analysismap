package com.autonavi.analysismap.excutefile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.ResponseStatus;

@Service
public class GetFiles {
	Logger log =  Logger.getLogger(getClass());
	
	public String getFilesFromMkdir(String path){
		try {
			File file = new File(path);
			File[] files = file.listFiles();
			log.debug("获取目录下文件的数量："+files.length);
			String fileName = "";
			for(File f: files){
				fileName += f.getName()+";";
			}
			log.debug("获取目录下文件的名字为："+ fileName);
			return fileName;
		} catch (Exception e) {
			log.debug("获取文件名出错");
			return "获取文件名出错";
		}
	}
	public ResponseStatus getFiles(String path){
		try {
			File mkdir = new File(path);
			mkdir = mkdir.getAbsoluteFile();
			log.debug("文件的根目录："+mkdir);
			if(!mkdir.exists()){
				return new ResponseStatus(300,"文件不存在！");
			}
			File[] mkdirs = mkdir.listFiles();
			Map<String, Map<String, Map<String, String>>> map = new HashMap<String, Map<String,Map<String,String>>>();
			for(File m:mkdirs){
				File[] files = m.listFiles();
				Map<String,  Map<String, String>> map1 = new HashMap<String,  Map<String, String>>();
				for(File f: files){
					Map<String, String> map2 = new HashMap<String, String>();
					String fileName = f.getName();
					String[] arrStr = fileName.split("_");
					map2.put(arrStr[0], arrStr[1]);
					map1.put(arrStr[2].substring(0,arrStr[2].indexOf(".")), map2);
				}
				map.put(m.getName(), map1);
			}
			return new ResponseStatus(100,JSONArray.fromObject(map).toString(),"查询完成！");
		} catch (Exception e) {
			log.debug("获取文件名出错");
			e.printStackTrace();
			return new ResponseStatus(400,"获取文件错误！");
		}
	}
}
