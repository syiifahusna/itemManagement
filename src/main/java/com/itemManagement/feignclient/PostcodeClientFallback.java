package com.itemManagement.feignclient;

import org.springframework.stereotype.Component;

@Component
public class PostcodeClientFallback  implements PostcodeClient {

	@Override
	public String getPostcodesByState(String state) {
		// TODO Auto-generated method stub
		return null;
	}

}
