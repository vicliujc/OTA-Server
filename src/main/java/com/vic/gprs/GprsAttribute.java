package com.vic.gprs;

import io.netty.channel.ChannelHandlerContext;

public class GprsAttribute {
	private volatile String gprs;
	private volatile ChannelHandlerContext ctx;
	public String getGprs() {
		return gprs;
	}
	public GprsAttribute(String gprs, ChannelHandlerContext ctx) {
		this.gprs = gprs;
		this.ctx = ctx;
	}
	public void setGprs(String gprs) {
		this.gprs = gprs;
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

}
