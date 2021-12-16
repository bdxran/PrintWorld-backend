package com.rbl.printworld.exceptions;

public class ApplicationException extends RuntimeException {

	private String errorCode;

	public ApplicationException(Throwable cause, String errorCode, String message) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ApplicationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
