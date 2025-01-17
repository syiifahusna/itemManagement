package com.itemManagement.service;

import com.itemManagement.entity.Image;
import com.itemManagement.entity.User;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.ImageRepository;
import com.itemManagement.repository.UserRepository;
import com.itemManagement.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final ImageUtil imageUtil;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageRepository imageRepository, ImageUtil imageUtil, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageRepository = imageRepository;
        this.imageUtil = imageUtil;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public ResponseMessage updateProfileImage(MultipartFile profileImgRequest, Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED, "User not found", null);
        }

        if (profileImgRequest == null || profileImgRequest.isEmpty()) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST, OperationStatusEnum.FAILED, "No new image file", null);
        }

        String oldProfileImageName = (user.getProfileImg() != null && user.getProfileImg().getImgName() != null)
                ? user.getProfileImg().getImgName()
                : null;
        String newProfileImageName = imageUtil.updateImage(oldProfileImageName,profileImgRequest);
        Image newProfileImage = imageRepository.save(new Image(newProfileImageName));

        user.setProfileImg(newProfileImage);
        User savedUser = userRepository.save(user);

        userDetailsService.updateUserDetails(savedUser.getUsername());

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                "Profile image has been updated",null);
    }

    @Transactional
    public ResponseMessage updateUserField(Long id, Consumer<User> updateAction, String successMessage) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED, "User not found", null);
        }

        updateAction.accept(user);
        User savedUser = userRepository.save(user);

        userDetailsService.updateUserDetails(savedUser.getUsername());

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS, successMessage, null);
    }

    public ResponseMessage updateName(String nameRequest, Long id) {
        return updateUserField(id, user -> user.setName(nameRequest), "Name has been updated");
    }

    public ResponseMessage updateEmail(String emailRequest, Long id) {
        return updateUserField(id, user -> user.setEmail(emailRequest), "Email has been updated");
    }

    public ResponseMessage updateUsername(String usernameRequest, Long id) {
        return updateUserField(id, user -> user.setUsername(usernameRequest), "Username has been updated");
    }

    @Transactional
    public ResponseMessage updatePassword(String previousPasswordRequest, String newPasswordRequest, String confirmNewPasswordRequest, Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED, "User not found", null);
        }

        if (!passwordEncoder.matches(previousPasswordRequest, user.getPassword())) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST, OperationStatusEnum.FAILED, "Previous password does not match", null);
        }

        if (!newPasswordRequest.equals(confirmNewPasswordRequest)) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST, OperationStatusEnum.FAILED, "Password does not match", null);
        }

        user.setPassword(passwordEncoder.encode(newPasswordRequest));
        User savedUser = userRepository.save(user);

        userDetailsService.updateUserDetails(savedUser.getUsername());

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS, "Password has been updated", null);
    }
}
