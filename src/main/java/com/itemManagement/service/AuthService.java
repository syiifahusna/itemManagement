package com.itemManagement.service;

import com.itemManagement.entity.*;
import com.itemManagement.entity.Cart;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.AuthRequest;
import com.itemManagement.payload.RegisterRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.*;
import com.itemManagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final RoleRepository roleRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final AccConfirmationTokenRepository accConfirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;

    //============Jwt===================
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ResetPasswordTokenRepository resetPasswordTokenRepository, AccConfirmationTokenRepository accConfirmationTokenRepository, MailingService mailingService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.accConfirmationTokenRepository = accConfirmationTokenRepository;
        this.mailingService = mailingService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public ResponseMessage register(RegisterRequest registerRequest){
        if(userRepository.existsByUsername(registerRequest.getUsername()) && userRepository.existsByEmail(registerRequest.getEmail())){
            return  new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Username or email has already been used",null);
        }

        Optional<Role> optionalRole = roleRepository.findRoleByAuthority("CLIENT");
        if(optionalRole.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Client type user does not exist",null);
        }

        Set<Role> roles = new HashSet<>(Collections.singleton(optionalRole.get()));



        User user = new User(
                capitalizeWords(registerRequest.getName().trim()),
                registerRequest.getEmail().trim().toLowerCase(),
                registerRequest.getUsername().trim(),
                passwordEncoder.encode(registerRequest.getPassword()),
               null,
                roles,
                true,
                true,
                true,
                false);

        User newUser = userRepository.save(user);
        Cart cart = new Cart(newUser,null,null);

        cartRepository.save(cart);

        String token = UUID.randomUUID().toString();
        AccConfirmationToken accConfirmationToken = new AccConfirmationToken(
                token,
                newUser,
                LocalDateTime.now().plusDays(7)
        );

        accConfirmationTokenRepository.save(accConfirmationToken);

        mailingService.sendAccConfirmationMail(user.getName(), user.getEmail(), token);

        return new ResponseMessage(HttpStatus.CREATED, OperationStatusEnum.SUCCESS,
                "You account has been created. Please confirm your email before login",null);

    }

    @Transactional
    public ResponseMessage accountConfirmation(String tokenRequest){

        Optional<AccConfirmationToken> accConfirmationTokenOptional = accConfirmationTokenRepository.findByToken(tokenRequest);
        if(accConfirmationTokenOptional.isPresent()){
            AccConfirmationToken accConfirmationToken = accConfirmationTokenOptional.get();
            if(accConfirmationToken.getExpirationDate().isAfter(LocalDateTime.now())){
                if (!accConfirmationToken.getUser().isEnabled()) {
                    User user = accConfirmationToken.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);
                    accConfirmationTokenRepository.save(accConfirmationToken);

                    return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,
                            "Account Confirmed",null);
                }
            }
        }

        return new ResponseMessage(HttpStatus.NOT_FOUND,OperationStatusEnum.FAILED,
                "Unfortunately, this link is unavailable or expired",null);
    }

    @Transactional
    public ResponseMessage forgotPassword(String emailRequest){
        emailRequest = emailRequest.trim();
        if(!isValidEmail(emailRequest)){
            return new ResponseMessage(HttpStatus.BAD_REQUEST,OperationStatusEnum.FAILED,
                    "Email is not valid",null);
        }

        Optional<User> optionalUser = userRepository.findUserByEmail(emailRequest);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            String token = UUID.randomUUID().toString();
            ResetPasswordToken resetPasswordToken =
                    new ResetPasswordToken(
                            token,
                            user,
                            LocalDateTime.now().plusDays(1));

            resetPasswordTokenRepository.save(resetPasswordToken);

            mailingService.sendResetPasswordAccMail(user.getName(), user.getEmail(), token);

            return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,
                    "Email has been sent",null);
        }

        return new ResponseMessage(HttpStatus.NOT_FOUND,OperationStatusEnum.FAILED,
                "Email is not registered",null);
    }

    @Transactional
    public ResponseMessage resetPassword(String tokenRequest,
                                         String newPasswordRequest,
                                         String confirmPasswordRequest){

        Optional<ResetPasswordToken> resetPasswordTokenOptional = resetPasswordTokenRepository.findByToken(tokenRequest);
        if(resetPasswordTokenOptional.isPresent()){
            ResetPasswordToken resetPasswordToken= resetPasswordTokenOptional.get();
            if(resetPasswordToken.getExpirationDate().isAfter(LocalDateTime.now())){
                if(newPasswordRequest.equals(confirmPasswordRequest)){
                    User user = resetPasswordToken.getUser();


                    String newPassword = passwordEncoder.encode(confirmPasswordRequest);
                    user.setPassword(newPassword);

                    userRepository.save(user);

                    resetPasswordToken.setExpirationDate(LocalDateTime.now());
                    resetPasswordTokenRepository.save(resetPasswordToken);

                    return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,
                            "Password has been reset",null);
                }else{
                    return new ResponseMessage(HttpStatus.BAD_REQUEST,OperationStatusEnum.FAILED,
                            "Passwords do not match!",null);
                }
            }
        }
        return new ResponseMessage(HttpStatus.NOT_FOUND,OperationStatusEnum.FAILED,
                "Unfortunately, this link is unavailable or expired",null);
    }



    @Transactional
    public ResponseMessage createPassword(String tokenRequest,
                                          String newPasswordRequest,
                                          String confirmPasswordRequest){

        //filter token
        Optional<AccConfirmationToken> accConfirmationTokenOptional = accConfirmationTokenRepository.findByToken(tokenRequest);
        if(accConfirmationTokenOptional.isPresent()) {
            AccConfirmationToken accConfirmationToken = accConfirmationTokenOptional.get();
            if (accConfirmationToken.getExpirationDate().isAfter(LocalDateTime.now())) {
                if(newPasswordRequest.equals(confirmPasswordRequest)){
                    User user = accConfirmationToken.getUser();

                    String newPassword = passwordEncoder.encode(confirmPasswordRequest);
                    user.setPassword(newPassword);
                    user.setEnabled(true);

                    userRepository.save(user);

                    accConfirmationToken.setExpirationDate(LocalDateTime.now());
                    accConfirmationTokenRepository.save(accConfirmationToken);

                    return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,
                            "Account password has been created. Account confirmed",null);
                }else{
                    return new ResponseMessage(HttpStatus.BAD_REQUEST,OperationStatusEnum.FAILED,
                            "Passwords do not match!",null);
                }
            }
        }
        return new ResponseMessage(HttpStatus.NOT_FOUND,OperationStatusEnum.FAILED,
                "Unfortunately, this link is unavailable or expired",null);
    }


    private String capitalizeWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence;
        }

        String[] words = sentence.split(" ");
        StringBuilder capitalizedSentence = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                // Capitalize the first letter and append the rest
                capitalizedSentence.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Remove the trailing space and return the result
        return capitalizedSentence.toString().trim();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        return pattern.matcher(email).matches();
    }

    //============Jwt===================

    public ResponseMessage authenticateUser(AuthRequest authRequest){

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                );
        try {
            authenticationManager.authenticate(authenticationToken);
            String jwtToken = jwtUtil.generateToken(authRequest.getUsername());
            return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,"Authorized","Bearer " + jwtToken);

        }catch (Exception e){
            return new ResponseMessage(HttpStatus.UNAUTHORIZED,OperationStatusEnum.FAILED,"Incorrect username or password",e.getMessage());
        }
    }
}
