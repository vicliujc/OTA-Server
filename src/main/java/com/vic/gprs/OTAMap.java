package com.vic.gprs;

import java.util.concurrent.ConcurrentHashMap;

import com.vic.mybatis.SqlMsg;

public class OTAMap {
	
	private static final ConcurrentHashMap<String, SqlMsg> OTANOW=new ConcurrentHashMap<String, SqlMsg>();
	
	public static void put(String gprsid, SqlMsg sqlMsg) {
		OTANOW.put(gprsid, sqlMsg);
	}
	
	public static void remove(String gprsid) {
		OTANOW.remove(gprsid);
	}
	
	public static SqlMsg get(String gprsid) {
	    return	OTANOW.get(gprsid);
	}

}
