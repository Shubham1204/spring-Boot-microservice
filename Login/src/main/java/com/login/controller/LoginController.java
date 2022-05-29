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
import com.login.entities.LoginResponseModel;
import com.login.entities.ResponseMessage;
import com.login.entities.UserModel;
import com.login.exception.InvalidOtpException;
import com.login.exception.InvalidParametersException;
import com.login.exception.InvalidPasswordException;
import com.login.exception.UnableToSendOtpException;
import com.login.exception.UnableToUpdateException;
import com.login.exception.UserNotFoundException;
import com.login.exception.UserNotVerifiedException;
import com.login.service.EmailService;
import com.login.service.LoginService;
import com.login.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/public")
@Slf4j
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private LoginService loginService;

//	@GetMapping("/")
//    public ResponseEntity<?> main(OAuth2AuthenticationToken token) {
//        System.out.println(token.getPrincipal());
//        return ResponseEntity.ok("user : "+token);
//    }

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestModel loginRequestModel) {
		log.info(
				"a) Inside controller: login with mapping : public/login and loginModel values : " + loginRequestModel);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			if (loginRequestModel.getEmail() == null || loginRequestModel.getPassword() == null
					|| loginRequestModel.getEmail() == "" || loginRequestModel.getPassword() == ""
					|| loginRequestModel.getEmail() == " " || loginRequestModel.getPassword() == " ") {

				log.error("b) controller: login -> Either email or Password is missing and returning : "
						+ responseMessage);
				throw new InvalidParametersException("either userEmail or password missing");
			}
			log.info(
					"c) controller: login -> Username and password is neither null nor empty so passing login details to userService class with method : getUserByUserEmailAndUserPassword and values passed are : "
							+ loginRequestModel);
			UserModel user = userService.getUserByUserEmailAndUserPassword(loginRequestModel);

			log.info("d) controller: login -> after getting data in userModel usermodel value is :" + user);
			if (user != null) {
				if (user.getUserStatus() == 'P') {
					log.info(
							"e) controller: login -> user is not null but userStatus is Pending so user has to verify the email first ask user to check email for otp");

					throw new UserNotVerifiedException("userEmailnot verify.");
				}
				log.info(
						"f)controller: login -> user is not null nor user status is Yes (user is authenticated) so now authenticating user details");
				LoginResponseModel loginResponseModel = new LoginResponseModel(user.getUserName(), user.getUserEmail(),
						user.getRole(), loginService.generateToken(loginRequestModel));
				log.info("j) controller: login -> after passing values to loginResponseModel value is :"
						+ loginResponseModel);
				return ResponseEntity.ok(loginResponseModel);
			} else {
				if (userService.getUserByUserEmail(loginRequestModel.getEmail()) != null) {
					throw new InvalidPasswordException("user password is incorrect");
				}
				log.error(
						"k) controller: login -> user is not authorized/or user not registered. responseMessage values : "
								+ responseMessage);
				throw new UserNotFoundException("user is not authorized/or user not registered");
			}
		} catch (InvalidParametersException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Either_Email_Password_missing);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (InvalidPasswordException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Invalid_Password);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_registered);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UserNotVerifiedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_verified_check_email_for_otp);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@PostMapping("/password/forgot")
	@Transactional
	public ResponseEntity<?> forgotPasswrod(@RequestBody LoginRequestModel loginRequestModel) {
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			if (loginRequestModel.getEmail() == null || loginRequestModel.getEmail() == ""
					|| loginRequestModel.getEmail() == " ") {

				log.error("b) controller: login -> Either email or Password is missing and returning : "
						+ responseMessage);
				throw new InvalidParametersException("either userEmail or password missing");
			}
			UserModel userByUserEmail = userService.getUserByUserEmail(loginRequestModel.getEmail());
			if (userByUserEmail != null) {
				boolean isOtpSent = emailService.sendEmailForOtp(userByUserEmail.getUserEmail());
				if (isOtpSent) {
					responseMessage.setMessage("Otp for password reset sent successfull");
					return ResponseEntity.ok(responseMessage);
				} else {
					throw new UnableToSendOtpException("Uanble to send Otp");
				}
			} else {
				throw new UserNotFoundException("User Not Found in db");
			}
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_registered);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToSendOtpException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Otp_not_sent);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"w) controller -> UserController and method getUsers and getting UnableToSendOtpException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (MessagingException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_send_email);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.error("x) controller: register -> user not registered and getting MessagingException : message: "
					+ e.getMessage());
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToUpdateException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_update_details);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"y) controller -> UserController and method getUsers and getting UnableToUpdateException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (InvalidParametersException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Invalid_parameters);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"z) controller -> UserController and method getUsers and getting InvalidParametersException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info("z) controller -> UserController and method getUsers and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@PostMapping("/password/reset")
	@Transactional
	public ResponseEntity<?> resetPassword(@RequestBody LoginRequestModel loginRequestModel) {
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			if (loginRequestModel.getEmail() == null || loginRequestModel.getPassword() == null
					|| loginRequestModel.getOtp() == null || loginRequestModel.getEmail() == ""
					|| loginRequestModel.getPassword() == "" || loginRequestModel.getOtp() == ""
					|| loginRequestModel.getEmail() == " " || loginRequestModel.getPassword() == " "
					|| loginRequestModel.getOtp() == " ") {

				log.error("b) controller: login -> Either email or Password is missing and returning : "
						+ responseMessage);
				throw new InvalidParametersException("either userEmail or password missing");
			}
			UserModel user = userService.getUserByUserEmail(loginRequestModel.getEmail());

			long now = System.currentTimeMillis();
			long prev = Long.valueOf(user.getUserOtpTime()) + TimeUnit.MINUTES.toMillis(5);
			if (prev < now) {
				boolean sendEmailForOtp = emailService.sendEmailForOtp(user.getUserEmail());
				if (!sendEmailForOtp) {
					throw new UnableToSendOtpException("Unable to send otp on email");
				}
				throw new Exception("Something went wrong");

			} else {
				if (loginRequestModel.getOtp().equals(user.getUserOtp())) {
					boolean updatePassword = userService.updatePassword(loginRequestModel);
					if (updatePassword) {
						responseMessage.setMessage("Password Reset successful pls login again!");
						return ResponseEntity.ok(responseMessage);
					} else {
						throw new UnableToUpdateException("unable to update password");
					}
				} else {
					throw new InvalidOtpException("Given Otp is invalid");
				}
			}
		} catch (UserNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.User_not_registered);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToSendOtpException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Otp_not_sent);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"w) controller -> UserController and method getUsers and getting UnableToSendOtpException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (MessagingException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_send_email);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.error("x) controller: register -> user not registered and getting MessagingException : message: "
					+ e.getMessage());
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (UnableToUpdateException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unable_to_update_details);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"y) controller -> UserController and method getUsers and getting UnableToUpdateException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (InvalidParametersException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Invalid_parameters);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info(
					"z) controller -> UserController and method getUsers and getting InvalidParametersException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage() + " : " + e.getMessage());
			e.printStackTrace();
			log.info("z) controller -> UserController and method getUsers and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}