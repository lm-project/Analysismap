package com.autonavi.analysismap.excutefile;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.ResponseStatus;

/**
 * 实现文件处理
 * 
 * @author zhentao.liu
 *
 */
@Service
public class DealFileImpl implements DealFileInter {
	@Autowired
	private CreateFile createFile;
	@Autowired
	private WriteFile writeFile;
	@Autowired
	private ReadFile readFile;
	@Autowired
	private ExistFile existFile;
	@Autowired
	private GetFiles getFiles;
	@Autowired
	private ReNameFile renameFile;
	@Autowired
	private DeleteFile deleteFile;
	Logger log = Logger.getLogger(getClass());

	@Override
	public boolean existFile(String path) {
		return existFile.existFile(path);
	}

	@Override
	public String createFile(String path) {
		return createFile.createFile(path);

	}

	@Override
	public String readDataFromTxt(String path) throws IOException {
		return readFile.readDataFromTxt(path);
	}

	@Override
	public Collection<String> readLineFromTxt(String path) throws IOException {
		return readFile.readLineFromTxt(path);
	}

	@Override
	public void writeDataToTxt(String content, String path, boolean isApppend)
			throws IOException {
		writeFile.writeDataToTxt(content, path, isApppend);
	}

	@Override
	public void writeDataToTxt(Collection<String> uniqPoiNames, String path,
			boolean isApppend) throws IOException {
		writeFile.writeDataToTxt(uniqPoiNames, path, isApppend);

	}

	@Override
	public String getFilesFromMkdir(String path) {

		return getFiles.getFilesFromMkdir(path);
	}

	@Override
	public boolean reNameFile(String path1, String path2) {

		return renameFile.renameFile(path1, path2);
	}

	@Override
	public String createMkdir(String path) {
		return createFile.createMkdir(path);
	}

	@Override
	public String deleteFile(String path) {

		return deleteFile.deleteFile(path);
	}

	@Override
	public ResponseStatus getFiles(String path) {
		return getFiles.getFiles(path);
	}


}
