package com.itemManagement.service;

import com.itemManagement.entity.*;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.enums.OrderStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.CartRepository;
import com.itemManagement.repository.ItemRepository;
import com.itemManagement.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ClientService {

    public final ItemRepository itemRepository;
    private  final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    public ClientService(ItemRepository itemRepository, CartRepository cartRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public ResponseMessage addToCart(Long id,  User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> createCart(user));

        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Item not found", null);
        }

        Item item = optionalItem.get();

        Optional<CartItems> optionalCartItems = cart.getCartItems().stream()
                .filter(cartItem ->
                        cartItem.getItem().equals(item))
                .findFirst();

        if(optionalCartItems.isPresent()){
            cart.getCartItems().stream()
                    .filter(cartItems -> cartItems.getItem().getName().equals(optionalCartItems.get().getItem().getName()))
                    .findFirst()
                    .ifPresent(cartItems -> cartItems.setQuantity(cartItems.getQuantity() + 1));
        }else{
            CartItems cartItems = new CartItems(cart,item,1);
            cart.addCartItems(cartItems);
        }

        cartRepository.save(cart);

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                "Item " + item.getName() + " added to cart",null);
    }

    public void removeFromCart(Long id,User user){
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> createCart(user));

        cart.getCartItems().removeIf(cartItems -> {
            return cartItems.getItem().getId().equals(id);
        });

        cartRepository.save(cart);

    }

    public ResponseMessage getCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> createCart(user));
        if(cart.getCartItems().isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Cart is empty", null);
        }

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                null, optionalCart.get());

    }

    public void  decreaseQuantity(Long id, User user){
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> createCart(user));

        cart.getCartItems().stream().filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findFirst()
                .ifPresent(cartItem ->{
                            cartItem.setQuantity(cartItem.getQuantity() - 1);
                            if(cartItem.getQuantity() <= 0){
                                cart.getCartItems().remove(cartItem);
                            }
                        }
                );

        cartRepository.save(cart);
    }

    public void  increaseQuantity(Long id, User user){
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> createCart(user));

        cart.getCartItems().stream().filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + 1));

        cartRepository.save(cart);
    }

    @Transactional
    private Cart createCart(User user){
        Cart newCart = cartRepository.save(new Cart(user,null,null));
        return newCart;
    }

    public BigDecimal getTotalPrice(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if(optionalCart.isEmpty()){
            return BigDecimal.ZERO;
        }

        return optionalCart.get().getCartItems().stream()
                .map(cartItem -> cartItem.getItem().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Long createOrder(User user) {

        Order order = new Order(user,null,BigDecimal.valueOf(0.0), OrderStatusEnum.PLACED.toString());

        //get cart
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.get();

        //get cart Items;
        List<CartItems> cartItems = cart.getCartItems();

        //get total price
        BigDecimal totalPrice = getTotalPrice(user);

        //convert cart items to order items
        List<OrderItems> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItems(order,cartItem.getItem(), cartItem.getQuantity()))
                .toList();

        //create order
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        //clear cart item
        cart.getCartItems().clear();

        //save order and save cart items
        cartRepository.save(cart);
        Order newOrder = orderRepository.save(order);

        return newOrder.getId();
    }
}
