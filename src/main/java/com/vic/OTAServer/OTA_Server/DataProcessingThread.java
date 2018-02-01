package com.vic.OTAServer.OTA_Server;

public class DataProcessingThread implements Runnable {
	private byte[] msg;

	public DataProcessingThread(byte[] msg) {
		super();
		this.msg = msg;
	}

	public void run() {
		// TODO Auto-generated method stub
		ProtocolOperate.operate(msg);
	}

}
