package com.login.exception;

public class UnableToSendOtpException extends Exception {

	public UnableToSendOtpException(String errorMsg) {
		super(errorMsg);
	}
}
