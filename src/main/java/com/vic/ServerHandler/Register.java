package com.vic.ServerHandler;

import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.Protocol;
import com.vic.gprs.Gprs;
import com.vic.gprs.GprsAttribute;
import com.vic.main.App1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class Register extends ChannelInboundHandlerAdapter{
    private ApplicationContext ac=App1.ac;
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) {
		ByteBuf buf=(ByteBuf) msg;
		byte[] rep=new byte[buf.readableBytes()];
		buf.readBytes(rep);
		String gprsId=Protocol.getGprsId(rep);
		registerGprs(ctx, gprsId);
	}
	
	public void registerGprs(ChannelHandlerContext ctx,String gprsID) {
		GprsAttribute ga=(GprsAttribute) ac.getBean("gprsAttribute");
		final AttributeKey<GprsAttribute> GprsCTX = AttributeKey.valueOf("ga");
		
		if (Gprs.containsKey(gprsID)) {
			if (ctx.channel().id()==Gprs.getOnlineGprs().get(gprsID).channel().id()) {
				return;
			}
		}
		Gprs.put(gprsID,ctx);
	}
}
