package com.itemManagement.repository;

import com.itemManagement.entity.Notification;
import com.itemManagement.enums.NotificationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByStatusAndUser_Id(NotificationStatusEnum status, Long userId);

}
