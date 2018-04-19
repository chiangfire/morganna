package com.firecode.morganna.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * @author JIANG
 */
public class ErrorUtils {
	
	/**
	 * 获取错误信息
	 * @param error
	 * @return
	 */
	public static final String getPrintStackTrace(Throwable error){
		StringWriter stackTrace = new StringWriter();
		error.printStackTrace(new PrintWriter(stackTrace));
		stackTrace.flush();
		return stackTrace.toString();
	}

}
