package com.cloudGateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "/loginServiceFallBack")
	public String LoginServiceFallBackMethod() {
		return "Unable to connect to the service. Please try again later.";
	}
	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "/courseServiceFallBack")
	public String CourseServiceFallBackMethod() {
		return "Unable to connect to the service. Please try again later.";
	}
}
