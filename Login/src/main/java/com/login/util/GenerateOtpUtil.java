package com.login.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateOtpUtil {

	public String generateOtp() {
		log.info("aa) Inside util: GenerateOtpUtil and method : generateOtp");
		int min = 100000;
		int max = 999999;
		int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
		log.info("ab) util -> GenerateOtpUtil and otp generated is : "+randomNumber);
		return Integer.toString(randomNumber);
	}

}
