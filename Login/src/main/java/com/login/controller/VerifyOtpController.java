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
import com.login.exception.UnableToSendOtpException;
import com.login.exception.UnableToUpdateException;
import com.login.exception.UserNotFoundException;
import com.login.exception.UserNotVerifiedException;
import com.login.service.EmailService;
import com.login.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/public")
@Slf4j
public class VerifyOtpController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/verify")
	@Transactional
	public ResponseEntity<?> login(@RequestBody LoginRequestModel loginRequestModel) {
		log.info("u) Inside controller: verifyOtp with mapping : public/veify and loginRequestModel values : "
				+ loginRequestModel);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			if (loginRequestModel.getEmail() == null || loginRequestModel.getEmail() == "") {
				log.error("v) controller: verifyOtp -> Either email or otp is missing and returning : "
						+ responseMessage);
				throw new InvalidParametersException("Either_Email_Otp_missing");
			}
			UserModel user = userService.getUserByUserEmail(loginRequestModel.getEmail());
			if (user != null) {
				if (user.getUserStatus() == 'P') {
					long now = System.currentTimeMillis();
					long prev = Long.valueOf(user.getUserOtpTime()) + TimeUnit.MINUTES.toMillis(5);
					if (prev < now) {
						emailService.sendEmailForOtp(user.getUserEmail());
					} else {
						if (loginRequestModel.getOtp().equals(user.getUserOtp())) {
							if (userService.updateUserStatus(user.getUserEmail())) {
								responseMessage.setMessage("User Verified : "+loginRequestModel.getEmail());
								return ResponseEntity.ok(responseMessage);
							}
						} else {
							throw new InvalidOtpException("Pls enter correct otp");
						}
					}
					throw new UserNotVerifiedException("Please verify email first");
				}
				responseMessage.setMessage("User already Verified : "+loginRequestModel.getEmail());
				return ResponseEntity.ok(responseMessage);
			} else {
				throw new UserNotFoundException("user is not authorized/user not registered");
			}
		} catch (UnableToSendOtpException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Otp_not_sent);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToUpdateException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_update_details);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserNotVerifiedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_verified_check_email_for_otp);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (InvalidParametersException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Either_Email_Otp_missing);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (InvalidOtpException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Invalid_Otp);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (MessagingException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_send_email);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}