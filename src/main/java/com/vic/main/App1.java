package com.vic.main;


import java.util.Dictionary;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vic.mybatis.OTADao;
import com.vic.mybatis.OnlineOfflineThread;
import com.vic.mybatis.SqlStart;

public class App1 {
	private static Logger logger = Logger.getLogger(App1.class);
	public static ApplicationContext ac=new ClassPathXmlApplicationContext("beans.xml");
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//ApplicationContext ac=new ClassPathXmlApplicationContext("com/vic/main/beans.xml");
		    OTADao otaDao =(OTADao) ac.getBean("otaDao");
		    otaDao.initializeStatus();
		    otaDao.initializeOTAStatus();
		    new Thread(new OnlineOfflineThread()).start();
			NettyStart nettyStart=(NettyStart) ac.getBean("nettyStart");
			new Thread(nettyStart).start();
			SqlStart sqlStart=new SqlStart();
			new Thread(sqlStart).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Exception", e);
		}
		

	}

}
