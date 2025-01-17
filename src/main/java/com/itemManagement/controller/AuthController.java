package com.itemManagement.controller;

import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.RegisterRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "msg", required = false) String msg,
                        Model model){

        msg = (msg != null) ? msg.replace("_", " ") : "";
        ResponseMessage responseMessage;
        if (msg.equals("You have logged out")) {
            //when logout
            responseMessage = new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,msg,null);
            model.addAttribute("responseMessage", responseMessage);
        } else if (msg.equals("Wrong credential")) {
            //when login fail
            responseMessage =new ResponseMessage(HttpStatus.OK, OperationStatusEnum.FAILED,msg,null);
            model.addAttribute("responseMessage", responseMessage);
        }

        return "public/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("registerRequest",new RegisterRequest());
        return "public/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           Model model){
        ResponseMessage responseMessage;
        if(bindingResult.hasErrors()){
            List<String> errorFields = bindingResult.getFieldErrors().stream().map(e -> e.getField()).toList();
            String invalidField = "Field "+ String.join(",", errorFields) +" are invalid";
            responseMessage = new ResponseMessage(
                    HttpStatus.BAD_REQUEST,
                    OperationStatusEnum.FAILED,
                    invalidField,
                    null);
        }else{
            responseMessage = authService.register(registerRequest);
        }

        model.addAttribute("responseMessage", responseMessage);
        return registerPage(model);
    }

    @GetMapping("/account_confirmation")
    public String accountConfirmationPage(@RequestParam("token") String tokenRequest,
                                          Model model){
        ResponseMessage responseMessage = authService.accountConfirmation(tokenRequest);
        model.addAttribute("responseMessage",responseMessage);
        return "public/account_confirmation";
    }

    @GetMapping("/forgot_password")
    public String forgotPasswordPage(Model model){
        return "public/forgot_password";
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestParam(name = "email") String emailRequest,
                                 Model model){

        ResponseMessage responseMessage = authService.forgotPassword(emailRequest);
        model.addAttribute("responseMessage",responseMessage);
        return forgotPasswordPage(model);
    }

    @GetMapping ("/reset_password")
    public String resetPasswordPage(@RequestParam(value = "token") String tokenRequest,
                                    Model model){
        model.addAttribute("token",tokenRequest);
        return "public/reset_password";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam(name = "token") String tokenRequest,
                                @RequestParam(name = "newPassword") String newPasswordRequest,
                                @RequestParam(name = "confirmPassword") String confirmPasswordRequest,
                                Model model){
        ResponseMessage responseMessage = authService.resetPassword(tokenRequest,newPasswordRequest,confirmPasswordRequest);
        model.addAttribute("responseMessage",responseMessage);
        return resetPasswordPage(tokenRequest,model);
    }

    @GetMapping ("/create_password")
    public String createPasswordPage(@RequestParam(value = "token") String tokenRequest, Model model){
        model.addAttribute("token",tokenRequest);
        return "public/create_password";
    }

    @PostMapping("/create_password")
    public String createPassword(@RequestParam(name = "token") String tokenRequest,
                                @RequestParam(name = "newPassword") String newPasswordRequest,
                                @RequestParam(name = "confirmPassword") String confirmPasswordRequest,
                                Model model){
        ResponseMessage responseMessage = authService.createPassword(tokenRequest,newPasswordRequest,confirmPasswordRequest);
        model.addAttribute("responseMessage",responseMessage);
        return createPasswordPage(tokenRequest,model);
    }
}
