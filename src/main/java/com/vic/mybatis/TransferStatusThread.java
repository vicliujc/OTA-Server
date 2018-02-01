package com.vic.mybatis;

import com.vic.main.App1;

import ErrorLogger.ErrorLog;

public class TransferStatusThread implements Runnable {
	private SqlMsg sqlMsg;
	

	public TransferStatusThread(SqlMsg sqlMsg) {
		super();
		this.sqlMsg = sqlMsg;
	}


	public void run() {
		try {
			OTADao otaDao=(OTADao) App1.ac.getBean("otaDao");
			otaDao.transferStatus(sqlMsg);
		} catch (Exception e) {
			// TODO: handle exception
			ErrorLog.errorWrite("sql线程", e);
			
		}
		
	}

}
