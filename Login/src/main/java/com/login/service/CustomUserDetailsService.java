package com.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.dao.UserRepository;
import com.login.entities.UserModel;
import com.login.entities.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		log.info(
				"an) Inside Service: CustomUserDetailsService and method : loadUserByUsername paramter recieved username : "
						+ username);
		UserModel user = userRepository.findByUserEmail(username);
		if (user == null) {
			log.error("ar) Unable to retrieve User for the username given.");
			log.error("aq) service -> CustomUserDetailsService UsernameNotFoundException");
			throw new UsernameNotFoundException(username);
		} else {
			log.info(
					"ao) service -> CustomUserDetailsService after getting user using username passed as parameter to this function usermodel is : "
							+ user);
			UserPrincipal userPrincipal = new UserPrincipal(user);

			log.info(
					"ap) service -> CustomUserDetailsService after getting userPrincipal using user passsed UerPrincipal is : "
							+ userPrincipal);
			return userPrincipal;
		}
	}
}