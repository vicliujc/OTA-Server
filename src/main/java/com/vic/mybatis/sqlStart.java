package com.vic.mybatis;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.DocRead;
import com.vic.OTAServer.OTA_Server.OtaServer;
import com.vic.main.App1;

import org.apache.log4j.Logger;
import  org.junit.Test; 

public class sqlStart implements Runnable{
    
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
				while(true) {
					Thread.sleep(10000);
					PollingInstruction pollingInstruction=new PollingInstruction();
					List<OTAMsg> OTAObjects;
					try {
					OTAObjects=pollingInstruction.findOTA();
				    if(!OTAObjects.isEmpty()) {
				    	for(OTAMsg otaObject:OTAObjects) {
				    	OtaServer otaServer=new OtaServer(otaObject);
				    	new Thread(otaServer).start();
				    	}
				    }
					
					
					
					
					
					
					}
					catch(Exception x) 
					{
					logger.error("Exception",x);
					}
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	
	
		
}
