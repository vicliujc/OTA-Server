package com.vic.OTAServer.OTA_Server;

import com.vic.gprs.OTAMap;
import com.vic.gprs.OTATask;
import com.vic.main.App1;
import com.vic.mybatis.OTADao;
import com.vic.mybatis.OTAMsg;
import com.vic.mybatis.SqlMsg;

import ErrorLogger.ErrorLog;

public class ProtocolOperate {
	static final int MAX_SUBRQSANS=10;
	
	
	/***
	 * 区分传入数据类型 
	 * @param Msg
	 */
	public static void operate(byte[] msg) {
		try {
			if (msg[2] == (byte)0xF1) {
			     switch (msg[10]) {
			        case (byte) 0xF2:
				       subcontactRequestAns(msg);
				       break;
	                case (byte) 0xF3:
	                	subcontactRequest(msg);
			    	   break;
			        default:
				       break;
			     }
			}
		} catch (Exception e) {
			ErrorLog.errorWrite("传入数据", e);
			// TODO: handle exception
		}
		
	}
	
	/***
	 * 处理分包请求的回复数据
	 * @param msg
	 */
	public static void  subcontactRequestAns(byte[] msg) {
		switch (msg[11]) {
		case (byte)0x80:
			try {
				subcontactRequestAnsSuccess(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ErrorLog.errorWrite("分析分包请求", e);
			}
			break;
		default:
			try {
				subcontactRequestAnsFail(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ErrorLog.errorWrite("分析分包请求", e);
			}
			return;
		}
	}
	
	/***
	 * 判断分包发送是否接受正确
	 * @param msg
	 */
	public static void  subcontactRequest(byte[] msg) {
		switch (msg[11]) {
		case (byte)0x80:
			try {
				subcontactRequestSuccess(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ErrorLog.errorWrite("分包发送回复", e);
			}
			break;
		default:
			try {
				subcontactRequestFail(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ErrorLog.errorWrite("分包发送回复", e);
			}
			return;
		}
	}
	
	/***
	 * 上一包发送成功 开始发送下一包
	 * @param msg
	 */
	public static void subcontactRequestSuccess(byte[] msg) {
		String gprsid=Protocol.getGprsId(msg);
		OTATask.get(gprsid).send(true);
	}
	
	/***
	 * 上一包发生失败 重新发送
	 * 80H：传送成功； 
       01H：序号错误；
       02H：接收的数据量不对；
       03H：接收超时
	 * @param msg
	 */
	public static void subcontactRequestFail(byte[] msg) throws Exception{
		String errorMsg;
		switch (msg[11]) {
           case (byte)0x01:
			 errorMsg="序号错误";
			 break;
           case (byte)0x02:
        	   errorMsg="接收的数据量不对";
	         break;
           case (byte)0x03:
        	   errorMsg="接收超时";
		    break;
		   default:
			   errorMsg="未知错误："+msg[11];
			break;
		}
		String gprsid=Protocol.getGprsId(msg);
		OTAMap.get(gprsid).setResult_info("第"+OTATask.get(gprsid).getNow_pack_num()+"失败："+errorMsg);
		OTATask.get(gprsid).send(false);
	}
	
	/***
	 * 分包请求通过 开始传包
	 * @param msg
	 * @throws Exception
	 */
	public static void subcontactRequestAnsSuccess(byte[] msg) throws Exception{
		String gprsid=Protocol.getGprsId(msg);
		OTATask.get(gprsid).send(true);
	}
	
	/***
	 * 分包发送请求失败 
	 * 01H：数据过长；
    02H：帧类型错误；
    03H：数据长度与数据包数目不匹配；
    04H：接收端忙；
    81H：预留
    82H：固件类型错误
    83H：版本错误
	 * @param msg
	 */
	public static void subcontactRequestAnsFail(byte[] msg) throws Exception{
		String errorMsg;
		switch (msg[11]) {
           case (byte)0x01:
			 errorMsg="数据过长";
			 break;
           case (byte)0x02:
        	   errorMsg="帧类型错误";
	         break;
           case (byte)0x03:
        	   errorMsg="数据长度与数据包数目不匹配";
		    break;
	       case (byte)0x81:
	    	   errorMsg="预留";
		     break;
	       case (byte)0x82:
	    	   errorMsg="固件类型错误";
		     break;
	       case (byte)0x83:
	    	   errorMsg="版本错误";
		     break;
		   default:
			   errorMsg="未知错误："+msg[11];
			break;
		}
		String gprsid=Protocol.getGprsId(msg);
		SqlMsg sqlMsg=OTAMap.get(gprsid);
		sqlMsg.setState(3);
		sqlMsg.setResult_info("分包请求失败："+errorMsg);
		OTADao otaDao=(OTADao) App1.ac.getBean("otaDao");
		otaDao.transferStatus(sqlMsg);
	}

}
