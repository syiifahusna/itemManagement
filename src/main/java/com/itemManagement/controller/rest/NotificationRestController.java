package com.itemManagement.controller.rest;

import com.itemManagement.entity.Notification;
import com.itemManagement.entity.User;
import com.itemManagement.entity.dto.ShortNotification;
import com.itemManagement.payload.ResponseNotification;
import com.itemManagement.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/notification")
public class NotificationRestController {

    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/getunread")
    public ResponseEntity<ResponseNotification> getUnreadNotification(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        List<Notification> notifications =  notificationService.getUnreadNotifications(user.getId());
        List<ShortNotification> shortNotifications = notificationService.convertNotificationToShortNotification(notifications);

        ResponseNotification responseNotification = new ResponseNotification(shortNotifications.size(),shortNotifications);
        return ResponseEntity.ok(responseNotification);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ResponseNotification> readNotification(@PathVariable("id") Long id,
                                                                 Authentication authentication){
        System.out.println("requested ..... to update ...........");
        User user = (User) authentication.getPrincipal();
        notificationService.readNotification(id,user.getId());

        List<Notification> notifications =  notificationService.getUnreadNotifications(user.getId());
        List<ShortNotification> shortNotifications = notificationService.convertNotificationToShortNotification(notifications);

        ResponseNotification responseNotification = new ResponseNotification(shortNotifications.size(),shortNotifications);
        return ResponseEntity.ok(responseNotification);
    }

}
