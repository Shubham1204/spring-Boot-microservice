package com.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.dao.UserRepository;
import com.login.entities.LoginRequestModel;
import com.login.entities.UserModel;
import com.login.exception.InvalidPasswordException;
import com.login.exception.UnableToAddException;
import com.login.exception.UnableToUpdateException;
import com.login.exception.UserExistsException;
import com.login.exception.UserNotFoundException;
import com.login.exception.UserRoleNotMappedException;
import com.login.util.PasswordEncryptDcrypt;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private PasswordEncryptDcrypt passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	public boolean addUser(UserModel user)
			throws UserExistsException, UserNotFoundException, UnableToAddException, UserRoleNotMappedException {
		log.info("ca) service -> UserService and method : addUser and saving to db with paramter recieved UserModel : "
				+ user);
		if (getUserByUserEmail(user.getUserEmail()) == null) {
			user.setUserPassword(passwordEncoder.encrypt(user.getUserPassword()));
			Integer saveUserQuery = userRepository.saveUserQuery(user.getUserName(), user.getUserEmail(),
					user.getUserContact(), user.getUserPassword(), user.getUserStatus(), user.getUserOtp(),
					user.getUserOtpTime());
			log.info("cb) service -> UserService and method : addUser after saving to db count : " + saveUserQuery);
			if (saveUserQuery > 0) {
				Integer mapUserRole = userRepository.mapUserRole(user.getUserEmail(), user.getRole().getRoleName());
				log.info("cc) service -> UserService and method : addUser after mapping to db count : " + mapUserRole);
				if (mapUserRole > 0) {
					log.info("cd) service -> UserService and method : addUser and return to calling function");
					return true;
				} else {
					log.error("ce) service -> UserService and method : addUser and getting UserRoleNotMappedException");
					throw new UserRoleNotMappedException("Unable to map user to role");
				}
			} else {
				log.error("cf) service -> UserService and method : addUser and getting UnableToAddException");
				throw new UnableToAddException("Unable to add user to db");
			}
		} else {
			log.error("cg) service -> UserService and method : addUser and getting UserExistsException");
			throw new UserExistsException("user already exists in db");
		}
	}

	public boolean updateOtp(String userEmail, String otp) throws UserNotFoundException, UnableToUpdateException {
		log.info(
				"ch) service -> UserService and method : updateOtp and updating to db with paramter recieved UserEMail : "
						+ userEmail + " and otp : " + otp);
		if (getUserByUserEmail(userEmail) != null) {
			long currentTime = System.currentTimeMillis();
			log.info(
					"ci) service -> UserService and method : updateOtp and updating otp to db with paramter recieved UserEMail : "
							+ userEmail + " and otp : " + otp + " and currentTime : " + currentTime);
			Integer updateOtpAndTime = userRepository.updateOtpAndTime(userEmail, otp, currentTime);
			log.info("cj) service -> UserService and method : updateOtp after updating to db count" + updateOtpAndTime);
			if (updateOtpAndTime > 0) {
				log.info("ck) service -> UserService and method : updateOtp and returning to calling fn");
				return true;
			} else {
				return false;
//				log.error("cl) service -> UserService and method : updateOtp and getting UnableToUpdateException");
//				throw new UnableToUpdateException("Unable to update otp");
			}
		} else {
			return false;
//			log.error("cm) service -> UserService and method : updateOtp and getting UserNotFoundException");
//			throw new UserNotFoundException("User Email does not exist in db");
		}
	}

	public boolean updateUserStatus(String userEmail) throws UserNotFoundException, UnableToUpdateException {
		log.info(
				"cn) service -> UserService and method : updateUserStatus and updating to db with paramter recieved UserEMail : "
						+ userEmail);
		if (getUserByUserEmail(userEmail) != null) {
			Integer updateUserStatus = userRepository.updateUserStatus(userEmail);
			log.info(
					"co) service -> UserService and method : updateUserStatus after updating to db with paramter count : "
							+ updateUserStatus);
			if (updateUserStatus > 0) {
				log.info("cp) service -> UserService and method : updateUserStatus and return to calling function");
				return true;
			} else {
				return false;
//				log.error(
//						"cq) service -> UserService and method : updateUserStatus and getting UnableToUpdateException");
//				throw new UnableToUpdateException("Unable to update user Status");
			}
		} else {
			return false;
//			log.error("cr) service -> UserService and method : updateUserStatus and getting UserNotFoundException");
//			throw new UserNotFoundException("User Email does not exist in db");
		}
	}

	public boolean updatePassword(LoginRequestModel loginRequestModel) throws UserNotFoundException {
		if (getUserByUserEmail(loginRequestModel.getEmail()) != null) {
			loginRequestModel.setPassword(passwordEncoder.encrypt(loginRequestModel.getPassword()));
			Integer updatePassword = userRepository.updatePassword(loginRequestModel.getEmail(),
					loginRequestModel.getOtp(), loginRequestModel.getPassword());
			if (updatePassword > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public List<UserModel> getUsers() throws UserNotFoundException {
		log.info("cs) service -> UserService and method : getUsers");
		List<UserModel> findAllUsers = userRepository.findAll();
		log.info("ct) service -> UserService and method : getUsers and findAllUsers : " + findAllUsers);
		if (findAllUsers.size() > 0) {
			log.info("cu) service -> UserService and method : getUsers and return to calling fn");
			return findAllUsers;
		} else {
			log.error("cv) service -> UserService and method : getUsers and getting UserNotFoundException");
			throw new UserNotFoundException("Unable to find any user in db");
		}
	}

	public UserModel getUserByUserEmail(String userEmail) throws UserNotFoundException {
		log.info(
				"cw) service -> UserService and method : getUserByUserEmail and getting from db with paramter recieved UserEMail : "
						+ userEmail);
		UserModel user = userRepository.findByUserEmail(userEmail);
		log.info(
				"cx) service -> UserService and method : getUserByUserEmail and getting from db with paramter recieved UserModel : "
						+ user);
		if (user != null) {
//			user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
			log.info("cy) service -> UserService and method : getUserByUserEmail and return to calling fn");
			return user;
		} else {
			return null;
		}
//			log.error("cz) service -> UserService and method : getUserByUserEmail and getting UserNotFoundException");
//			throw new UserNotFoundException("Unable to find user in db");
	}

	public UserModel getUserByUserEmailAndUserPassword(LoginRequestModel loginRequestModel)
			throws InvalidPasswordException, UserNotFoundException {
//		log.info("parameters recieved : "+loginRequestModel +" : "+passwordEncoder.matches(loginRequestModel.getPassword(), "$2a$10$EA5cvacrvlRpX1Kkm2NWMegwtwkUF3OCGgsqrX6rZ/l5pj4iXhb.e"));
		loginRequestModel.setPassword(passwordEncoder.encrypt(loginRequestModel.getPassword()));
		log.info(
				"da) service -> UserService and method : getUserByUserEmailAndUserPassword and getting from db with paramter recieved login request model : "
						+ loginRequestModel);
//		log.info("pass : "+loginRequestModel.getPassword());
		UserModel findByUserEmailAndUserPassword = userRepository
				.findByUserEmailAndUserPassword(loginRequestModel.getEmail(), loginRequestModel.getPassword());

		log.info("db) service -> UserService and method : getUserByUserEmailAndUserPassword and user model : "
				+ findByUserEmailAndUserPassword);
		if (findByUserEmailAndUserPassword != null) {
			log.info(
					"dc) service -> UserService and method : getUserByUserEmailAndUserPassword and return to calling fn");
			return findByUserEmailAndUserPassword;
		} else {
			return null;
//			if (getUserByUserEmail(loginRequestModel.getEmail()) != null) {
//				log.error("dd) service -> UserService and method : getUserByUserEmailAndUserPassword and getting InvalidPasswordException");
//				throw new InvalidPasswordException("Invalid/ Wrong Password");
//			} else {
//				log.error("de) service -> UserService and method : getUserByUserEmailAndUserPassword and getting UserNotFoundException");
//				throw new UserNotFoundException("Unable to find any user with this email");
//			}
		}
	}
}