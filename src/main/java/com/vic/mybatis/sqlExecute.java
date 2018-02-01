package com.vic.mybatis;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;

import com.vic.main.App1;

import ErrorLogger.ErrorLog;

public class SqlExecute implements Runnable {
	private static final LinkedBlockingQueue<SqlMsg> changeStatusQueue=new LinkedBlockingQueue<SqlMsg>();
	private static final int MAX_SQL_NUM=10;
	
	ApplicationContext ac=App1.ac;
	public static void put(SqlMsg sqlMsg) throws InterruptedException {
		changeStatusQueue.put(sqlMsg);
	}

	public void run() {
		OTADao otaDao;
		try {
		ExecutorService pool=Executors.newFixedThreadPool(MAX_SQL_NUM);	
		// TODO Auto-generated method stub
		while(true) {
			try {
				SqlMsg sqlMsg=changeStatusQueue.take();
				TransferStatusThread transferStatusThread=new TransferStatusThread(sqlMsg);
				pool.submit(transferStatusThread);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				ErrorLog.errorWrite("插入队列", e);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			ErrorLog.errorWrite("创建OTADAO", e);
		}

	}

}
