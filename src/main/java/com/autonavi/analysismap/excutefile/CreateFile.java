package com.autonavi.analysismap.excutefile;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
/**
 * 1.判断文件是否存在---------------------------existFile
 * 2.创建文件--------------------------------createFile
 * @author zhentao.liu
 *
 */
@Service
public class CreateFile {
	Logger log = Logger.getLogger(getClass());
	
	private File file;
	private boolean haveFile(String path){
		file = new File(path);
		if(file.exists()){
			log.debug("文件已存在！");
			return true;
		}
		return false;
	}
	
	/**
	 * 创建文件或者目录 
	 * @param path  文件路径
	 */
	public synchronized String createFile(String path) {
		try {
			if(haveFile(path)) return "文件已创建";
			File mkdir =  file.getParentFile();
			if(!mkdir.exists()){
				mkdir.mkdirs();
			}
			file.createNewFile();
			log.info("创建文件:"+file.getAbsolutePath());
			return "文件创建成功！";
		} catch (Exception e) {
			log.debug("文件创建失败！");
			return "文件创建失败！";
		}
	}
	public synchronized String createMkdir(String path) {
		try {
			if(haveFile(path)) return "目录已创建";
			file.mkdirs();
			log.info("创建文件目录:"+file.getAbsolutePath());
			return "目录创建成功！";
		} catch (Exception e) {
			log.debug("目录创建失败！");
			return "目录创建失败！";
		}
	}

	
}
