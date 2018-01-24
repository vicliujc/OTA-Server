package com.vic.mybatis;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.DocRead;

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
	
	public void run(){
		// TODO Auto-generated method stub
			try {
				while(true) {
					Thread.sleep(10000);
					PollingInstruction pollingInstruction=new PollingInstruction();
					List<OTAMsg> OTAObjects=pollingInstruction.findOTA();
				DocRead
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	
	
		
}
