package com.vic.gprs;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class AddressMsg {
	private static final LinkedBlockingQueue<AddressMsg> addressMsg=new LinkedBlockingQueue<AddressMsg>();
	
	private int id;
	private String gprs_id;
	private int type1;
	private String address1;
	private int type2;
	private String address2;
	private int type3;
	private String address3;
	private int type4;
	private String address4;
	private int send_done;
	
	@Override
	public String toString() {
		return "AddressMsg [id=" + id + ", gprs_id=" + gprs_id + ", type1=" + type1 + ", address1=" + address1
				+ ", type2=" + type2 + ", address2=" + address2 + ", type3=" + type3 + ", address3=" + address3
				+ ", type4=" + type4 + ", address4=" + address4 + ", send_done=" + send_done + "]";
	}
	public int getSend_done() {
		return send_done;
	}
	public void setSend_done(int send_done) {
		this.send_done = send_done;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGprs_id() {
		return gprs_id;
	}
	public void setGprs_id(String gprs_id) {
		this.gprs_id = gprs_id;
	}
	public int getType1() {
		return type1;
	}
	public void setType1(int type1) {
		this.type1 = type1;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public int getType2() {
		return type2;
	}
	public void setType2(int type2) {
		this.type2 = type2;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public int getType3() {
		return type3;
	}
	public void setType3(int type3) {
		this.type3 = type3;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public int getType4() {
		return type4;
	}
	public void setType4(int type4) {
		this.type4 = type4;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	
	

}
