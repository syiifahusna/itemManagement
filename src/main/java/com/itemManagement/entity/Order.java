package com.itemManagement.entity;

import com.itemManagement.audit.Auditable;
import com.itemManagement.enums.OrderStatusEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItems> orderItems;

    private BigDecimal totalPrice;

    private String status;

    public Order() {}

    public Order(Long id, User user, List<OrderItems> orderItems, BigDecimal totalPrice, String status) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order(User user, List<OrderItems> orderItems, BigDecimal totalPrice, String status) {
        this.user = user;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
