package com.login.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.login.entities.ResponseMessage;

public class ErrorMap {

	public static enum ErrorMsg{
		Insufficient_Privileges_for_the_user,
		Unknown_Error,
		Either_Email_Password_missing,
		Either_Email_Otp_missing,
		User_not_verified_check_email_for_otp,
		User_not_registered,
		user_already_exists,
		Unable_to_send_email,
		Unable_to_update_details,
		User_role_not_mapped,
		Otp_not_sent,
		Invalid_Password,
		Invalid_Otp,
		Page_not_found,
		User_does_not_exist,
		Right_already_exists,
		Role_already_exists,
		Role_not_saved,
		Role_right_not_mapped,
		User_is_not_Administrator,
		Specified_role_does_not_exist,
		Specified_right_does_not_exist,
		Insufficient_Rights_for_current_operation,
		Invalid_parameters,
		Invalid_Session,
		User_account_inactive,
		Invalid_Search_Field_type,
		Invalid_Field_Length,
		No_data_found,
		Cannot_assign_rights_to_user,
		Attachment_does_not_exist
	}
	
		public static Map<ErrorMsg, ResponseMessage> errMap = new HashMap<>();
		static {
			errMap.put(ErrorMsg.Unknown_Error, new ResponseMessage("Unknown Error",-50000,HttpStatus.INTERNAL_SERVER_ERROR));
		errMap.put(ErrorMsg.Insufficient_Privileges_for_the_user, new ResponseMessage("Insufficient Privileges for the user",-50001,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_Password, new ResponseMessage("Invalid Password",-50002,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Page_not_found, new ResponseMessage("Page not found",-50003,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.User_does_not_exist, new ResponseMessage("User does not exist",-50004,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.User_is_not_Administrator, new ResponseMessage("User is not Administrator",-50005,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Specified_role_does_not_exist, new ResponseMessage("Specified role does not exist",-50006,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Specified_right_does_not_exist, new ResponseMessage("Specified right does not exist",-50007,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Insufficient_Rights_for_current_operation, new ResponseMessage("Insufficient Rights for current operation",-50008,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_parameters, new ResponseMessage("Invalid parameters",-50009,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_Session, new ResponseMessage("Invalid Session",-50010,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.User_account_inactive, new ResponseMessage("User account is inactive",-50011,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_Search_Field_type, new ResponseMessage("Invalid Search Field type",-50012,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_Field_Length, new ResponseMessage("Invalid Field Length",-50013,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.No_data_found, new ResponseMessage("No data found",-50014,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Cannot_assign_rights_to_user, new ResponseMessage("Cannot assign rights to user",-50015,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Attachment_does_not_exist, new ResponseMessage("Attachment does not exist",-50016,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Either_Email_Password_missing, new ResponseMessage("Please enter both Email and Password. Either Email or Password is missing.",-50017,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.User_not_verified_check_email_for_otp, new ResponseMessage("userStatus is Pending so user has to verify the email first ask user to check email for otp",-50018, HttpStatus.UNAUTHORIZED));
		errMap.put(ErrorMsg.User_not_registered, new ResponseMessage("user is not authorized/or user not registered",-50019, HttpStatus.UNAUTHORIZED));
		errMap.put(ErrorMsg.Otp_not_sent, new ResponseMessage("Otp not sent to user",-50020, HttpStatus.FAILED_DEPENDENCY));
		errMap.put(ErrorMsg.Either_Email_Otp_missing, new ResponseMessage("Please enter both Email and otp. Either Email or otp is missing.",-50021,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Right_already_exists, new ResponseMessage("Given Right already exists in database",-50022,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Role_already_exists, new ResponseMessage("Given Role already exists in database",-50023,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Role_not_saved, new ResponseMessage("Given Role is not saved database",-50024,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Role_right_not_mapped, new ResponseMessage("Given Role is not mapped to given right",-50025,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.user_already_exists, new ResponseMessage("Given user already exist in db",-50026,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.User_role_not_mapped, new ResponseMessage("Given user is not mapped to given role",-50027,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Unable_to_send_email, new ResponseMessage("Unable to send email",-50028,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Unable_to_update_details, new ResponseMessage("Unable to update user details",-50029,HttpStatus.BAD_REQUEST));
		errMap.put(ErrorMsg.Invalid_Otp, new ResponseMessage("Invalid otp",-50030,HttpStatus.BAD_REQUEST));
		}

}
