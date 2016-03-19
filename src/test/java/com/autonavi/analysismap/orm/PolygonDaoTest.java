package com.autonavi.analysismap.orm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.analysismap.entity.PolygonCollection;
import com.autonavi.analysismap.orm.impl.PolygonDaoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:service-context.xml")
public class PolygonDaoTest {

	@Autowired
	private PolygonDaoImpl polygonDao;
	@Rollback(value=true)
	@Test
	public void testSelect() {
		PolygonCollection polygon = new PolygonCollection();
		polygon.setKey("翰林景园");
		polygon.setType("baidu");
		polygon.setContext("");
		polygon.setGeom("MULTIPOLYGON(((383205.6788894019 106455.35031397686,383206.1964060017 106455.20176083791,383207.1652427119 106457.26306202459,383206.64776688593 106457.41870172732,383207.06932667067 106458.49404691378,383207.4915012242 106460.2320062806,383207.9444236059 106460.26334870965,383211.6228141673 106458.35198856355,383214.847851994 106456.6132392804,383215.7811463107 106455.73424464607,383216.0395497282 106455.44809716538,383216.23318372364 106455.04985022012,383216.2005518409 106454.71832454416,383215.62128343503 106453.75258050073,383214.5527681694 106452.09562578278,383214.0671361005 106451.42602421383,383213.84002261574 106450.56382837944,383213.649245753 106450.16640649401,383213.5197945516 106450.05028247235,383213.06698613125 106449.96435492829,383212.6789162428 106449.96172764142,383211.61556390085 106450.29318132966,383211.45375737874 106450.18050823966,383209.71145365713 106450.93299251227,383209.42012652056 106450.68637987392,383208.25579525455 106450.87760813424,383207.74202859576 106451.13035905737,383207.32147803245 106451.12802237814,383206.7069808923 106451.32272872573,383206.5776462128 106451.4048564685,383205.9343520771 106451.58871675862,383205.18993430183 106451.27524612944,383204.7694128968 106451.38478518627,383204.8996945736 106452.35766107278,383204.86779349844 106452.86882698102,383204.6740377916 106453.29277767033,383205.6788894019 106455.35031397686,383205.6788894019 106455.35031397686)))");
		polygonDao.insert(polygon);
	}

}
