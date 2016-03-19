package com.autonavi.analysismap.mapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * <p>
 * desc:公有权限请求操作控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:qiang.cai@autonavi.com">caiqiang</a>
 * 
 * @time 2014/9/9
 */

@Controller
public class CommonMapper {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(CommonMapper.class);

	@RequestMapping("/")
	public String index1() {
		return "index";
	}
	
	@RequestMapping("/index")
	public String index2() {
		return "index";
	}
	
	@RequestMapping("/404")
	public String error() {
		return "404";
	}
	
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
	
}
