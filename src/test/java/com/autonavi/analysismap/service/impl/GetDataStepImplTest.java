package com.autonavi.analysismap.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.service.GetDataStepInter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context.xml")
public class GetDataStepImplTest{
	
	@Autowired
	private DealFileInter dealFile;
	
	@Autowired
	private GetDataStepInter getDataStep;

	@Test
	public void test() throws FileNotFoundException, IOException {

		String json = "{\"key\":\"小区\",\"level\":\"10\",\"bntType\":\"将Geom存入数据库\",\"mapType\":\"baidu\",\"noUpdate\":true,\"district\":\"北京市\",\"boundary\":{\"cityMinLng\":12849780.03,\"cityMinLat\":4758298.57,\"cityMaxLng\":13081648.69,\"cityMaxLat\":4993877.7}}";
		JSONObject ob = JSONObject.fromObject(json);
		GrabArgsInfo gai = new GrabArgsInfo(ob);				
		getDataStep.insertDB(gai, dealFile);
	}
}
