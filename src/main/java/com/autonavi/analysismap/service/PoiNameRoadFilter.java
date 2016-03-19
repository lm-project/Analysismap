package com.autonavi.analysismap.service;


public class PoiNameRoadFilter extends PoiNameAbstractFilter {

	public String getRegexp() {
		return ".*[路街]$";
	}

}
