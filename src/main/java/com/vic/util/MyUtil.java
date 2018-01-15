package com.vic.util;

public class MyUtil {
	
	/**
	 * byte[] 转 int
	 */
	
	public static long byteArraytoInt(byte[] n) {
		byte[] x=new byte[4];
		System.arraycopy(n, 0, x, 4-n.length, n.length);
		long ans=((x[0] & 0xFF)<<24)+((x[1] & 0xFF)<<16)+((x[2] & 0xFF)<<8)+(x[3] & 0xFF);
		return ans;
	}

	public static byte[] intToUInt16Bytes(int crc) {
		// TODO Auto-generated method stub
		return null;
	}

}
