package com.autonavi.analysismap.excutefile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.GetFiles;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:service-context.xml")
public class GetFileNameTest {

	@Test
	public void test() {
		ResponseStatus rs = new GetFiles().getFiles("AnalysismapData/mapType/北京市/17/小区/");
		System.out.println(rs.toString());
	}

}
