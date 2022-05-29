package com.login.entities;

import lombok.Data;

@Data
public class LoginRequestModel {

	private String email;
	private String password;
	private String otp;
}
