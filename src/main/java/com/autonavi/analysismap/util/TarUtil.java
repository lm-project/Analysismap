package com.autonavi.analysismap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Title: TarUtil
 * </p>
 * <p>
 * desc: 打包处理类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author
 * @time 2014-7-4 15:16
 * 
 */
public class TarUtil {
	private static Log log = LogFactory.getLog(TarUtil.class);

	public static File pack(File[] sources, File target) {
		TarArchiveOutputStream os = null;
		Collection<File> tmpFiles = new ArrayList<File>();
		try {
			os = new TarArchiveOutputStream(new FileOutputStream(target));
			for (File file : sources) {
				File tmpFile = new File(file.getName());
				tmpFiles.add(tmpFile);
				os.putArchiveEntry(new TarArchiveEntry(tmpFile));
				
				IOUtils.copy(new FileInputStream(tmpFile), os);
				
				os.closeArchiveEntry();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		
		for (File file : tmpFiles) {
			try{
				file.delete();
			}catch(Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
		}

		return target;
	}
}
