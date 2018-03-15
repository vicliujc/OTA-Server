package com.vic.mybatis;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;

import com.vic.gprs.AddressMsg;
import com.vic.gprs.OnOffMsg;

public class OTADao implements OTAMethod{
    private SqlSessionTemplate sessionTemplate;
    private int port ;
	
	public void setPort(int port) {
		this.port = port;
	}
	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	/***
	 * 获得需要更新的列表
	 */
	public List<OTAMsg> otaSelect(int number) {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.otaSelect";
		return sessionTemplate.selectList(statement,number);
	}

	/***
	 * 数据传输过程中 状态更新 result_info send_done
	 */
	public void transferStatus(SqlMsg sqlMsg) {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.transferStatus";
		sessionTemplate.update(statement, sqlMsg);
	}
     
	/***
	 * 重启程序初始化上下线状态
	 */
	public void initializeStatus() {
		// TODO Auto-generated method stub          
		String statement="com.vic.mybatis.OTAMethod.initializeStatus";
		sessionTemplate.update(statement, port);
	}
	
	/***
	 * 更新上下线状态
	 */
	public void onlineUpdate(OnOffMsg onOffMsg) {
		String statement="com.vic.mybatis.OTAMethod.onlineUpdate";
		sessionTemplate.update(statement, onOffMsg);
	}
	
	/***
	 * 重启程序初始化OTA传输状态
	 */
	public void initializeOTAStatus() {
		String statement="com.vic.mybatis.OTAMethod.initializeOTAStatus";
		sessionTemplate.update(statement, port);
	}
	
	//获取更改地址数据
	public List<AddressMsg> addressMsgGet() {
		String statement="com.vic.mybatis.OTAMethod.addressMsgGet";
		return sessionTemplate.selectList(statement);
		
		
	}
	
	//更改地址状态更新
	public void addressChangeAns(AddressMsg addressMsg) {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.addressChangeAns";
		sessionTemplate.update(statement,addressMsg);
	}
	
	
	
}
	


