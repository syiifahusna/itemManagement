package com.itemManagement.service;

import com.itemManagement.entity.AccConfirmationToken;
import com.itemManagement.entity.Role;
import com.itemManagement.entity.User;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.payload.UserRequest;
import com.itemManagement.repository.AccConfirmationTokenRepository;
import com.itemManagement.repository.RoleRepository;
import com.itemManagement.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccConfirmationTokenRepository accConfirmationTokenRepository;

    private final MailingService mailingService;

    public EmployeeService(UserRepository userRepository, RoleRepository roleRepository, AccConfirmationTokenRepository accConfirmationTokenRepository, MailingService mailingService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accConfirmationTokenRepository = accConfirmationTokenRepository;
        this.mailingService = mailingService;
    }

    public Page<User> getEmployees(int page){
        Pageable pageable = PageRequest.of(page,50);
        Page<User> employeePageable = userRepository.findByRoles_Authority("EMPLOYEE",pageable);
        return employeePageable;
    }

    public ResponseMessage getEmployee(Long id){

        //change find by id and authority
        Optional<User> employee = userRepository.findById(id);
        return employee.map(user ->
                new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                        "Found employee", user))
                .orElseGet(() ->
                        new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                                "Employee not found", null));
    }

    @Transactional
    public ResponseMessage newEmployee(UserRequest userRequest){
        if(userRepository.existsByEmail(userRequest.getEmail()) || userRepository.existsByUsername(userRequest.getUsername())){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Username or email has already been used", null);
        }

        Optional<Role> optionalRole = roleRepository.findRoleByAuthority("EMPLOYEE");
        if(optionalRole.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Employee type user does not exist",null);
        }

        Set<Role> roles = new HashSet<>(Collections.singleton(optionalRole.get()));

        User user = new User(
                userRequest.getName(),
                userRequest.getEmail(),
                userRequest.getUsername(),
                null,
                null,
                roles,
                true,
                true,
                true,
                false
        );

        User newUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        AccConfirmationToken accConfirmationToken = new AccConfirmationToken(
                token,
                newUser,
                LocalDateTime.now().plusDays(7)
        );


        accConfirmationTokenRepository.save(accConfirmationToken);

        mailingService.sendCreateAccountPasswordMail(userRequest.getName(),userRequest.getEmail(),token);

        return new ResponseMessage(HttpStatus.CREATED, OperationStatusEnum.SUCCESS,
                "New employee has been created. Employee must create account password to confirm their account",null);
    }

    @Transactional
    public ResponseMessage updateEmployee(UserRequest userRequest){
        //change find by id and authority
        Optional<User> optionalUser = userRepository.findById(userRequest.getId());
        if(optionalUser.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Employee type user does not exist",null);
        }

        User user = optionalUser.get();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());

        userRepository.save(user);

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                "Employee has been updated",null);
    }

    @Transactional
    public ResponseMessage disableEmployee(Long id){

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Boolean isEmployee = user.getRoles().stream()
                    .anyMatch(r -> "EMPLOYEE".equals(r.getAuthority()));
            if(isEmployee){
                //disable account
                user.setEnabled(false);
                userRepository.save(user);

                return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                        "Employee has been disabled", null);
            }
        }
        return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                "Employee not found", null);
    }


}
