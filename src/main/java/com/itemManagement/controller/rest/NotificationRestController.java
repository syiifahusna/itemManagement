package com.itemManagement.controller.rest;

import com.itemManagement.entity.Notification;
import com.itemManagement.entity.User;
import com.itemManagement.entity.dto.ShortNotification;
import com.itemManagement.payload.ResponseNotification;
import com.itemManagement.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationRestController {

    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseNotification> getUnreadNotification(){
        List<Notification> notifications =  notificationService.getUnreadNotifications(2L);
        List<ShortNotification> shortNotifications = notificationService.convertNotificationToShortNotification(notifications);
        ResponseNotification responseNotification = new ResponseNotification(shortNotifications.size(),shortNotifications);


        return ResponseEntity.ok(responseNotification);
    }

}
