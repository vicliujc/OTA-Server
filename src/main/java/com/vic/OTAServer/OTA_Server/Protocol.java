package com.vic.OTAServer.OTA_Server;

import com.vic.util.*;

public class Protocol {
	private static final int MIN_FRAME_LENGHT = 11;
	
	public static String getGprsId(byte[] msg) {
		if (msg.length<MIN_FRAME_LENGHT) {
			return null;
		}
		byte[] gprs=new byte[6];
	    System.arraycopy(msg, 4, gprs, 0, 6);
	    byte[] n=new byte[3];
	    System.arraycopy(gprs, 3, n, 0, 3);
		String gprsId=""+(char)(gprs[0]&0xff)+(char)(gprs[1]&0xff)+(char)(gprs[2]&0xff);
		String idNum=""+MyUtil.byteArraytoInt(n);
		gprsId += String.format("%6s", idNum.trim()).replaceAll("\\s", "0");
		return gprsId;
	}
	
	 

}
