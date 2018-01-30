package com.vic.OTAServer.OTA_Server;

public class ProtocolOperate {
	static final int MAX_SUBRQSANS=10;
	
	/***
	 * 区分传入数据类型 
	 * @param Msg
	 */
	public static void operate(byte[] msg) {
		if (msg[2] == (byte)0xF1) {
		     switch (msg[10]) {
		        case (byte) 0xF2:
			       subcontactRequestAns(msg);
			       break;
                case (byte) 0xF3:
			
		    	   break;
		        default:
			       break;
		     }
		}
	}
	
	/***
	 * 处理分包请求的回复数据
	 * @param msg
	 */
	public static void  subcontactRequestAns(byte[] msg) {
		switch (msg[11]) {
		case (byte)0x80:
			subcontactRequestAnsSuccess(msg);
			break;
		default:
			subcontactRequestAnsFail(msg);
			return;
		}
	}
	
	public static void subcontactRequestAnsSuccess(byte[] msg) {
		
	}
	
	/***
	 * 01H：数据过长；
    02H：帧类型错误；
    03H：数据长度与数据包数目不匹配；
    04H：接收端忙；
    81H：预留
    82H：固件类型错误
    83H：版本错误
	 * @param msg
	 */
	public static void subcontactRequestAnsFail(byte[] msg) {
		String errorMsg;
		switch (msg[11]) {
           case (byte)0x01:
			 errorMsg="数据过长";
			 break;
           case (byte)0x02:
        	   errorMsg="帧类型错误";
	         break;
           case (byte)0x03:
        	   errorMsg="数据长度与数据包数目不匹配";
		    break;
	       case (byte)0x81:
	    	   errorMsg="预留";
		     break;
	       case (byte)0x82:
	    	   errorMsg="固件类型错误";
		     break;
	       case (byte)0x83:
	    	   errorMsg="版本错误";
		     break;
		   default:
			   errorMsg="未知错误："+msg[11];
			break;
		}
		
		
	}

}
