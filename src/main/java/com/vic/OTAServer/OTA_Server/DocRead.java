package com.vic.OTAServer.OTA_Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	
	private boolean fileExist(String path) throws Exception{
		File file=new File(path);
		try {
			
			if (file.exists()) {
				return true;
			}
			System.out.println(path + " 此路径文件不存在");
			throw new Exception("未找到文件");
		} finally {
			// TODO: handle finally clause
			
		}
	
	}
	
	public byte[] getDocBin() throws Exception {
		if (!fileExist(path)) return null;
		Path file =Paths.get(path);
		doc=Files.readAllBytes(file);
		return doc;
	}

}
