package com.login.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.exception.UnableToSendOtpException;
import com.login.exception.UnableToUpdateException;
import com.login.exception.UserNotFoundException;
import com.login.util.EmailUtil;
import com.login.util.GenerateOtpUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailUtil emailUtil;

	public boolean sendEmailForOtp(String userEmail) throws UnableToSendOtpException, MessagingException, UserNotFoundException, UnableToUpdateException {
		log.info(
				"at) Inside Service: EmailService and method : sendEmailForOtp paramter recieved email : " + userEmail);
			GenerateOtpUtil generateOtpHelper = new GenerateOtpUtil();
			String otp = generateOtpHelper.generateOtp();
			log.info("au) service -> EmailService generated otp is : " + otp);
			boolean isUpdateOtp = userService.updateOtp(userEmail, otp);
			log.info("av) service -> EmailService otp sent in db : " + isUpdateOtp);
			if (isUpdateOtp) {
				log.info("aw) service -> EmailService before sending otp on email, userEmail : "+userEmail+" and otp : "+otp);
//				boolean sent = emailUtil.sendEmail(otp, userEmail);
				boolean sent = true;
				log.info("ax) service -> EmailService after sending otp on email : " + sent);
				if (sent) {
					return true;
				} else {
					log.error("ay) service -> EmailService getting MessagingException");
					throw new MessagingException("Unable to send Email");
				}
			} else {
				log.error("az) service -> EmailService getting UnableToSendOtpException");
				throw new UnableToSendOtpException("Unable to Send Otp");
			}
	}
}