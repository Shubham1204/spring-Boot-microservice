package com.login.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.login.entities.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{

	
	@Modifying
	@Query(value = "INSERT INTO user_mst(user_name,user_email,user_contact,user_password,user_status,user_otp,user_otp_time) VALUES ( ?1 , ?2 , ?3 , ?4 , ?5 , ?6 , ?7 )",nativeQuery = true)
	Integer saveUserQuery(String userName, String userEmail,String userContact,String userPassword,char userStatus,String userOtp, String userOtpTime);

	
	@Modifying
	@Query(value = "INSERT INTO user_role_mapping(user_id,role_id) VALUES ((select user_id from user_mst where user_email= ?1 ),(select role_id from role_mst where role_name= ?2 ) )",nativeQuery = true)
	Integer mapUserRole(String userEmail, String roleName);
	
	
	UserModel findByUserEmail(String userEmail);
	
	UserModel findByUserEmailAndUserPassword(String email, String password);

	@Modifying
	@Query(value = "update user_mst set user_otp= ?2 , user_otp_time= ?3  where user_email= ?1 ",nativeQuery = true)
	Integer updateOtpAndTime(String userEmail, String otp,long currentTime);

	@Modifying
	@Query(value = "update user_mst set user_status=\'Y\'  where user_email= ?1 ",nativeQuery = true)
	Integer updateUserStatus(String userEmail);

	@Modifying
	@Query(value = "update user_mst set user_password= ?3 where user_email= ?1 and user_otp= ?2",nativeQuery = true)
	Integer updatePassword(String userEmail, String otp,String password);
}
