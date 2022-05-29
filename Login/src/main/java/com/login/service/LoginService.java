package com.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.entities.LoginRequestModel;
import com.login.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	public String generateToken(LoginRequestModel loginRequestModel) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(), loginRequestModel.getPassword()));
		log.info("g) service: loginService -> after authenticating user and passing user email to get userDetails ");
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestModel.getEmail());
		log.info("h) service: loginService -> after getting values in userDetails values are :" + userDetails);
		String generatedToken = jwtUtil.generateToken(userDetails);
		log.info("i) service: loginService -> after generating token token value: " + generatedToken);
		return generatedToken;
	}
}
