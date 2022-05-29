package com.courses.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

	private int userId;
	private String userName;
	private String userEmail;
	private String userContact;
	private String userPassword;
	private String userOtp;
	private String userOtpTime;
	private char userStatus;
}