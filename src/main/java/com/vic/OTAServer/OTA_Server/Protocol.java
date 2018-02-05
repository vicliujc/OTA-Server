package com.vic.OTAServer.OTA_Server;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import com.vic.util.*;

public class Protocol {
	private static final int MIN_FRAME_LENGHT = 11;
	
	/***
	 * 从上传数据中获得GprsID
	 * @param msg
	 * @return
	 */
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
	
	public static boolean isRealData(byte[] data) {
		
		byte bbc=0x00;
		for (int i = 0; i < data.length-1; i++) {
			bbc ^= (byte)data[i];
		}
		
		return (byte)bbc==(byte)data[data.length-1];
	}
	
	/***
	 *添加表头、数据长度、异或
	 * @return
	 */
	public static byte[] fullSend(byte[] data) {
		byte[] head= {(byte) 0x7f,(byte) 0xf7};
		byte[] length = { (byte) (data.length+1)};
		if (data.length>255) length[0]= 0x00;
		byte[] allMsg=new byte[data.length+4];
		byte bbc=0x00;
		System.arraycopy(head, 0, allMsg, 0, 2);
		System.arraycopy(length, 0, allMsg, 2, 1);
		System.arraycopy(data, 0, allMsg, 3, data.length);
		for (int i = 0; i < allMsg.length-1; i++) {
			bbc ^= allMsg[i];
		}
		allMsg[allMsg.length-1]=bbc;
		return allMsg;
	}
	
	/***
	 * 分包请求数据帧
	 * @param docLength
	 * @param num
	 * @param object
	 * @param fimwareType
	 * @param version
	 * @param subnum
	 * @return
	 */
	public static byte[] subcontractRequest(int docLength,int num,byte object,byte fimwareType ,byte version,byte[] subnum) {
		byte[] head= {(byte) 0xf2,0x20};
		byte[] length=MyUtil.intTobyteArray(docLength);
		byte[] packNum= { MyUtil.intTobyteArray(num)[2],MyUtil.intTobyteArray(num)[3]};
		byte[] ver= {object,fimwareType,version};
		byte[] allMsg;
		if(object==3) {
       allMsg=new byte[14];}
		else {
			allMsg=new byte[11];
		}
		System.arraycopy(head, 0, allMsg, 0, 2);
		System.arraycopy(length, 0, allMsg, 2, 4);
		System.arraycopy(packNum, 0, allMsg, 6, 2);
		System.arraycopy(ver, 0, allMsg, 8, 3);
		if(object==3) {
		System.arraycopy(subnum, 0, allMsg, 11, 3);}
		return fullSend(allMsg);
	}
	
	public static boolean isSubcontactRequesAns(byte[] data) 
	{
		return  data[10] == (byte)0xf2;
	}
	
	/***
	 * 分包数据
	 * @param data
	 * @return
	 */
	public static byte[] subcontractMsg(byte[] data) {
		byte[] dataLength=new byte[2];
		
		byte[] crc=CRC16.calculate(data);
		if(data.length>252) 
		{
		    dataLength[0] = MyUtil.intTobyteArray(data.length+3)[2] ;
	     	dataLength[1] = MyUtil.intTobyteArray(data.length+3)[3] ;
		    byte[] allMsg=new byte[data.length+5];
	    	allMsg[0]=(byte) 0xf3;
	    	System.arraycopy(dataLength, 0, allMsg, 1, 2);
	    	System.arraycopy(data, 0, allMsg, 3, data.length);
		    System.arraycopy(crc, 0, allMsg, data.length+3, 2);
		    return fullSend(allMsg);
		}
		else {
			byte[] allMsg=new byte[data.length+3];
			allMsg[0]=(byte) 0xf3;
			System.arraycopy(data, 0, allMsg, 1, data.length);
			System.arraycopy(crc, 0, allMsg, data.length+1, 2);
			return fullSend(allMsg);
		}
	}
	
	
	public static List<byte[]> unpacking(byte[] msg) throws Exception{
		List<byte[]> ans=new ArrayList<byte[]>();
		ans.clear();
		if(msg.length<= msg[2]+3) {
			ans.add(msg);
			return ans;
		}
		
		for (int i = 2; i < msg.length; i++) {
			if(msg[i]== (byte)0x7F) {
				if(i+1 <msg.length && msg[i+1]== (byte)0xF7) {
					byte[] ans1=new byte[i];
					System.arraycopy(msg, 0, ans1, 0, i);
					ans.add(ans1);
					byte[] ans2=new byte[msg.length-i];
					System.arraycopy(msg, i, ans2, 0, msg.length-i);
					ans.add(ans2);
				}
			}
		}
		return ans;
	}
	 

}
