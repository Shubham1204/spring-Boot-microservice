package com.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.login.entities.RightModel;
import com.login.exception.RightExistsException;
import com.login.exception.RightNotFoundException;
import com.login.service.RightService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authenticated/admin/rights")
@CrossOrigin("*")
@Slf4j
public class RightController {

	@Autowired
	private RightService rightService;

	@PostMapping("/add")
	public ResponseEntity<?> addRight(@RequestBody RightModel right) {
		log.info("df) controller -> RightController and method addRight and getting parameter RightModel : " + right);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RightModel savedRight = rightService.addRight(right);
			log.info("dg) controller -> RightController and method addRight after saving right to db RightModel : "
					+ savedRight);
			return ResponseEntity.ok(savedRight);
		} catch (RightExistsException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Right_already_exists);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dh) controller -> RightController and method addRight and getting RightExistsException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (RightNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_right_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"di) controller -> RightController and method addRight and getting RightNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("dj) controller -> RightController and method addRight and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getRights() {
		log.info("dk) controller -> RightController and method getRights");
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			List<RightModel> rights = rightService.getRights();
			log.info("dl) controller -> RightController and method getRights and rights are : " + rights);
			return ResponseEntity.ok(rights);
		} catch (RightNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_right_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dm) controller -> RightController and method getRights and getting RightNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info("dn) controller -> RightController and method getRights and getting Exception with response : "
					+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}

	@GetMapping("/get/{rightName}")
	public ResponseEntity<?> getRightByName(@PathVariable("rightName") String rightName) {
		log.info("do) controller -> RightController and method getRightByName and getting parameter right name : "
				+ rightName);
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RightModel right = rightService.getRightByName(rightName);
			log.info("dp) controller -> RightController and method getRightByName and right : " + right);
			return ResponseEntity.ok(right);
		} catch (RightNotFoundException e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Specified_right_does_not_exist);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dq) controller -> RightController and method getRightByName and getting RightNotFoundException with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		} catch (Exception e) {
			responseMessage = ErrorMap.errMap.get(ErrorMsg.Unknown_Error);
			responseMessage.setMessage(responseMessage.getMessage()+" : "+e.getMessage());
			e.printStackTrace();
			log.info(
					"dr) controller -> RightController and method getRightByName and getting Exception with response : "
							+ responseMessage);
			return ResponseEntity.status(responseMessage.getErroHttpStatus()).body(responseMessage);
		}
	}
}