package com.vic.ServerHandler;

import org.apache.log4j.Logger;

import com.vic.gprs.Gprs;
import com.vic.main.App1;
import com.vic.main.NettyStart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class OfflineHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(App1.class);
   @Override
   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	   if(evt instanceof IdleStateEvent) {
		   //判断事件是否为读失效
		   
		   if(((IdleStateEvent)evt).state() == IdleState.READER_IDLE)
		   ctx.close();
	   }
       ctx.fireUserEventTriggered(evt);
   }
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		try {
			if (!Gprs.getOnlineGprs().containsKey( ctx.channel().attr(NettyStart.GPRS).get().getGprs()))
                return;
			if(Gprs.getOnlineGprs().get( ctx.channel().attr(NettyStart.GPRS).get().getGprs()).channel().id().asLongText()
					==ctx.channel().id().asLongText()) {
				//System.out.println(Gprs.getOnlineGprs().containsKey(ctx.channel().attr(NettyStart.GPRS).get().getGprs()));
				Gprs.remove(ctx.channel().attr(NettyStart.GPRS).get().getGprs());
				//System.out.println(Gprs.getOnlineGprs().containsKey(ctx.channel().attr(NettyStart.GPRS).get().getGprs()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
	}

}
