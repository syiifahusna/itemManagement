package com.itemManagement.repository;

import com.itemManagement.entity.Order;
import com.itemManagement.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findAllByUserAndStatus(
            @Param("user") User user,
            @Param("status") String status,
            Pageable pageable
    );

    Page<Order> findAllByUserAndCreatedDateBetween(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    Page<Order> findAllByUserAndStatusAndCreatedDateBetween(
            @Param("user") User user,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );


    Optional<Order> findByIdAndUser(Long id, User user);



    Page<Order> findAllByCreatedDateBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<Order> findAllByStatusAndCreatedDateBetween(String status, LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<Order> findByStatus(String status, Pageable pageable);
}
