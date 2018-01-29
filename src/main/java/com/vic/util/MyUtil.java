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
		byte[] ans=new byte[2];
		ans[0]=intTobyteArray(crc)[2];
		ans[1]=intTobyteArray(crc)[3];
		return ans;
	}
	
	public static byte[] intTobyteArray(int num) {
		byte[] ans=new byte[4];
		ans[0]=(byte) ((num>>12) & 0xff);
		ans[1]=(byte) ((num>>8) & 0xff);
		ans[2]=(byte) ((num>>4) & 0xff);
		ans[3]=(byte) (num & 0xff);
       return ans;
       }
	
	public static byte[] reverse(byte[] object) {
		byte[] ans=new byte[object.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i]=object[ans.length-1-i];
		}
		return ans;
	}
}
