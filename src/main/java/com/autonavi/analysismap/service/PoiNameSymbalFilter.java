package com.autonavi.analysismap.service;


public class PoiNameSymbalFilter extends PoiNameAbstractFilter {

	protected String getRegexp() {
		return "[)(·]";
	}

}
