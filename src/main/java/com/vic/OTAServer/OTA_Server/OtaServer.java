package com.vic.OTAServer.OTA_Server;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.context.ApplicationContext;

import com.vic.gprs.Gprs;
import com.vic.gprs.OTAMap;
import com.vic.gprs.OTATask;
import com.vic.gprs.OTATimeOut;
import com.vic.main.App1;
import com.vic.mybatis.OTADao;
import com.vic.mybatis.OTAMsg;
import com.vic.mybatis.SqlExecute;
import com.vic.mybatis.SqlMsg;
import com.vic.util.MyUtil;

import ErrorLogger.ErrorLog;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class OtaServer implements Runnable{
	ApplicationContext ac=App1.ac;
	
	public static int UNPACK_FRAME_LENGTH=510;//含bbc crc校验 表头长度
	public int  packNum;
	public static Logger logger=Logger.getLogger(OtaServer.class);
	// 读取文件后的全部byte[] 
	private byte[] doc;
	private OTAMsg otaMsg;
	
	private String path;
	private int now_pack_num;
	private int failTime=0;
	//建立存储doc map
	public static final HashMap<String, byte[]> DOCMAP= new HashMap<String, byte[]>();
	
	
	/***\
	 * 获得现在的数
	 * @return
	 */
	public int getNow_pack_num() {
		return now_pack_num;
	}

	
	/***
	 * 构造器 传入otamsg对象
	 * 
	 * @param otaMsg
	 */
	public OtaServer(OTAMsg otaMsg) {
		this.otaMsg=otaMsg;
		this.path=otaMsg.getBin_file_path();
		try {
			getDocNow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("OtaServer",e);
			e.printStackTrace();
		}
	}
	
	
	//外部获取文档byte[]
	public byte[] getDoc() {
		return doc;
	}

	//读取文档
	private void getDocNow() throws Exception {
		DocRead docRead=new DocRead(path);
		if (!DOCMAP.containsKey(path)) {
			DOCMAP.put(path, docRead.getDocBin());
		}
		doc=DOCMAP.get(path);
		packNum=(int) Math.ceil( (double)doc.length/UNPACK_FRAME_LENGTH);
	}
	
	//读取包数量
	public int getPackNum() {
		double x=(double)(doc.length)/UNPACK_FRAME_LENGTH;
		packNum=(int) Math.ceil(x);
		return packNum;
	}
	
	/***
	 * 输入需要的包数 传出第num包
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public byte[] tanceforByteNow(int num) throws Exception {
		if(num>packNum) throw new Exception("不存在传输的包数");
		if (doc.length==0) throw new Exception("未传入文件");
		byte[] now_pack;
		if(num==packNum) {
			now_pack=new byte[doc.length-UNPACK_FRAME_LENGTH* (num-1)+2];
			System.arraycopy(doc, UNPACK_FRAME_LENGTH* (num-1) , now_pack, 2, doc.length-UNPACK_FRAME_LENGTH* (num-1));
		}
		else {
			now_pack=new byte[UNPACK_FRAME_LENGTH+2];
			System.arraycopy(doc, UNPACK_FRAME_LENGTH*(num-1), now_pack, 2, UNPACK_FRAME_LENGTH);
		}
	  
	    byte[] pack_num_send=MyUtil.intToUInt16Bytes(now_pack_num);
	    System.arraycopy(pack_num_send,0,now_pack,0,2);
	    return Protocol.subcontractMsg(now_pack);
	} 
	
	/***
	 * 发送分包请求 插入sql发送队列
	 */
    public void run() {
    	ApplicationContext ac=App1.ac;
    	//先发分包请求
    	try {
    		if (doc==null) {
    	    	SqlMsg sqlMsg=(SqlMsg) ac.getBean("sqlMsg");
    	    	sqlMsg.setId(otaMsg.getId());
    	    	sqlMsg.setResult_info("未找到文件");
    	    	sqlMsg.setState(3);
    	    	SqlExecute.put(sqlMsg);
    	    	OTATask.remove(otaMsg.getGprs_id());
    	    	return;
    		}
    	byte[] subcontact=Protocol.subcontractRequest(doc.length, packNum,(byte)otaMsg.getTarget(), (byte) otaMsg.getFirmware(), (byte) otaMsg.getVersion(), otaMsg.getSub());
    	ChannelHandlerContext ctx=Gprs.getCTX(otaMsg.getGprs_id());
    	ctx.write(Unpooled.copiedBuffer(subcontact));
    	ctx.flush();
    	OTADao otaDao=(OTADao) ac.getBean("otaDao");
    	SqlMsg sqlMsg=(SqlMsg) ac.getBean("sqlMsg");
    	sqlMsg.setId(otaMsg.getId());
    	sqlMsg.setResult_info("已发送分包请求");
    	sqlMsg.setState(1);
    	SqlExecute.put(sqlMsg);
    	OTAMap.put(otaMsg.getGprs_id(), sqlMsg);
    	OTATimeOut.put(otaMsg.getGprs_id(), new Date());
    	now_pack_num=0;
    	}
    	catch (Exception e) {
			logger.error("OtaServer run", e);// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    /***
     * 输入上一次的传输情况 成功now_pack_num+1 失败不变 传输第now_pack_num包
     * @param ans
     */
    public void send(boolean ans) {
    	try {
    		if (failTime>5) {
    			SqlMsg sqlMsg=OTAMap.get(otaMsg.getGprs_id());
    	    	sqlMsg.setState(3);
    			statewrite("第"+now_pack_num+"包传输失败超过5次");
    			String gprsid=otaMsg.getGprs_id();
    			OTAMap.remove(gprsid);
    			OTATask.remove(gprsid);
    			OTATimeOut.remove(gprsid);
				return ;
			}
    		if (ans) {
    			now_pack_num += 1;
    			failTime=0;
			}
    		else {
				failTime ++;
				SqlMsg sqlMsg=OTAMap.get(otaMsg.getGprs_id());
				SqlExecute.put(sqlMsg);
			}
    		if (now_pack_num>packNum) {
    			statewrite("所有分包已经发送完毕");
    			OTATimeOut.remove(otaMsg.getGprs_id());
        		return;
        	}
    		byte[] transforPack=tanceforByteNow(now_pack_num);
    		Gprs.getCTX(otaMsg.getGprs_id()).writeAndFlush(Unpooled.copiedBuffer(transforPack));
    		statewrite("已发送："+now_pack_num+"/"+packNum);
    		
		} catch (Exception e) {
			// TODO: handle exception
			ErrorLog.errorWrite("发包", e);
		}
    }
    
    /***
     * 插入数据库result_info
     * @throws Exception 
     */
    public void statewrite(String msg) throws InterruptedException {
    	OTADao otaDao=(OTADao) App1.ac.getBean("otaDao");
    	SqlMsg sqlMsg=OTAMap.get(otaMsg.getGprs_id());
    	sqlMsg.setResult_info(msg);
    	SqlExecute.put(sqlMsg);
    }
    
    


}
