package com.firecode.morganna.common;

public class AlipayApiExecuteException extends RuntimeException {

	private static final long serialVersionUID = -4372108926312560015L;

	private String errCode;
	private String errMsg;

	public AlipayApiExecuteException(String errCode, String errMsg) {
		super(errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public AlipayApiExecuteException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlipayApiExecuteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AlipayApiExecuteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AlipayApiExecuteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AlipayApiExecuteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
