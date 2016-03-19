package com.autonavi.analysismap.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.DealOriPoiRegex;
@Service
public  class PoiNameAbstractFilter implements PoiNameFilterInter {
	@Autowired
	private  DealOriPoiRegex dealOriPoiRegex;
	Logger log = Logger.getLogger(getClass());
	private Pattern pattern;
	@Override
	public String delStuffStringByRegex(String poiName) {
		String[] regexArr = dealOriPoiRegex.getRegexArr();
		for(int i = 0; i<regexArr.length; i++){
			log.info("正则 去重:"+regexArr[i]);
			Matcher matcher = getPattern(regexArr[i]).matcher(poiName);
			if(matcher.find())
				poiName = poiName.replace(matcher.group(),"");
			
		}
		return poiName;
	}

	private Pattern getPattern(String regexStr) {
		if ( pattern == null ) {
			pattern = Pattern.compile(regexStr);
		}
		return pattern;
	}

//	protected abstract String getRegexp() ;

}
