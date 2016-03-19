package com.autonavi.analysismap.orm.impl;

import java.io.Serializable;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: MyBatis DAO基础类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-3-28 13:33
 * 
 */

public class BasicMyBatisDao extends SqlSessionDaoSupport{
	
	public int save(String key, Object object) {
		return getSqlSession().insert(key, object);
	}
	
	public void delete(String key, Serializable id) {
		getSqlSession().delete(key, id);
	}
	
	public int update(String key, Object object) {
		return getSqlSession().update(key, object);
	}

	public void delete(String key, Object object) {
		getSqlSession().delete(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Object params) {
		System.out.println(key +"," + params);
		return (T) getSqlSession().selectOne(key, params);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key) {
		return getSqlSession().selectList(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Object params) {
		return getSqlSession().selectList(key, params);
		
	}
}
