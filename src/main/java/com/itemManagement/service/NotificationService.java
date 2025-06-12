package com.itemManagement.service;

import com.itemManagement.entity.Notification;
import com.itemManagement.entity.Order;
import com.itemManagement.entity.User;
import com.itemManagement.entity.dto.ShortNotification;
import com.itemManagement.enums.NotificationStatusEnum;
import com.itemManagement.payload.ResponseNotification;
import com.itemManagement.repository.NotificationRepository;
import com.itemManagement.repository.OrderRepository;
import com.itemManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public NotificationService(OrderRepository orderRepository, UserRepository userRepository, NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void saveNotification(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        //add account is enable later
        Optional<User> optionalEmployee = userRepository.findById(2L);

        if(optionalOrder.isPresent() && optionalEmployee.isPresent()){
            Order order = optionalOrder.get();
            User employee = optionalEmployee.get();

            Notification notification = new Notification(
                            employee,
                            "New Order Received",
                            "Check new order from " + order.getUser().getUsername()
                                    + " with order id " + order.getId(),
                            NotificationStatusEnum.NEW);

            notificationRepository.save(notification);

            // Send notification via webSocket
            sendNotificationUpdate(employee.getId());
        }

    }

    public List<Notification> getUnreadNotifications(Long employeeId){
        return notificationRepository.findByStatusAndUser_Id(NotificationStatusEnum.NEW,employeeId);
    }

    @Transactional
    public void readNotification(Long notificationId, Long employeeId) {
//        Notification notification = notificationRepository.findById(notificationId)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        notification.setStatus(NotificationStatusEnum.READ);
//        notificationRepository.save(notification);
//
//        // Send updated notification via webSocket
//        sendNotificationUpdate(employeeId);
    }

    private void sendNotificationUpdate(Long employeeId) {

        List<Notification> notifications =  getUnreadNotifications(2L);
        List<ShortNotification> shortNotifications = convertNotificationToShortNotification(notifications);

        ResponseNotification responseNotification = new ResponseNotification(shortNotifications.size(),shortNotifications);
        messagingTemplate.convertAndSend("/topic/notifications", responseNotification);
    }

    public List<ShortNotification> convertNotificationToShortNotification(List<Notification> notifications){
        List<ShortNotification> shortNotifications = notifications.stream()
                .map(notification -> {
                    LocalDateTime createdTime = notification.getCreatedDate(); // assuming Auditable has getCreatedDate()
                    ShortNotification shortNotification = new ShortNotification();
                    shortNotification.setId(notification.getId());
                    shortNotification.setTitle(notification.getTitle());
                    shortNotification.setMessage(notification.getMessage());
                    shortNotification.setCreatedTime(createdTime);

                    return shortNotification;
                })
                .collect(Collectors.toList());

        return shortNotifications;
    }
}
