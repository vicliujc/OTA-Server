package com.vic.ServerHandler;

import java.util.List;

import com.vic.OTAServer.OTA_Server.Protocol;

import ErrorLogger.ErrorLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class UnpackingHandler extends ChannelInboundHandlerAdapter {
	
	 @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 try {
			 ByteBuf buf=(ByteBuf)msg;
			    byte[] msgByte=new byte[buf.readableBytes()];
			    buf.getBytes(0, msgByte);
			    List<byte[]> msgList=Protocol.unpacking(msgByte) ;
			    if (msgList==null) {
					return;
				}
			    for (byte[] b : msgList) {
			       ctx.fireChannelRead( Unpooled.copiedBuffer(b) );
				}
		} catch (Exception e) {
			ErrorLog.errorWrite("分包", e);// TODO: handle exception
		}
		   
	    }

}
