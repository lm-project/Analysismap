package com.autonavi.analysismap.excutefile;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExistFile {
	
	Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 * 判断文件是否存在
	 * @param path
	 * @return
	 */
	public boolean existFile(String path) {
		File file = new File(path);
		boolean isExist =  file.exists();
		if(isExist){
			log.debug(path+"存在！");
		}else{
			log.debug(path+"不存在！");
		}
		return isExist;
	}
}
