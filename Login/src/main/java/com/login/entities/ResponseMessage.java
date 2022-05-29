package com.login.entities;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

	private String message;
	private int errorCode;
	private HttpStatus erroHttpStatus;
}
