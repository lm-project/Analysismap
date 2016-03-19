package com.autonavi.analysismap.excutefile;

import org.junit.Test;

import com.autonavi.analysismap.excutefile.DeleteFile;

public class DeleteFileTest {

	@Test
	public void test() {
		System.out.println(new DeleteFile().deleteFile("D:/facility/baidu/陕西省/西安市/10/医院/"));
	}

}
