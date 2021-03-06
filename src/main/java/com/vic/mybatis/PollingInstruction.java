package com.vic.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.support.DaoSupport;

import com.vic.gprs.AddressMsg;
import com.vic.gprs.OTATask;

public class PollingInstruction {
	ApplicationContext ac=com.vic.main.App1.ac;
	
	/***
	 * 判断升级个数 取出升级设备
	 * @return
	 */
	public List<OTAMsg> findOTA() {
		OTADao dao=(OTADao) ac.getBean("otaDao");
		
		if (OTATask.size() >= 30) {
			List<OTAMsg> zero=new ArrayList<OTAMsg>();
			return zero;
		}
		List<OTAMsg> msgs=dao.otaSelect( 30-OTATask.size() );
		
		return msgs;
	}
	
	/***
	 * 获取地址改变数据
	 */
	public  List<AddressMsg> findAddress(){
		OTADao dao=(OTADao) ac.getBean("otaDao");
		 List<AddressMsg> msgs= dao.addressMsgGet();
		 return msgs;
	}

}
