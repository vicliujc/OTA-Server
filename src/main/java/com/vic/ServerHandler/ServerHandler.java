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
	 /***
	  * 打印传入服务器的数据 交由处理类ProtocolOperate.operate()处理
	  */
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        //ctx.fireChannelRead(msg);
		 try {
	        ByteBuf buf=(ByteBuf) msg;
	        byte[] req=new byte[buf.readableBytes()];
	        buf.readBytes(req);
	        boolean ansIsReal=Protocol.isRealData(req);
	        System.out.print(new Date().toLocaleString()+"  "+Protocol.getGprsId(req));
            if (!ansIsReal) {
            	 System.out.print(" 未知数据  ");
			}
	        for (byte b : req) {
				System.out.print(" "+ Integer.toHexString(b&(int)0xff));
			}
	        System.out.print("\r\n");
	        if(ansIsReal)
	        ProtocolOperate.distributionDataThread(req);
	        
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			 logger.error("Exception",e);
		}
	    }
}
