package com.autonavi.analysismap.excutefile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
/**
 * 
 * 写数据
 * @author zhentao.liu
 *
 */

@Service
public class WriteFile{

	private Logger log = Logger.getLogger(getClass());

	private BufferedWriter getOutput(String path,boolean isApppend) throws IOException {
		BufferedWriter out = null;
		File file = new File(path);
		FileOutputStream outContent = new FileOutputStream(file, isApppend);
		out = new BufferedWriter(new OutputStreamWriter(outContent, "utf-8"));
		return out;
	}

	public void writeDataToTxt(String content, String path, boolean isApppend) throws IOException {
		BufferedWriter out = getOutput(path,isApppend);
		try {
			out.write(content);
		} finally {
			out.flush();
			out.close();
		}
	}
	public void writeDataToTxt(Collection<String> uniqPoiNames, String path,boolean isApppend) throws IOException {
		BufferedWriter out = getOutput(path,isApppend);
		try {
			for (String name : uniqPoiNames) {
				out.write(name);
				out.newLine();
			}
		} finally {
			out.flush();
			out.close();
		}
	}


}
