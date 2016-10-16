package com.myretail.exception;

public class MyRetailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int errorCode;

	public MyRetailException(Exception e) {
		super();
	}

	public MyRetailException(Exception e, int errCode) {
		super(e);
		this.errorCode = errCode;

	}

}
