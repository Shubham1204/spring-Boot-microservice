package com.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.config.ErrorMap;
import com.login.config.ErrorMap.ErrorMsg;
import com.login.entities.ResponseMessage;
import com.login.entities.RoleModel;
import com.login.exception.RightNotFoundException;
import com.login.exception.RoleExistsException;
import com.login.exception.RoleNotFoundException;
import com.login.exception.RoleNotSavedException;
import com.login.exception.RoleRightNotMappedException;
import com.login.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authenticated/admin/roles")
@CrossOrigin("*")
@Slf4j
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping("/add")
	@Transactional
	public ResponseEntity<?> addRole(@RequestBody RoleModel role) {
		log.info("ds) controller -> RoleController and method addRole and getting parameter RoleModel : " + role);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RoleModel addRole = roleService.addRole(role);
			log.info("dt) controller -> RoleController and method addRole after saving to db role : " + addRole);
			return ResponseEntity.ok(addRole);
		} catch (RightNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_right_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"du) controller -> RoleController  and method addRole and getting RightNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_role_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dv) controller -> RoleController  and method addRole and getting RoleNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleRightNotMappedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Role_right_not_mapped);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dw) controller -> RoleController  and method addRole and getting RoleRightNotMappedException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleNotSavedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Role_not_saved);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dx) controller -> RoleController  and method addRole and getting RoleNotSavedException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleExistsException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Role_already_exists);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dy) controller -> RoleController  and method addRole and getting RoleExistsException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("dz) controller -> RoleController  and method addRole and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getRoles() {
		log.info("ea) controller -> RoleController and method getRoles");
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			List<RoleModel> roles = roleService.getRoles();
			log.info("eb) controller -> RoleController and method getRoles getting role from db : " + roles);
			return ResponseEntity.ok(roles);
		} catch (RoleNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_role_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"ec) controller -> RoleController  and method getRoles and getting RoleNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("ed) controller -> RoleController  and method getRoles and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@GetMapping("/get/{rName}")
	public ResponseEntity<?> getRoleByName(@PathVariable("rName") String roleName) {
		log.info("ef) controller -> RoleController and method getRoleByName and getting parameter RoleName : "
				+ roleName);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RoleModel role = roleService.getRoleByRoleName(roleName);
			log.info("eg) controller -> RoleController and method getRoleByName getting role from db : " + role);
			return ResponseEntity.ok(role);
		} catch (RoleNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_role_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"eh) controller -> RoleController  and method getRoleByName and getting RoleNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("ei) controller -> RoleController  and method getRoleByName and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@PostMapping("/map/right")
	@Transactional
	public ResponseEntity<?> mapRoleRight(@RequestBody RoleModel role) {
		log.info("ej) controller -> RoleController and method mapRoleRight and getting parameter RoleModel : " + role);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			boolean mapRoleToRight = roleService.mapRoleToRight(role);
			log.info("ek) controller -> RoleController and method role right mapped : " + mapRoleToRight);
			responseMessage.setMessage("Role Right mapped for : "+role.getRoleName());
			return ResponseEntity.ok(responseMessage);
		} catch (RightNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_right_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"el) controller -> RoleController  and method mapRoleRight and getting RightNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_role_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"em) controller -> RoleController  and method mapRoleRight and getting RoleNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RoleRightNotMappedException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Role_right_not_mapped);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"en) controller -> RoleController  and method mapRoleRight and getting RoleRightNotMappedException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("eo) controller -> RoleController  and method mapRoleRight and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}