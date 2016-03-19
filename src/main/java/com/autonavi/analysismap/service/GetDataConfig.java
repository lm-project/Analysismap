package com.autonavi.analysismap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetDataConfig {

	private GetDataConfig() {
		
	}
	private static GetDataConfig config;
	
	private static Properties properties = new Properties();
	public void loadConfig(String file ) throws FileNotFoundException, IOException {
		properties.load(new FileReader(new File(file)));

	}
	public static GetDataConfig getInstance( ) {
		if ( config == null ) {
			config = new GetDataConfig();
		}
		return config;
	}
	
	
	public void setProperty(String key,String value) {
		properties.setProperty(key, value);
	}
	public String getProperty(String key) {
		String property = properties.getProperty(key);
		int indexOf = property.indexOf("{");
		if(indexOf >0 ) {
			String value = property.substring(indexOf + 1, property.indexOf("}"));
			property = property.replaceAll("\\$\\{"+value+"\\}", properties.getProperty(value));
		}
		return property;
	}
}
