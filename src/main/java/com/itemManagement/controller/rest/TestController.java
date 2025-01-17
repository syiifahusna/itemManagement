package com.itemManagement.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	
	private final TestService testService;
	
	@Autowired
	public TestController(TestService testService) {
		this.testService = testService;
	}

	@GetMapping("/{state}")
    public ResponseEntity<ResponseMessage> getStatePostcodes(@PathVariable("state") String state) {
		ResponseMessage responseMessage = testService.getPostcode(state);
		return ResponseEntity.ok().body(responseMessage);
		
	}
	
	
}
