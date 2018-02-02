package com.vic.ServerHandler;

import java.util.Date;

import com.vic.OTAServer.OTA_Server.Protocol;
import com.vic.OTAServer.OTA_Server.ProtocolOperate;

import ErrorLogger.ErrorLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutHandler extends ChannelOutboundHandlerAdapter {
	
	/***
	 * 传出打印
	 */
	 @Override
	    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		 try {
		        ByteBuf buf=(ByteBuf) msg;
		        byte[] req=new byte[buf.readableBytes()];
		        buf.getBytes(0, req);
		        System.out.print(new Date().toLocaleString()+"  server send");
		        for (byte b : req) {
					System.out.print(" "+ Integer.toHexString(b&(int)0xff));
				}
		        System.out.print("\r\n");
		        ctx.writeAndFlush(msg).addListener(new ChannelFutureListener() {
		            public void operationComplete(ChannelFuture future) throws Exception {
		                if (!future.isSuccess()){
		                    future.cause().printStackTrace();
		                }
		            }
		        });
			 }catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 ErrorLog.errorWrite("传出处理器",e);
			}
	        
	    }

}
