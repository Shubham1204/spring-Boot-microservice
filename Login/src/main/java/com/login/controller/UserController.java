package com.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.config.ErrorMap;
import com.login.config.ErrorMap.ErrorMsg;
import com.login.entities.ResponseMessage;
import com.login.entities.UserModel;
import com.login.exception.UserNotFoundException;
import com.login.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authenticated/users")
@CrossOrigin("*")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getAll")
	public ResponseEntity<?> getUsers() {
		log.info("ep) controller -> RightController and method getUsers");
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			List<UserModel> users = userService.getUsers();
			log.info("eq) controller -> RightController and method getUsers and users retrieved are : "+users);
			return ResponseEntity.ok(users);
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("er) controller -> UserController and method getUsers and getting UserNotFoundException with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("es) controller -> UserController and method getUsers and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@GetMapping("/get/{uEmail}")
	public ResponseEntity<?> getUserByEmail(@PathVariable("uEmail") String userEmail) {
		log.info("et) controller -> RightController and method getUserByEmail");
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			UserModel user = userService.getUserByUserEmail(userEmail);
			log.info("eu) controller -> RightController and method getUsers and user retrieved is : "+user);
			return ResponseEntity.ok(user);
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("ev) controller -> UserController and method getUserByEmail and getting User_does_not_exist with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("ew) controller -> UserController and method getUserByEmail and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}