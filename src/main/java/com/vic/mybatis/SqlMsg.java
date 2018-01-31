package com.vic.mybatis;

public class SqlMsg {
	private int id;
	private String result_info;
	private int state;

	public String getResult_info() {
		return result_info;
	}

	public void setResult_info(String result_info) {
		this.result_info = result_info;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
