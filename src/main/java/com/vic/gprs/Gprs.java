package com.vic.gprs;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class Gprs {
	private static final Map<String,ChannelHandlerContext> onlineGprs=new HashMap<String, ChannelHandlerContext>();

	public synchronized static Map<String, ChannelHandlerContext> getOnlineGprs() {
		return onlineGprs;
	}

	public synchronized static boolean containsKey(String gprsID) {
		return onlineGprs.containsKey(gprsID);
	}
	public synchronized static void put(String id,ChannelHandlerContext ctx) {
		onlineGprs.put(id, ctx);
	}
	
	public synchronized static void remove(String id) {
		onlineGprs.remove(id);
	}

}
