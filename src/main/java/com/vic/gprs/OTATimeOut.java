package com.vic.gprs;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.vic.OTAServer.OTA_Server.OtaServer;
import com.vic.main.App1;
import com.vic.mybatis.OTADao;
import com.vic.mybatis.SqlExecute;
import com.vic.mybatis.SqlMsg;

import ErrorLogger.ErrorLog;

public class OTATimeOut implements Runnable {
	private static final  ConcurrentHashMap<String , Date> OTATime=new ConcurrentHashMap<String, Date>();
	
	public static void put(String gprsid, Date otaServer) {
		OTATime.put(gprsid, otaServer);
	}
	public static void remove(String gprsid) {
		OTATime.remove(gprsid);
	}
	public static Date get(String gprsid) {
	    return	OTATime.get(gprsid);
	}
	
	/***
	 * 判断是否超时 600 000=1小时
	 * @param date
	 * @return
	 */
	private static boolean compareTime(Date date) throws Exception{
		long time = new Date().getTime();
		return (time> (date.getTime()+600000) );
	}

	/***
	 * 比较两个时间 每五分钟对比map里元素一次
	 */
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (!OTATime.isEmpty()) {
				try {
					Iterator<Entry<String, Date>> it=OTATime.entrySet().iterator();
					while (it.hasNext()) {
						try {
							Entry<String, Date> entry=it.next();
							if (compareTime(entry.getValue())) {
								OTADao otaDao=(OTADao) App1.ac.getBean("otaDao");
						    	SqlMsg sqlMsg=OTAMap.get(entry.getKey());
						    	sqlMsg.setId(6);
						    	SqlExecute.put(sqlMsg);
						    	OTAMap.remove(entry.getKey());
						    	OTATask.remove(entry.getKey());
							}
						} catch (Exception e) {
							ErrorLog.errorWrite("超时监测比较", e);
						}
					}
				} catch (Exception e) {
					ErrorLog.errorWrite("超时监测", e);
				}
				
			}
		    try {
				Thread.sleep(300000);
		    } catch (InterruptedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}


