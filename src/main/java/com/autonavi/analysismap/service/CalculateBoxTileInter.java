package com.autonavi.analysismap.service;

import com.autonavi.analysismap.entity.GrabArgsInfo;

/**
 * 实现计算boxTile
 * 1.计算box---------------------------calculateBox
 * 2.计算tile--------------------------calculateTile
 * @author zhentao.liu
 *
 */
public interface CalculateBoxTileInter {
	/**
	 * 
	 * @param gai 
	 * @return
	 */
	public String calculateBox( GrabArgsInfo gai );
	/**
	 * 
	 * @param boxStr  城市中所包含的box
	 * @param level   级别
	 * @return
	 */
	public String calculateTile(String boxStr, GrabArgsInfo gai );
}
