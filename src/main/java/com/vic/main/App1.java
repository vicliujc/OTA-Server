package com.vic.main;


import java.util.Dictionary;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App1 {
	private static Logger logger = Logger.getLogger(App1.class);
	public static ApplicationContext ac=new ClassPathXmlApplicationContext("beans.xml");
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//ApplicationContext ac=new ClassPathXmlApplicationContext("com/vic/main/beans.xml");
			NettyStart nettyStart=(NettyStart) ac.getBean("nettyStart");
			nettyStart.start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Exception", e);
		}
		

	}

}
