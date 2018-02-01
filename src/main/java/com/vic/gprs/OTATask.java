package com.vic.gprs;

import java.util.concurrent.ConcurrentHashMap;

import com.vic.OTAServer.OTA_Server.OtaServer;
import com.vic.mybatis.SqlMsg;

public class OTATask {
	private static final ConcurrentHashMap<String , OtaServer> OTADATA =new ConcurrentHashMap<String, OtaServer>();
	
	public static void put(String gprsid, OtaServer otaServer) {
		OTADATA.put(gprsid, otaServer);
	}
	
	public static void remove(String gprsid) {
		OTADATA.remove(gprsid);
	}
	
	public static OtaServer get(String gprsid) {
	    return	OTADATA.get(gprsid);
	}
	
	public static int size() {
		return OTADATA.size();
	}

}
