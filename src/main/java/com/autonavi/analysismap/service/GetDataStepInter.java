package com.autonavi.analysismap.service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.entity.ResponseStatus;
import com.autonavi.analysismap.excutefile.DealFileInter;


public interface GetDataStepInter {
	public ResponseStatus getPoi(GrabArgsInfo gai);
	public ResponseStatus filterPoi(DealFileInter dealFile, GrabArgsInfo gai, DealDataInter dealData);
	public ResponseStatus getPolygon(GrabArgsInfo gai,DealFileInter dealFile, GetDataByUrlInter getDataByUrl);
	public ResponseStatus convertPolygon(GrabArgsInfo gai, DealFileInter dealFile, DealDataInter dealData);
	public ResponseStatus insertDB(GrabArgsInfo gai, DealFileInter dealFile);
	public ResponseStatus checkFile(GrabArgsInfo gai, DealFileInter dealFile);
}
