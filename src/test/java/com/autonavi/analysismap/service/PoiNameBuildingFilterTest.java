package com.autonavi.analysismap.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class PoiNameBuildingFilterTest {

	private PoiNameFilterInter bfilter = new PoiNameBuildingFilter();
	List<PoiNameFilterInter> filters = Arrays.asList(new PoiNameFilterInter[] {
			new PoiNameBuildingFilter(), new PoiNameBuilding2Filter() });

	@Test
	public void test() {
//		assertEquals("AA", bfilter.delStuffStringByRegex("AA19号楼"));
//		assertEquals("AA", bfilter.delStuffStringByRegex("AA号楼"));
//		assertEquals("AA19号院blalba",
//				bfilter.delStuffStringByRegex("AA19号院blalba"));

		assertEquals("AA", doit(1 ,"AA19号楼21栋",filters));
		assertEquals("AA", doit(1 ,"AA19号楼21",filters));
		assertEquals("AA", doit(1 ,"AA19号楼21号",filters));
	}

	private String doit(int idx, String poiName,List<PoiNameFilterInter> filters) {
		System.out.println(poiName);
		if (idx < 0) {
			return poiName;
		}
		return doit(idx - 1, filters.get(idx).delStuffStringByRegex(poiName),filters);
	}

}
