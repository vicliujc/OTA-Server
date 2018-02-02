package com.vic.mybatis;

import java.util.concurrent.LinkedBlockingQueue;

import com.vic.gprs.OnOffMsg;
import com.vic.main.App1;

import ErrorLogger.ErrorLog;

public class OnlineOfflineThread implements Runnable{
	private static final LinkedBlockingQueue<OnOffMsg> OnOffQueue=new LinkedBlockingQueue<OnOffMsg>();
	
	public static void put(OnOffMsg onOffMsg) throws InterruptedException {
		OnOffQueue.put(onOffMsg);
	}

	public void run() {
		while(true) {
			try {
				OnOffMsg onoff=OnOffQueue.take();
				OTADao otaDao=(OTADao) App1.ac.getBean("otaDao");
				otaDao.onlineUpdate(onoff);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorLog.errorWrite("上下线数据库插入", e);
				
			}
		}
	}
}
