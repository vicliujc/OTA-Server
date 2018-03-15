package com.vic.mybatis;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;

import com.vic.OTAServer.OTA_Server.DocRead;
import com.vic.OTAServer.OTA_Server.OtaServer;
import com.vic.OTAServer.OTA_Server.Protocol;
import com.vic.OTAServer.OTA_Server.ProtocolOperate;
import com.vic.gprs.AddressMsg;
import com.vic.gprs.Gprs;
import com.vic.gprs.OTATask;
import com.vic.gprs.OTATimeOut;
import com.vic.main.App1;

import ErrorLogger.ErrorLog;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import org.apache.log4j.Logger;
import  org.junit.Test; 

public class SqlStart implements Runnable{
    private static final int OTA_START_NUM=10;
	ApplicationContext ac=com.vic.main.App1.ac;
	
	
	public void sendAddressChange(AddressMsg addressMsg) throws InterruptedException {
		try {
			ChannelHandlerContext ctx=Gprs.getCTX(addressMsg.getGprs_id());
			ctx.writeAndFlush(Unpooled.copiedBuffer(Protocol.addressChange(addressMsg)));
			addressMsg.setSend_done(1);
			SqlExecute.put(addressMsg);
		} catch (Exception e) {
			ErrorLog.errorWrite("发送地址 添加数据库", e);
			// TODO: handle exception
		}
		
	}
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
				new Thread(new OTATimeOut()).start();
				PollingInstruction pollingInstruction=new PollingInstruction();
				while(true) {
					
					Thread.sleep(10000);
					
					
					try {
						List<OTAMsg> OTAObjects=pollingInstruction.findOTA();
					    List<AddressMsg> addressObjects = pollingInstruction.findAddress();
						try {
							//开始OTA
					        if(!OTAObjects.isEmpty()) {
					    	  for(OTAMsg otaObject:OTAObjects) {
					    		  if (OTATask.contains(otaObject.getGprs_id())) 
									continue;
					    	  OtaServer otaServer=new OtaServer(otaObject);
					    	  OTATask.put(otaObject.getGprs_id(), otaServer);
					    	  threadPool.submit(otaServer);
					    	  
					    	//  new Thread(otaServer).start();
					    	}
					      }
						} catch (Exception e) {
							ErrorLog.errorWrite("升级启动", e);
						}
				        
					    //更改地址
                        if (!addressObjects.isEmpty()) {
                    	  try {
                    		  for(AddressMsg addressMsg:addressObjects) {
                    			  System.out.println( Gprs.containsKey(addressMsg.getGprs_id()) );
                    			  if (!Gprs.containsKey(addressMsg.getGprs_id()))
                    				  continue;
                        		  sendAddressChange(addressMsg);
                        	  }
						} catch (Exception e) {
							ErrorLog.errorWrite("地址更改", e);
						}
                    	  
						}
					}
					catch(Exception x) 
					{
						x.printStackTrace();
					logger.error("sql start Exception",x);
					}
					
					
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	
	
		
}
