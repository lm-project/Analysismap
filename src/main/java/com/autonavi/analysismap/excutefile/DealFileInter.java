package com.autonavi.analysismap.excutefile;

import java.io.IOException;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.ResponseStatus;
/**
 * 处理文件的接口
 * 1.判断文件是否存在--------------------existFile
 * 2.创建 文件-------------------------createFile
 * 3.读取文件-------------------------readDataFromTxt
 * 4.写入文件--------------------------writeDataToTxt
 *  * @author zhentao.liu
 *
 */
@Service
public interface DealFileInter {
	public boolean existFile( String path );
	public String createFile( String path );
	public String createMkdir(String path );
	public String getFilesFromMkdir( String path );
	public ResponseStatus getFiles(String path);
	public String deleteFile( String path );
	public boolean reNameFile(String path1 , String path2);
	public String readDataFromTxt(String path) throws IOException;
	public Collection<String> readLineFromTxt(String path) throws IOException;
	public void writeDataToTxt(String content, String path, boolean isApppend) throws IOException;
	public void writeDataToTxt(Collection<String> uniqPoiNames, String path, boolean isApppend) throws IOException;
	

}
