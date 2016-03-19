package com.autonavi.analysismap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.analysismap.entity.GrabArgsInfo;
import com.autonavi.analysismap.service.CalculateBoxTileInter;
import com.autonavi.analysismap.util.CalculateBdBoxTile;
/**
 * 
 * 实现CalculateBoxTileInter接口
 * 1.计算box，以json返回-------------------------------calculateBox
 * 2.计算boxTile ，以json返回--------------------------calculateTile
 * @author zhentao.liu
 *
 */
@Service
public class CalculateBoxTileImpl implements CalculateBoxTileInter {
	@Autowired
	private CalculateBdBoxTile calculateBdBoxTiel;
	@Override
	public String calculateBox(GrabArgsInfo gai) {
		
		return calculateBdBoxTiel.spliteBdBox( gai );
	}

	@Override
	public String calculateTile(String boxStr, GrabArgsInfo gai) {
		return calculateBdBoxTiel.splitBoxToTile( boxStr, gai );
	}

}
