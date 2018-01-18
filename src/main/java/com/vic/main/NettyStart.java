package com.vic.main;

import java.util.concurrent.TimeUnit;

import com.vic.ServerHandler.OfflineHandler;
import com.vic.ServerHandler.Register;
import com.vic.ServerHandler.ServerHandler;
import com.vic.gprs.GprsAttribute;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

public class NettyStart {
	private int port;
	public final static AttributeKey<GprsAttribute> GPRS = AttributeKey.valueOf("GprsAttribute");
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start() throws Exception{
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workGroup=new NioEventLoopGroup();
		
		try {
			ServerBootstrap sb=new ServerBootstrap();
			sb.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1000)
            .option(ChannelOption.SO_REUSEADDR, true)
            
            .childHandler(new ChannelInitializer<Channel>() {
            	@Override
            	public void initChannel(Channel ch) throws Exception{
            		ChannelPipeline p=ch.pipeline();
            		p.addLast(new IdleStateHandler(1,0, 0,TimeUnit.MINUTES));
            		p.addLast("Register",new Register());
            		p.addLast(new ServerHandler());
            		p.addLast(new OfflineHandler());
            		
            	}
			}).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			
			ChannelFuture future=sb.bind(port).sync();
			System.out.println("开始监听:"+port);
			future.channel().closeFuture().sync();
			
			
		} finally {
			// TODO: handle finally clause
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}

}
