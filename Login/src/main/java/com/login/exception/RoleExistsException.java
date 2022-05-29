package com.login.exception;

public class RoleExistsException extends Exception {

	public RoleExistsException(String errorMsg) {
		super(errorMsg);
	}
}
