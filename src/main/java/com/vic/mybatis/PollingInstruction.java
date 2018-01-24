package com.vic.mybatis;

import java.util.List;

import org.springframework.context.ApplicationContext;

public class PollingInstruction {
	ApplicationContext ac=com.vic.main.App1.ac;
	
	public List<OTAMsg> findOTA() {
		OTADao dao=(OTADao) ac.getBean("otaDao");
		List<OTAMsg> msgs=dao.otaSelect();
		return msgs;
	}

}
