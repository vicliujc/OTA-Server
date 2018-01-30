package com.vic.mybatis;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class sqlExecute implements Runnable {
	
    public static final ConcurrentHashMap<OTAMsg,Integer> sqlExecuteQueue =new ConcurrentHashMap<OTAMsg, Integer>();
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			OTAMsg otaMsg;
			int type;
			if (sqlExecuteQueue.isEmpty()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			synchronized(sqlExecuteQueue) {
				
			}
			
			
			
		}

	}

}
