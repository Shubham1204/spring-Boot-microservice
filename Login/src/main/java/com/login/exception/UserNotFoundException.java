package com.login.exception;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String errorMsg) {
		super(errorMsg);
	}
}
