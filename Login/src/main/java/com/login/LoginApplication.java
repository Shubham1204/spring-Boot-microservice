package com.login;

import javax.mail.MessagingException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication /* (scanBasePackages = {"com.login.dao.*"}) */
@EnableSwagger2
@EnableCaching
/* @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) */
public class LoginApplication {

	public static void main(String[] args) throws MessagingException {
		SpringApplication.run(LoginApplication.class, args);
	}

}
