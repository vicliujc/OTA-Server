package com.vic.gprs;

import java.sql.Timestamp;

public class OnOffMsg {
	private String gprs_id;
	private int link_status;
	private Timestamp update_time;
	private int port;
	public String getGprs_id() {
		return gprs_id;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setGprs_id(String gprs_id) {
		this.gprs_id = gprs_id;
	}
	public int getLink_status() {
		return link_status;
	}
	public void setLink_status(int link_status) {
		this.link_status = link_status;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	
	

}
