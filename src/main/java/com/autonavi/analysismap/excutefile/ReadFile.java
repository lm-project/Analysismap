package com.autonavi.analysismap.excutefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 以utf-8一次读完数据
 * 返回读取数据
 * @author zhentao.liu
 *
 */

@Service
public class ReadFile{
	private Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 * 
	 * @param path     文件路径
	 * @return
	 * @throws IOException
	 */
	public String readDataFromTxt(String path) throws IOException{
		FileInputStream in = null;
		String txtContent = "";
		try {
			File file = new File(path);
			Long filelength = file.length();
			byte[] filecontent = new byte[filelength.intValue()];		
			in = new FileInputStream(file);
			in.read(filecontent);
			txtContent = new String(filecontent,"utf-8");
			log.info("读数据从："+path);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			in.close();
		}
		return txtContent;
	}
	public Collection<String> readLineFromTxt(String path) throws IOException {
		BufferedReader br = null;
		InputStreamReader ips = null;
		Collection<String> uniqPoiNames = new HashSet<String>();
		try {
			ips = new InputStreamReader(new FileInputStream(path), "utf-8"); //gb2312
			br = new BufferedReader(ips);
			String data = null;
		    while((data = br.readLine())!=null){
		        uniqPoiNames.add(data);
	            log.info(data);
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ips.close();
			br.close();
		}
		return uniqPoiNames;
	}


}
