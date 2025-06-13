package com.itemManagement.entity.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ShortNotification {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime createdTime;
    private String timePass;


    public ShortNotification(){}

    public ShortNotification(Long id, String title, String message, LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getTimePass() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else {
            long days = seconds / 86400;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }

    public void setTimePass(String timePass) {
        this.timePass = timePass;
    }

    @Override
    public String toString() {
        return "ShortNotification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createdTime=" + createdTime +
                ", timePass='" + timePass + '\'' +
                '}';
    }
}
