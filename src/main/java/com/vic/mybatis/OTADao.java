package com.vic.mybatis;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;

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
	public List<OTAMsg> otaSelect() {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.otaSelect";
		return sessionTemplate.selectList(statement);
	}

	/***
	 * 数据传输过程中 状态更新
	 */
	public void transferStatus(OTAMsg otaMsg) {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.transferStatus";
		sessionTemplate.update(statement, otaMsg);
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
	 * 
	 */
    public void changeChansferStatus(OTAMsg otaMsg) {
    	String statement="com.vic.mybatis.OTAMethod.initializeStatus";
		sessionTemplate.update(statement, otaMsg.getState());
    }
	

}
