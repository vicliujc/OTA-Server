package com.vic.gprs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

public class Gprs {
	private static final ConcurrentHashMap<String,ChannelHandlerContext> onlineGprs=new ConcurrentHashMap<String, ChannelHandlerContext>();

	public  static Map<String, ChannelHandlerContext> getOnlineGprs() {
		return onlineGprs;
	}

	public static boolean containsKey(String gprsID) {
		return onlineGprs.containsKey(gprsID);
	}
	public static void put(String id,ChannelHandlerContext ctx) {
		onlineGprs.put(id, ctx);
	}
	
	public static void remove(String id) {
		onlineGprs.remove(id);
	}
	
	public static ChannelHandlerContext getCTX(String id) {
		return onlineGprs.get(id);
	}

}
