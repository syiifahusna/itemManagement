package com.itemManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.itemManagement.feignclient.PostcodeClient;
import com.itemManagement.payload.ResponseMessage;

import feign.Response;
import feign.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.itemManagement.entity.dto.State;

import com.itemManagement.enums.OperationStatusEnum;

@Service
public class TestService{

	private final PostcodeClient postcodeClient;
	
	@Autowired
	public TestService(PostcodeClient postcodeClient) {
		this.postcodeClient = postcodeClient;
	}
	
	public ResponseMessage getPostcode(String state) {
		try {
			String response = postcodeClient.getPostcodesByState(state);
			
			ObjectMapper objectMapper = new ObjectMapper();
			State stateDto = objectMapper.readValue(response, State.class);

			return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,"Postcodes found",stateDto);
		}catch(Exception e) {
		
		    return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, OperationStatusEnum.FAILED, "Failed to fetch postcodes",e.getMessage());
		}
				
	}
}
