package com.vic.ServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.vic.gprs.*;

import java.util.Map;

import com.vic.OTAServer.OTA_Server.*;

public class Register extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) {
		ByteBuf buf=(ByteBuf) msg;
		byte[] rep=new byte[buf.readableBytes()];
		buf.readBytes(rep);
		String gprsId=Protocol.getGprsId(rep);
		registerGprs(ctx, gprsId);
	}
	
	public void registerGprs(ChannelHandlerContext ctx,String gprsID) {
		
		if (Gprs.containsKey(gprsID)) {
			if (ctx.channel().id()==Gprs.getOnlineGprs().get(gprsID).channel().id()) {
				return;
			}
		}
		Gprs.put(gprsID,ctx);
	}
}
