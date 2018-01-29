package com.vic.mybatis;


import java.util.Arrays;

import org.apache.ibatis.reflection.ArrayUtil;

import com.vic.util.MyUtil;

public class OTAMsg {
	private int id ;
	private String gprs_id;
	private int target;
	private String bin_file_path;
	private int firmware;
	private int version;
	private String slave_indexes;
	
	//构造器 setget toString
	public OTAMsg() {
		
	}
	public OTAMsg(int id, String gprs_id, int target, String bin_file_path, int firmware, int version,
			String slave_indexes) {
		super();
		this.id = id;
		this.gprs_id = gprs_id;
		this.target = target;
		this.bin_file_path = bin_file_path;
		this.firmware = firmware;
		this.version = version;
		this.slave_indexes = slave_indexes;
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
	public int gettarget() {
		return target;
	}
	public void settarget(int target) {
		this.target = target;
	}
	public String getBin_file_path() {
		return bin_file_path;
	}
	public void setBin_file_path(String bin_file_path) {
		this.bin_file_path = bin_file_path;
	}
	public int getFirmware() {
		return firmware;
	}
	public void setFirmware(int firmware) {
		this.firmware = firmware;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getSlave_indexes() {
		return slave_indexes;
	}
	public void setSlave_indexes(String slave_indexes) {
		this.slave_indexes = slave_indexes;
	}
	public byte[] getSub() {
		int ansInt=0;
		byte[] ans=new byte[3];
		String[] sub=slave_indexes.split("-");
		for (String subnum : sub) {
			ansInt += Math.pow(2, Integer.parseInt(subnum)-1);
		}
		byte[] x=MyUtil.reverse( MyUtil.intTobyteArray(ansInt));
		System.arraycopy(x, 0, ans, 0, 3);
		return ans;
	}
	@Override
	public String toString() {
		return "OTAMsg [id=" + id + ", gprs_id=" + gprs_id + ", target=" + target + ", bin_file_path=" + bin_file_path
				+ ", firmware=" + firmware + ", version=" + version + ", slave_indexes=" + slave_indexes + "]";
	}

}
