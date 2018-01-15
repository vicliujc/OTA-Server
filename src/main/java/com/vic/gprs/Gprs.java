package com.vic.gprs;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class Gprs {
	private static Map<String,ChannelHandlerContext> onlineGprs=new HashMap<String, ChannelHandlerContext>();

	public static Map<String, ChannelHandlerContext> getOnlineGprs() {
		return onlineGprs;
	}

	public static void setOnlineGprs(Map<String, ChannelHandlerContext> online) {
		onlineGprs = online;
	}
	
	public static boolean containsKey(String gprsID) {
		return onlineGprs.containsKey(gprsID);
	}
	public static void put(String id,ChannelHandlerContext ctx) {
		onlineGprs.put(id, ctx);
	}

}
