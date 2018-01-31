package ErrorLogger;

import org.apache.log4j.Logger;

public class ErrorLog {
	private static Logger logger=Logger.getLogger("错误记录");
	
	public static void errorWrite(String msg,Exception e) {
		logger.error(msg, e);
		e.printStackTrace();
	}

}
