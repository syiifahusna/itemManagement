package com.itemManagement.controller.rest;

import com.itemManagement.payload.AuthRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthRestController {

   private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseMessage> authenticateUser(@RequestBody AuthRequest authRequest){
       ResponseMessage responseMessage = authService.authenticateUser(authRequest);
       return ResponseEntity.ok(responseMessage);
    }

}
