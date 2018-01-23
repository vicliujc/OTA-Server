package com.vic.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository 
public interface OTAMethod {
	public List<OTAMsg> otaSelect();

}
