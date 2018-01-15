package com.vic.ServerHandler;

import com.vic.gprs.Gprs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class OfflineHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx,Object evt) {
		if(evt instanceof IdleStateEvent) {
			if(Gprs.getOnlineGprs().containsValue(ctx)) {
				Gprs.remove();
			}
			ctx.close();
		}else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
