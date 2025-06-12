package com.itemManagement.payload;

import com.itemManagement.entity.dto.ShortNotification;

import java.util.List;

public class ResponseNotification {

    private int countNotification;
    private List<ShortNotification> notifications;

    public ResponseNotification(int countNotification, List<ShortNotification> notifications) {
        this.countNotification = countNotification;
        this.notifications = notifications;
    }

    public int getCountNotification() {
        return countNotification;
    }

    public void setCountNotification(int countNotification) {
        this.countNotification = countNotification;
    }

    public List<ShortNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<ShortNotification> notifications) {
        this.notifications = notifications;
    }
}
