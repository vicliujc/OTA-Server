package com.vic.ServerHandler;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.Protocol;
import com.vic.gprs.Gprs;
import com.vic.gprs.GprsAttribute;
import com.vic.main.App1;
import com.vic.main.NettyStart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Register extends ChannelInboundHandlerAdapter{
    private ApplicationContext ac=App1.ac;
    private static Logger logger = Logger.getLogger(Register.class); 
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		
		ByteBuf buf=(ByteBuf) msg;
		byte[] rep=new byte[buf.readableBytes()];
		buf.getBytes(0, rep);
		String gprsId=Protocol.getGprsId(rep);
		if (gprsId == null) {
			return;
		}
		if(registerGprs(ctx, gprsId))
		{
			ctx.channel().pipeline().remove("Register");
		}
		ctx.fireChannelRead(msg);  
	}
	
	public boolean registerGprs(ChannelHandlerContext ctx,String gprsID) throws Exception{
		GprsAttribute ga=(GprsAttribute) ac.getBean("gprsAttribute");
		ga.setGprs(gprsID);
		ga.setCtx(ctx);
		ctx.channel().attr(NettyStart.GPRS).set(ga);
		Gprs.put(gprsID,ctx);
		return true;
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        logger.error(cause);
    }
}
