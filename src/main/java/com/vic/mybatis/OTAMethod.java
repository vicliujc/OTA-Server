package com.vic.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository 
public interface OTAMethod {
	
	public void initializeStatus();
	
	public List<OTAMsg> otaSelect();
	
	public void transferStatus(SqlMsg sqlMsg);
}
