package com.login.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseModel {

	private String userName;
	private String userEmail;
	private RoleModel role;
	private String token;
}
