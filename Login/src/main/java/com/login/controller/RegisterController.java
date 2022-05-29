package com.login.controller;

import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.config.ErrorMap;
import com.login.config.ErrorMap.ErrorMsg;
import com.login.entities.LoginRequestModel;
import com.login.entities.ResponseMessage;
import com.login.entities.UserModel;
import com.login.exception.InvalidOtpException;
import com.login.exception.InvalidParametersException;
import com.login.exception.UnableToAddException;
import com.login.exception.UnableToSendOtpException;
import com.login.exception.UnableToUpdateException;
import com.login.exception.UserExistsException;
import com.login.exception.UserNotFoundException;
import com.login.exception.UserRoleNotMappedException;
import com.login.service.EmailService;
import com.login.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/public")
@Slf4j
public class RegisterController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/register")
	@Transactional
	public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
		log.info("l) Inside controller: register with mapping : public/register and UserModel values : " + user);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			if(user.getUserEmail()==null || user.getUserPassword()==null) {
				throw new InvalidParametersException("either userEmail or password missing");
			}else {
				if(user.getRole().getRoleName()==null || user.getRole().getRoleName()==" " || user.getRole().getRoleName()=="") {
					user.getRole().setRoleName("user");
				}
				if(user.getUserName()==null || user.getUserName()=="" || user.getUserName()==" ") {
					user.setUserName(user.getUserEmail());
				}
			boolean addUser = userService.addUser(user);
			log.info("m) controller: register -> after adding user : " + addUser);
			if (!addUser) {
				log.error("n) controller: register -> user not registered/added");
				throw new UnableToAddException("unable to register user to db");
			} else {
				log.info("o) controller: register -> sending otp since user is added to db ");
				boolean sendEmailForOtp = emailService.sendEmailForOtp(user.getUserEmail());
				log.info("p) controller: register -> otp sent :" + sendEmailForOtp);
				if (!sendEmailForOtp) {
					log.info("q) controller: register -> otp not sent ");
					throw new UnableToSendOtpException("Unable to send email for otp");
				} else {
					log.info("r) controller: register -> user registration successful");
					responseMessage.setMessage("user registered");
					
					return ResponseEntity.ok(responseMessage);
				}
			}
			}
		} catch (UserExistsException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.user_already_exists);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"s) controller -> UserController and method getUsers and getting UserExistsException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"t) controller -> UserController and method getUsers and getting UserNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToAddException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_registered);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"u) controller -> UserController and method getUsers and getting UnableToAddException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserRoleNotMappedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_role_not_mapped);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"v) controller -> UserController and method getUsers and getting UserRoleNotMappedException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToSendOtpException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Otp_not_sent);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"w) controller -> UserController and method getUsers and getting UnableToSendOtpException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (MessagingException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_send_email);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.error("x) controller: register -> user not registered and getting MessagingException : message: "
					+ e.getMessage());
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToUpdateException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_update_details);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"y) controller -> UserController and method getUsers and getting UnableToUpdateException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}catch (InvalidParametersException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Invalid_parameters);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("z) controller -> UserController and method getUsers and getting InvalidParametersException with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("z) controller -> UserController and method getUsers and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}