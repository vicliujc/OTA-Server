package com.vic.ServerHandler;


import java.util.Date;

import org.apache.log4j.Logger;

import com.vic.OTAServer.OTA_Server.*;
import com.vic.main.App1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(ServerHandler.class);
	 @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        //ctx.fireChannelRead(msg);
		 try {
	        ByteBuf buf=(ByteBuf) msg;
	        byte[] req=new byte[buf.readableBytes()];
	        buf.readBytes(req);
	        System.out.print(new Date().toLocaleString()+"  "+Protocol.getGprsId(req));
	        for (byte b : req) {
				System.out.print(" "+ Integer.toHexString(b&(int)0xff));
			}
	        System.out.print("\r\n");
	        
	        if (Protocol.isSubcontactRequesAns(req)) {
				
			}
	        
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			 logger.error("Exception",e);
		}
	    }
}
