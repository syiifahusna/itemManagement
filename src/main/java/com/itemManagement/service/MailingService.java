package com.itemManagement.service;

import com.itemManagement.entity.Mailing;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.MailingRepository;
import com.itemManagement.util.EncryptDecryptUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

@Service
public class MailingService {

    private final MailingRepository mailingRepository;
    private final EncryptDecryptUtil encryptDecryptUtil;

    public MailingService(MailingRepository mailingRepository, EncryptDecryptUtil encryptDecryptUtil) {
        this.mailingRepository = mailingRepository;
        this.encryptDecryptUtil = encryptDecryptUtil;
    }

    public JavaMailSender getMailSender(Long senderId) {
        Mailing config = mailingRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Mail sender not found with ID: " + senderId));

        try {

            String host = encryptDecryptUtil.decrypt(config.getHost());
            int port = encryptDecryptUtil.decryptInteger(config.getPort());
            String username = encryptDecryptUtil.decrypt(config.getUsername());
            String password = encryptDecryptUtil.decrypt(config.getPassword());
            String protocol = encryptDecryptUtil.decrypt(config.getProtocol());

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(username);
            mailSender.setPassword(password);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", protocol);
            props.put("mail.smtp.auth", config.isAuth());
            props.put("mail.smtp.starttls.enable", config.isTlsEnabled());
            props.put("mail.smtp.starttls.required", config.isTlsRequired());
            props.put("mail.debug", config.isDebugEnabled());

            return mailSender;

        } catch (Exception e) {
            System.out.println("Error on mail sender decryption");
            return null;
        }
    }

