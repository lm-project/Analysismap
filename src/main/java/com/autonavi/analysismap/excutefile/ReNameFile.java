package com.autonavi.analysismap.excutefile;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ReNameFile {
	Logger log = Logger.getLogger(getClass());
	public synchronized boolean renameFile(String path1,String path2){
		try {
			File file1 = new File(path1);
			File file2 = new File(path2);
			boolean ok = file1.renameTo(file2);
			if(ok){
				log.debug(path1+"重命名为"+path2+"成功");
			}else{
				log.debug(path1+"重命名为"+path2+"失败！");
			}
			return true;
		} catch (Exception e) {
			log.debug("重命名文件出错：");
			return false;
		}
		
		
	}
}
