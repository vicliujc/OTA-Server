package com.vic.OTAServer.OTA_Server;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.vic.gprs.Gprs;
import com.vic.mybatis.OTAMsg;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class OtaServer implements Runnable{
	public static int UNPACK_FRAME_LENGTH;//不含bbc crc校验 表头长度
	public int  packNum;
	public static Logger logger=Logger.getLogger(OtaServer.class);
	// 读取文件后的全部byte[] 
	private byte[] doc;
	private OTAMsg otaMsg;
	
	private String path;
	private int now_pack_num;
	
	public OtaServer(OTAMsg otaMsg) {
		this.path=otaMsg.getBin_file_path();
		try {
			getDocNow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("OtaServer",e);
			e.printStackTrace();
		}
	}
	
	
	//外部获取文档byte[]
	public byte[] getDoc() {
		return doc;
	}

	//读取文档
	private void getDocNow() throws Exception {
		DocRead docRead=new DocRead(path);
		doc=docRead.getDocBin();
		packNum=(int) Math.ceil( (double)doc.length/UNPACK_FRAME_LENGTH);
	}
	
	//读取包
	public int getPackNum() {
		double x=(double)(doc.length)/UNPACK_FRAME_LENGTH;
		packNum=(int) Math.ceil(x);
		return packNum;
	}
	
	/***
	 * 输入需要的包数 传出包
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public byte[] tanceforByteNow(int num) throws Exception {
		if(num>packNum) throw new Exception("不存在传输的包数");
		if (doc.length==0) throw new Exception("未传入文件");
	    byte[] now_pack=new byte[UNPACK_FRAME_LENGTH];
	    System.arraycopy(doc, UNPACK_FRAME_LENGTH*(num-1), now_pack, 0, UNPACK_FRAME_LENGTH);
	    return now_pack;
	} 
	
	
    public void run() {
    	//先发分包请求
    	try {
    	byte[] subcontact=Protocol.subcontractRequest(doc.length, packNum,(byte)otaMsg.gettarget(), (byte) otaMsg.getFirmware(), (byte) otaMsg.getVersion(), otaMsg.getSub());
    	ChannelHandlerContext ctx=Gprs.getCTX(otaMsg.getGprs_id());
    	ctx.write(Unpooled.copiedBuffer(subcontact));
    	}
    	catch (Exception e) {
			logger.error("OtaServer run", e);// TODO: handle exception
		}
    }


}