    public void sendAccConfirmationMail(String recipientName, String recipientEmailAddress, String token){
        String subject = "Registration Confirmation";
        String confirmationUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/account_confirmation")
                .queryParam("token", token)
                .toUriString();

        String htmlMsg ="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Confirmation Email</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            font-size: 12px;\n" +
                "            color: #aaaaaa;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Confirmation Required</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Dear "+ recipientName +",</p>\n" +
                "            <p>Thank you for signing up! Please confirm your email address to complete your registration.</p>\n" +
                "            <a href=\""+ confirmationUrl +"\" class=\"btn\">Confirm Email</a>\n" +
                "            <p>If you did not sign up for this account, please disregard this email.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>&copy; Item Management. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";


        try{
            JavaMailSender senderMail = getMailSender(1l);

            MimeMessage mimeMessage = senderMail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(recipientEmailAddress);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);

            senderMail.send(mimeMessage);


        }catch (MessagingException e){
            System.out.println(e.getMessage());
            //logger.error("Error occurred while registering user: ", e);
        }



    }

    public void sendResetPasswordAccMail(String recipientName, String recipientEmailAddress, String token){

        String subject = "Password Reset Request";
        String resetPasswordUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/reset_password")
                .queryParam("token", token)
                .toUriString();


        String htmlMsg="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Reset Password</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            font-size: 12px;\n" +
                "            color: #aaaaaa;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Reset Your Password</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Dear "+ recipientName +",</p>\n" +
                "            <p>We received a request to reset your password. Click the button below to reset it.</p>\n" +
                "            <a href=\""+ resetPasswordUrl +"\" class=\"btn\">Reset Password</a>\n" +
                "            <p>If you did not request a password reset, please ignore this email or contact support if you have concerns.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>&copy; Item Management. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        try{
            JavaMailSender senderMail = getMailSender(1l);

            MimeMessage mimeMessage = senderMail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(recipientEmailAddress);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);

            senderMail.send(mimeMessage);

        }catch (MessagingException e){
            System.out.println(e.getMessage());
        }
    }

    public void sendCreateAccountPasswordMail(String recipientName, String recipientEmailAddress, String token){
        String subject = "Create Account Password";
        String createPasswordUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/create_password")
                .queryParam("token", token)
                .toUriString();

        String htmlMsg="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Create Password</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "            background-color: #0000FF;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            font-size: 12px;\n" +
                "            color: #aaaaaa;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Create Your Password</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Dear "+ recipientName +",</p>\n" +
                "            <p>Welcome! Please click the button below to create your new password and activate your account.</p>\n" +
                "            <a href=\""+ createPasswordUrl +"\" class=\"btn\">Create Password</a>\n" +
                "            <p>If you did not request this, please ignore this email or contact support if you have concerns.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>&copy; Item Management. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        try{
            JavaMailSender senderMail = getMailSender(1l);

            MimeMessage mimeMessage = senderMail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(recipientEmailAddress);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);

            senderMail.send(mimeMessage);

        }catch (MessagingException e){
            System.out.println(e.getMessage());
        }

    }

    public ResponseMessage getMailing(){
        Optional<Mailing> optionalMailing = mailingRepository.findById(1l);
        if (optionalMailing.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Mailing not found", null);
        }

        try {
            Mailing mailing = optionalMailing.get();
            mailing.setHost(encryptDecryptUtil.decrypt(mailing.getHost()));
            mailing.setPort(encryptDecryptUtil.decrypt(mailing.getPort()));
            mailing.setUsername(encryptDecryptUtil.decrypt(mailing.getUsername()));
            mailing.setPassword(encryptDecryptUtil.decrypt(mailing.getPassword()));
            mailing.setProtocol(encryptDecryptUtil.decrypt(mailing.getProtocol()));


            return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                    "Found mailing", mailing);
        }catch (Exception e){
            System.out.println("Error on mailing decryption");
            return null;
        }

    }

    @Transactional
    public ResponseMessage updateMailingField(Consumer<Mailing> updateAction, String message) {

        if (updateAction == null) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST,OperationStatusEnum.FAILED,message,null);
        }

        Mailing mailing = mailingRepository.findById(1l).orElse(null);

        if (mailing == null) {
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED, "Mailing not found", null);
        }

        updateAction.accept(mailing);
        mailingRepository.save(mailing);

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS, message, null);
    }

    public ResponseMessage updateHost(String hostRequest) {
        try{
            String encryptHost = encryptDecryptUtil.encrypt(hostRequest);
            return updateMailingField(mailing -> mailing.setHost(encryptHost), "Host has been updated");
        }catch (Exception e){
            return updateMailingField(null,"Error encrypting host request");
        }
    }

    public ResponseMessage updatePort(String portRequest) {
        try{
            String encryptPort = encryptDecryptUtil.encrypt(portRequest);
            return updateMailingField(mailing -> mailing.setPort(encryptPort), "Port has been updated");
        }catch (Exception e){
            return updateMailingField(null,"Error encrypting port request");
        }
    }

    public ResponseMessage updateUsername(String usernameRequest) {
        try{
            String encryptUsername = encryptDecryptUtil.encrypt(usernameRequest);
            return updateMailingField(mailing -> mailing.setUsername(encryptUsername), "Username has been updated");
        }catch (Exception e){
            return updateMailingField(null,"Error encrypting username request");
        }
    }

    public ResponseMessage updatePassword(String previousPasswordRequest,
                                          String newPasswordRequest,
                                          String confirmNewPasswordRequest) {
        try{

            Mailing mailing = mailingRepository.findById(1l).orElse(null);

            if (mailing == null) {
                return updateMailingField(null,"Mailing not found");
            }

            String decryptPassword = encryptDecryptUtil.decrypt(mailing.getPassword());
            if(!decryptPassword.equals(previousPasswordRequest)){
                return updateMailingField(null,"Previous password does not match");
            }

            if(!newPasswordRequest.equals(confirmNewPasswordRequest)){
                return updateMailingField(null,"Password does not match");
            }

            String encryptPassword = encryptDecryptUtil.encrypt(confirmNewPasswordRequest);
            return updateMailingField(mail -> mail.setPassword(encryptPassword), "Password has been updated");

        }catch (Exception e){
            return updateMailingField(null,"Error encrypting password request");
        }
    }

    public ResponseMessage updateProtocol(String protocolRequest) {
        try{
            String encryptProtocol = encryptDecryptUtil.encrypt(protocolRequest);
            return updateMailingField(mailing -> mailing.setProtocol(encryptProtocol), "Protocol has been updated");
        }catch (Exception e){
            return updateMailingField(null,"Error encrypting protocol request");
        }
    }


    public ResponseMessage updateAuth(boolean authRequest) {
        return updateMailingField(mailing -> mailing.setAuth(authRequest), "Authenticate has been updated");
    }

    public ResponseMessage updateEnableTls(boolean enableTlsRequest) {
        return updateMailingField(mailing -> mailing.setTlsEnabled(enableTlsRequest), "Enable Tls has been updated");

    }

    public ResponseMessage updateRequireTls(boolean requireTlsRequest) {
        return updateMailingField(mailing -> mailing.setTlsRequired(requireTlsRequest), "Require Tls has been updated");

    }

    public ResponseMessage updateEnableDebug(boolean enableDebugRequest) {
        return updateMailingField(mailing -> mailing.setDebugEnabled(enableDebugRequest), "Enable debug has been updated");

    }

}
