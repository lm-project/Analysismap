package com.autonavi.analysismap.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;
import com.autonavi.analysismap.service.DealDataInter;
import com.autonavi.analysismap.service.GetDataByUrlInter;
import com.autonavi.analysismap.service.GetDataStepInter;

@Service
public class GetDataStepImpl implements GetDataStepInter{
	
	@Autowired
	private GetPoiMain getPoi;
	@Autowired
	private FilterPoiMain filterPoi;
	@Autowired
	private GetPolygonMain getPolygon;
	@Autowired
	private ConvertPolygonMain converPolygon;
	@Autowired
	private InsertGeomToDB insertGeom;
	
	Logger log = Logger.getLogger(getClass());
	@Override
	public ResponseStatus getPoi(GrabArgsInfo gai) {
		return getPoi.getPoiMain(gai);
	}

	@Override
	public ResponseStatus filterPoi(DealFileInter dealFile, GrabArgsInfo gai,DealDataInter dealData) {
		
		return filterPoi.filterPoiMain(dealFile, gai, dealData);
	}

	@Override
	public ResponseStatus getPolygon( GrabArgsInfo gai,DealFileInter dealFile, GetDataByUrlInter getDataByUrl ) {
		
		return getPolygon.getPolygonMain(gai, dealFile, getDataByUrl);
	}

	@Override
	public ResponseStatus convertPolygon(GrabArgsInfo gai, DealFileInter dealFile, DealDataInter dealData) {
		return converPolygon.convertPolygonMain(dealFile, gai, dealData);
	}

	@Override
	public ResponseStatus insertDB(GrabArgsInfo gai, DealFileInter dealFile) {
		
		return insertGeom.insertGeomToDbMain(dealFile, gai);
		
	}

	@Override
	public ResponseStatus checkFile(GrabArgsInfo gai, DealFileInter dealFile) {
		return  dealFile.getFiles(gai.getDataPath());
	}

	

}
