package com.autonavi.analysismap.service;


public class PoiNameBuilding2Filter extends PoiNameAbstractFilter {

	protected String getRegexp() {
		return "[-0-9]{0,5}[甲乙丙丁戊][区]{0,1}$|[-0-9]{0,}[号栋]*$|[-A-Z]$";
	}

}
