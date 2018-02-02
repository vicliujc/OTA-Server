package com.vic.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vic.gprs.OnOffMsg;

@Repository 
public interface OTAMethod {
	
	public void initializeStatus();
	
	public void initializeOTAStatus();
	
	public List<OTAMsg> otaSelect(int number);
	
	public void transferStatus(SqlMsg sqlMsg);
	
	public void onlineUpdate(OnOffMsg onOffMsg);
}
