package com.vic.OTAServer.OTA_Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DocRead {
	
	private String path;
	private byte[] doc;
	
	public DocRead(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public byte[] getDocBin() throws Exception {
		File file =new File(path);
		if (!file.exists()) throw new Exception("没有找到文件");
		InputStream in=new FileInputStream(file);
		
		
		
		return doc;
	}

}
