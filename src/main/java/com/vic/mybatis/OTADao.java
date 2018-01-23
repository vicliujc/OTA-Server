package com.vic.mybatis;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;

public class OTADao implements OTAMethod{
	private SqlSessionTemplate sessionTemplate;
	
	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	public List<OTAMsg> otaSelect() {
		// TODO Auto-generated method stub
		String statement="com.vic.mybatis.OTAMethod.otaSelect";
		return sessionTemplate.selectList(statement);
	}

}
