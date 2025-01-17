package com.itemManagement.repository;

import com.itemManagement.entity.Cart;
import com.itemManagement.entity.CartItems;
import com.itemManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUser(User user);
}
