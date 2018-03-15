package com.vic.mybatis;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;

import com.vic.gprs.AddressMsg;
import com.vic.main.App1;

import ErrorLogger.ErrorLog;

public class SqlExecute implements Runnable {
	private static final LinkedBlockingQueue<Object> changeStatusQueue=new LinkedBlockingQueue<Object>();
	
	
	ApplicationContext ac=App1.ac;
	public static void put(Object sqlMsg) throws InterruptedException {
		changeStatusQueue.put(sqlMsg);
	}

	public void run() {
		OTADao otaDao;
		try {
		while(true) {
			try {
				Object sql=changeStatusQueue.take();
				if (sql.getClass()==SqlMsg.class) {
					SqlMsg sqlMsg=(SqlMsg) sql;
					otaDao=(OTADao) App1.ac.getBean("otaDao");
					otaDao.transferStatus(sqlMsg);
				}
				if (sql.getClass()==AddressMsg.class) {
					AddressMsg sqlMsg=(AddressMsg) sql;
					otaDao=(OTADao) App1.ac.getBean("otaDao");
					otaDao.addressChangeAns(sqlMsg);
				}
			} catch (InterruptedException e) {
				ErrorLog.errorWrite("插入队列", e);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			ErrorLog.errorWrite("创建OTADAO", e);
		}

	}

}
