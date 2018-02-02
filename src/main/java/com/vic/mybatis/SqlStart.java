package com.vic.mybatis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.DocRead;
import com.vic.OTAServer.OTA_Server.OtaServer;
import com.vic.gprs.OTATask;
import com.vic.main.App1;

import org.apache.log4j.Logger;
import  org.junit.Test; 

public class SqlStart implements Runnable{
    private static final int OTA_START_NUM=10;
	ApplicationContext ac=com.vic.main.App1.ac;
	 
//	@Test
//	public void test() {
//		OTADao dao=(OTADao) ac.getBean("otaDao");
//		List<OTAMsg> msgs=dao.otaSelect();
//		for (OTAMsg otaMsg : msgs) {
//			System.out.println(otaMsg);
//		}
//	}
	private static Logger logger = Logger.getLogger(App1.class);
	public void run(){
		// TODO Auto-generated method stub
			try {
				ExecutorService threadPool=Executors.newFixedThreadPool(OTA_START_NUM);
				SqlExecute sqlExecute=(SqlExecute) ac.getBean("sqlExecute");
				new Thread(sqlExecute).start();
				while(true) {
					
					Thread.sleep(10000);
					PollingInstruction pollingInstruction;
					//开始OTA
					try {
						
						pollingInstruction=new PollingInstruction();
						List<OTAMsg> OTAObjects;
					    OTAObjects=pollingInstruction.findOTA();
				        if(!OTAObjects.isEmpty()) {
				    	  for(OTAMsg otaObject:OTAObjects) {
				    	  OtaServer otaServer=new OtaServer(otaObject);
				    	  threadPool.submit(otaServer);
				    	  OTATask.put(otaObject.getGprs_id(), otaServer);
				    	//  new Thread(otaServer).start();
				    	}
				    }
					}
					catch(Exception x) 
					{
						x.printStackTrace();
					logger.error("Exception",x);
					}
					
					
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	
	
		
}
