package com.itemManagement.service;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.Order;
import com.itemManagement.entity.User;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseMessage getClientOrder(Long id, User user) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUser(id,user);
        return optionalOrder.map(order ->
                        new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                                "Found order", order))
                .orElseGet(() ->
                        new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                                "Order not found", null));
    }

    public Page<Order> getClientOrders(int page, LocalDate dateFrom, LocalDate dateTo, String status, User user) {
        Pageable pageable = PageRequest.of(page,50);

        // Check if dateTo is before dateFrom
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            return new PageImpl<>(List.of(), pageable, 0); // Return an empty page
        }

        // Handle different combinations of null/empty arguments
        if (status == null || status.isEmpty()) {
            if (dateFrom != null && dateTo != null) {
                return orderRepository.findAllByUserAndCreatedDateBetween(
                        user,
                        dateFrom.atStartOfDay(),
                        dateTo.atTime(23, 59, 59),
                        pageable
                );
            } else {
                return orderRepository.findByUser(user, pageable);
            }
        } else {
            if (dateFrom != null && dateTo != null) {
                return orderRepository.findAllByUserAndStatusAndCreatedDateBetween(
                        user,
                        status,
                        dateFrom.atStartOfDay(),
                        dateTo.atTime(23, 59, 59),
                        pageable
                );
            } else {
                return orderRepository.findAllByUserAndStatus(user, status, pageable);
            }
        }
    }

    public Page<Order> getOrders(int page, LocalDate dateFrom, LocalDate dateTo, String status) {
        Pageable pageable = PageRequest.of(page,50);

        // Check if dateTo is before dateFrom
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            return new PageImpl<>(List.of(), pageable, 0); // Return an empty page
        }

        // Handle different combinations of null/empty argumentss
        if (status == null || status.isEmpty()) {
            if (dateFrom != null && dateTo != null) {
                return orderRepository.findAllByCreatedDateBetween(
                        dateFrom.atStartOfDay(),
                        dateTo.atTime(23, 59, 59),
                        pageable
                );
            } else {
                return orderRepository.findAll(pageable);
            }
        }else{
            if (dateFrom != null && dateTo != null) {
                return orderRepository.findAllByStatusAndCreatedDateBetween(
                        status,
                        dateFrom.atStartOfDay(),
                        dateTo.atTime(23, 59, 59),
                        pageable
                );
            } else {
                return orderRepository.findByStatus(status, pageable);
            }
        }
    }


    public ResponseMessage getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(order ->
                        new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                                "Found order", order))
                .orElseGet(() ->
                        new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                                "Order not found", null));
    }

    public ResponseMessage updateOrderStatus(Long id,String statusRequest) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND,OperationStatusEnum.FAILED,"Order not found",null);
        }

        Order order = optionalOrder.get();
        order.setStatus(statusRequest);
        orderRepository.save(order);
        return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,"Status has been updated",null);
    }
}
