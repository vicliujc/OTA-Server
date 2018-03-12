package com.vic.main;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vic.ServerHandler.OfflineHandler;
import com.vic.ServerHandler.OutHandler;
import com.vic.ServerHandler.Register;
import com.vic.ServerHandler.ServerHandler;
import com.vic.ServerHandler.UnpackingHandler;
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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

public class NettyStart implements Runnable {
	private int port;
	public final static AttributeKey<GprsAttribute> GPRS = AttributeKey.valueOf("GprsAttribute");
	private static Logger logger = Logger.getLogger(NettyStart.class);
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run(){
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
            		p.addLast("outHandler",new OutHandler());
            		p.addLast(new IdleStateHandler(30,0, 0,TimeUnit.MINUTES));
            		p.addLast("Unpacking1",new LengthFieldBasedFrameDecoder(256,
            	            2, 1, 0, 0));
            		p.addLast("Unpacking",new UnpackingHandler());
            		p.addLast("Register",new Register());
            		p.addLast(new ServerHandler());
            		p.addLast(new OfflineHandler());
            		
            	}
			}).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			
			ChannelFuture future=sb.bind(port).sync();
			System.out.println("开始监听:"+port);
			future.channel().closeFuture().sync();
			
			
		}catch(Exception ex) {
			logger.error("NETTY START",ex);
			ex.printStackTrace();
		}
		finally {
		}
			// TODO: handle finally clause
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}

