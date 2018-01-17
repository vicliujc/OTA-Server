package com.vic.OTAServer.OTA_Server;

public class OtaServer {
	public static int UNPACK_FRAME_LENGTH;//不含bbc crc校验 表头长度
	public int  packNum;
	private byte[] doc;
	private String path;
	private int now_pack_num;
	
	public OtaServer(String path){
		this.path=path;
	}
	
	public void getDoc() throws Exception {
		DocRead docRead=new DocRead(path);
		doc=docRead.getDocBin();
	}
	
	public int getPackNum() {
		double x=(double)(doc.length)/UNPACK_FRAME_LENGTH;
		packNum=(int) Math.ceil(x);
		return packNum;
	}
	
	public byte[] tanceforByteNow(int num) throws Exception {
		if(num>packNum) throw new Exception("不存在传输的包数");
		if (doc.length==0) throw new Exception("未传入文件");
	    byte[] now_pack=new byte[UNPACK_FRAME_LENGTH];
	    System.arraycopy(doc, UNPACK_FRAME_LENGTH*(num-1), now_pack, 0, UNPACK_FRAME_LENGTH);
	    return now_pack;
	}
	
	

}
