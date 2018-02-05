package com.vic.OTAServer.OTA_Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.vic.gprs.OTAMap;
import com.vic.gprs.OTATask;
import com.vic.gprs.OTATimeOut;
import com.vic.main.App1;
import com.vic.mybatis.OTADao;
import com.vic.mybatis.OTAMsg;
import com.vic.mybatis.SqlMsg;

import ErrorLogger.ErrorLog;

public class ProtocolOperate {
	private static final int MAX_DATAPROCESSING_NUM=10;
	
	/***
	 * 创建线程处理传入数据
	 */
	public static void distributionDataThread(byte[] msg) {
		try {
			ExecutorService servicePool = Executors.newFixedThreadPool(MAX_DATAPROCESSING_NUM);
			DataProcessingThread dataProcessingThread=new DataProcessingThread(msg);
			servicePool.submit(dataProcessingThread);
		} catch (Exception e) {
			ErrorLog.errorWrite("数据处理创建线程", e);
		}
	}
	
	
	/***
	 * 区分传入数据类型 
	 * @param Msg
	 */
	public static void operate(byte[] msg) {
		try {
			if (msg[3] == (byte)0xF1) {
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
			
			if (msg[3]== (byte) 0x25) {
				UpgateResult(msg);
			}
		} catch (Exception e) {
			ErrorLog.errorWrite("传入数据", e);
			// TODO: handle exception
		}
	}
	
	/***
	 * 判断升级结果帧类型
	 * @param msg
	 */
	private static void UpgateResult(byte[] msg) {
		switch (msg[10]) {
		case (byte)0x01:
			try {
				updateStm32Result(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorLog.errorWrite("升级结果", e);
			}
			break;
		case (byte)0x02:
			updateBuletoothResult(msg);
			break;
		case (byte)0x03:
			updateSubResult(msg);
			break;
		default:
			break;
		}
	}

	/***
	 * 判断从机升级结果
	 *  0x00:未进行升级
       0x01:升级失败
       0x80:升级成功
	 * @param msg
	 */
	private static void updateSubResult(byte[] msg) {
		try {
			byte[] subAns=new byte[msg.length-11];
			System.arraycopy(msg, 11, subAns, 0, subAns.length);
			String ansSuccess = "";
			String ansNotUpadate="";
			String ansFail="";
			boolean allAns=true;
			for (int i = 0; i < subAns.length; i++) {
				switch (subAns[i]) {
				case (byte)0x80:
					ansSuccess += ""+(i+1)+"/" ;
					break;
				case (byte)0x00:
					ansNotUpadate += ""+(i+1)+"/" ;
					break;
				case (byte)0x01:
					ansFail += ""+(i+1)+"/" ;
				    allAns=false;
					break;
				default:
					break;
				}
			}
			
			if (allAns) {
				updateSubSuccess(ansSuccess,ansNotUpadate,ansFail,msg);
			}
			else {
				updateSubFail(ansSuccess,ansNotUpadate,ansFail,msg);
			} 
		} catch (Exception e) {
			ErrorLog.errorWrite("从机升级判断", e);
		}
		
	}

	/***
	 * 从机升级有失败
	 * @param ansSuccess
	 * @param ansNotUpadate
	 * @param ansFail
	 * @param msg
	 */
	private static void updateSubFail(String ansSuccess, String ansNotUpadate, String ansFail, byte[] msg) {
		// TODO Auto-generated method stub
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(3);
			OTATask.get(gprsid).statewrite("升级失败从机："+ansFail+";"+" 从机升级成功："+ansSuccess+"; 未从机升级："+ansNotUpadate+";");
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("从机成功升级", e);
			// TODO: handle exception
		}
	}

	/***
	 * 从机全部升级成功或未升级
	 * @param ansSuccess
	 * @param ansNotUpadate
	 * @param ansFail
	 * @param msg
	 */
	private static void updateSubSuccess(String ansSuccess, String ansNotUpadate, String ansFail,byte[] msg) {
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(2);
			OTATask.get(gprsid).statewrite("从机升级成功："+ansSuccess+"; 未从机升级："+ansNotUpadate+"; 升级失败从机："+ansFail+";");
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("从机成功升级", e);
			// TODO: handle exception
		}
	}

	/***
	 * 判断蓝牙升级结果
	 * @param msg
	 */
	private static void updateBuletoothResult(byte[] msg) {
		switch (msg[11]) {
		case (byte)0x80:
			UpdateBluetoothSuccess(msg);
			break;
		default:
		   UpdateBluetoothFail(msg);
			break;
		}
		
	}

	/***
	 * 升级蓝牙主机失败
	 * 0x00:未进行升级
       0x01:升级失败
       0x80:升级成功
       0x81:设备离线（针对蓝牙）
       0x82:固件类型错误（针对蓝牙）
       0x83:软件版本错误（针对蓝牙）
	 * @param msg
	 */
	private static void UpdateBluetoothFail(byte[] msg) {
		String error;
		switch (msg[11]) {
		case (byte) 0x01:
			error="升级失败";
			break;
		case (byte) 0x00:
			error="未进行升级";
			break;
		case (byte) 0x81:
			error="设备离线";
			break;
		case (byte) 0x82:
			error="固件类型错误";
			break;
		case (byte) 0x83:
			error="软件版本错误";
			break;
		
		default:
			error="未知错误";
			break;
		}
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(3);
			OTATask.get(gprsid).statewrite("蓝牙主机升级失败："+error);
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("失败升级", e);
			// TODO: handle exception
		}
		
	}

	/***
	 * 升级蓝牙主机成功
	 * @param msg
	 */
	private static void UpdateBluetoothSuccess(byte[] msg) {
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(2);
			OTATask.get(gprsid).statewrite("蓝牙主机升级成功");
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("成功升级", e);
			// TODO: handle exception
		}
		
	}

	/***
	 * 判断stm32升级结果
	 * @param msg
	 * @throws InterruptedException 
	 */
	private static void updateStm32Result(byte[] msg) throws InterruptedException {
		switch (msg[11]) {
		case (byte)0x80:
			UpdateSteam32Success(msg);
			break;
		default:
		   UpdateSteam32Fail(msg);
			break;
		}
		
	}

	/***
	 * Stem32升级失败
	 * 0x00:未进行升级
       0x01:升级失败
       0x80:升级成功
       0x81:设备离线（针对蓝牙）
       0x82:固件类型错误（针对蓝牙）
       0x83:软件版本错误（针对蓝牙）
	 * @param msg
	 */
	private static void UpdateSteam32Fail(byte[] msg) {
		String error;
		switch (msg[11]) {
		case (byte) 0x01:
			error="升级失败";
			break;
		case (byte) 0x00:
			error="未进行升级";
			break;
		default:
			error="未知错误";
			break;
		}
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(3);
			OTATask.get(gprsid).statewrite(error);
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("失败升级", e);
			// TODO: handle exception
		}
		
	}

	/***
	 * 升级成功结果
	 * @throws InterruptedException 
	 */
	private static void UpdateSteam32Success(byte[] msg) throws InterruptedException {
		
		try {
			String gprsid=Protocol.getGprsId(msg);
			OTAMap.get(gprsid).setState(2);
			OTATask.get(gprsid).statewrite("stm32主机升级成功");
			OTAMap.remove(gprsid);
			OTATask.remove(gprsid);
			OTATimeOut.remove(gprsid);
		} catch (Exception e) {
			ErrorLog.errorWrite("成功升级", e);
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
		switch (msg[13]) {
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
