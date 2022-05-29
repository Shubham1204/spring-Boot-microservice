package com.login.exception;

public class InvalidPasswordException extends Exception {

	public InvalidPasswordException(String errorMsg) {
		super(errorMsg);
	}
}
