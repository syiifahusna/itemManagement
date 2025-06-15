package com.itemManagement.controller;

import com.itemManagement.entity.Notification;
import com.itemManagement.entity.User;
import com.itemManagement.entity.dto.ShortNotification;
import com.itemManagement.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("user/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping({"/", ""})
    public String notificationsPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();

        //list all user notifications
        List<Notification> notifications = notificationService.getUnreadNotifications(user.getId());
        List<ShortNotification> shortNotifications = notificationService.convertNotificationToShortNotification(notifications);

        model.addAttribute("shortNotifications",shortNotifications);
        return "user/notifications";
    }
}
