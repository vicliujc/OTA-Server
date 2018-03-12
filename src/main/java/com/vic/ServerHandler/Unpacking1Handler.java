package com.vic.ServerHandler;

import java.nio.ByteOrder;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Unpacking1Handler extends LengthFieldBasedFrameDecoder {
	
	    
	public Unpacking1Handler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) 
	{
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		// TODO Auto-generated constructor stub
	}
	    //一帧最大长度
		private final int maxFrameLength=256;
		//数据长度位置
	    private final int lengthFieldOffset=2;
	    //数据长度长度
	    private final int lengthFieldLength=1;
	    //数据长度偏移
	    private final int lengthAdjustment=0;
	    
	    
	    private final ByteBuf HEADER = Unpooled.copiedBuffer(new byte[]{(byte)0x7F, (byte)0xF7});
	    private static final int HEADER_SIZE = 6;  
	    
	    
//	    @Override
//	    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//	       if(in==null) return null;
//	       int actualLengthFieldOffset = in.readerIndex() + lengthFieldOffset;
//	       long frameLength = in.getUnsignedShort(actualLengthFieldOffset);
//
//	        if (frameLength > maxFrameLength) {
//	            return null;
//	        }
//	        int frameLengthInt = (int) frameLength;
//	       
//	    }
	    
	    
	    
	    
	  

}
