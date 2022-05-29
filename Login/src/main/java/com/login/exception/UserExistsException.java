package com.login.exception;

public class UserExistsException extends Exception {

	public UserExistsException(String errorMsg) {
		super(errorMsg);
	}
}
