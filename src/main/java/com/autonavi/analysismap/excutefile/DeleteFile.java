package com.autonavi.analysismap.excutefile;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
@Service
public class DeleteFile {
	Logger log = Logger.getLogger(getClass());
	public String deleteFile(String path){
		try {
			File file = new File(path);  
			if (!file.exists()) {  // 不存在返回 false  
				log.debug("删除文件不存在:"+path);
			    return "删除文件不存在";  
			 }else{
				 String deleteOk = file.isDirectory() ? deleteMkdirs(file) :
						 file.delete() ? "删除文件成功" : "删除文件失败";
				 log.debug(path+deleteOk);
				 return deleteOk;
		    }  
		} catch (Exception e) {
			log.debug("删除文件出错！");
			return "删除文件出错";
		}
	} 
	private String deleteMkdirs(File dirFile){
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();  
		for (File file : files) {  
			deleteFile(file.getAbsolutePath());
		}
		return "";
	}
}
