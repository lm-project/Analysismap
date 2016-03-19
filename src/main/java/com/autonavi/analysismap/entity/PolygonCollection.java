package com.autonavi.analysismap.entity;

public class PolygonCollection {
	
	private int id;
	private String key;       //设施区域中文名称
	private String geom;      //设施区域geom
	private String type;      //设施区域类型：BD or QQ
	private String context;
	
	public PolygonCollection( ){
		
	}
	
	/**
	 * @param key
	 * @param geom
	 * @param type
	 */
	public PolygonCollection(String key, String geom, String type){
		super();
		this.key = key;
		this.geom = geom;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "PolygonCollection [id=" + id + ", key=" + key + ", geom="
				+ geom + ", type=" + type + ", context=" + context + "]";
	}
	
	

}
