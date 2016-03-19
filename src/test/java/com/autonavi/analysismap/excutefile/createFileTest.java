package com.autonavi.analysismap.excutefile;

import org.junit.Test;

import com.autonavi.analysismap.excutefile.CreateFile;

public class createFileTest {

	@Test
	public void test() {
		
		CreateFile cr = new CreateFile();
		System.out.println(cr.createFile("D:/facility/baidu/北京市/10.0/大学/processing"));
	}

}
