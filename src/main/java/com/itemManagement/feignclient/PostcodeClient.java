package com.itemManagement.feignclient;


import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
		name = "postcode-client",
		url = "https://raw.githubusercontent.com",
		fallback = PostcodeClientFallback.class
)
public interface PostcodeClient{
	
	@GetMapping("/syiifahusna/malaysia-postcodes/refs/heads/master/{state}.json")
	String getPostcodesByState(@PathVariable("state") String state);
	
	
}