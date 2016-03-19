package com.autonavi.analysismap.service;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class GetDataConfigTest {

	@Test
	public void test() throws FileNotFoundException, IOException {
		GetDataConfig instance = GetDataConfig.getInstance();
		instance.loadConfig("src/main/resources/config.properties");
		assertEquals("", instance.getProperty("storagePath"));
	}

}
