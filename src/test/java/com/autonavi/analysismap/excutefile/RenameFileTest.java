package com.autonavi.analysismap.excutefile;

import org.junit.Test;

import com.autonavi.analysismap.excutefile.ReNameFile;

public class RenameFileTest {

	@Test
	public void test() {
		boolean ok = new ReNameFile().renameFile("D:/facility/baidu/上海市/10/小区/0", 
				"D:/facility/baidu/上海市/10/小区/covertGeomAndSaveDB");
		System.out.println(ok);
	}

}
