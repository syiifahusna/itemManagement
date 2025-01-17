package com.itemManagement.entity;

import com.itemManagement.audit.Auditable;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_cust_id"))
    private User user;

    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CartItems> cartItems;


    public Cart(){}

    public Cart(Long id, User user, List<CartItems> cartItems, String note) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
    }

    public Cart(User user, List<CartItems> cartItems, String note) {
        this.user = user;
        this.cartItems = cartItems;
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

    public List<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItems> cartItems) {
        this.cartItems = cartItems;
    }


    public void addCartItems(CartItems cartItems){
        this.cartItems.add(cartItems);
    }

    public void removeCartItems(CartItems cartItems){
        this.cartItems.remove(cartItems);
    }
}
