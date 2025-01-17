package com.itemManagement.controller;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.Order;
import com.itemManagement.entity.User;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.ClientService;
import com.itemManagement.service.ItemService;
import com.itemManagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ItemService itemService;
    private final OrderService orderService;


    public ClientController(ClientService clientService, ItemService itemService, OrderService orderService) {
        this.clientService = clientService;
        this.itemService = itemService;
        this.orderService = orderService;
    }

    @GetMapping("/items")
    public String itemsPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                            @RequestParam(value = "keyword",required = false, defaultValue = "") String keyword,
                            @RequestParam(value = "field",required = false, defaultValue = "") String field,
                            Model model){
        int currentPage = page.orElse(0);
        Page<Item> pageItem = itemService.getItems(currentPage, keyword, field);
        model.addAttribute("pageItem", pageItem);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("field", field);
        return "user/client/items";
    }

    @GetMapping("/item/{id}")
    public String itemPage(@PathVariable("id") Long id,  Model model){
        ResponseMessage responseMessage = itemService.getItem(id);
        Item item = (Item) responseMessage.getBody();
        model.addAttribute("item",item);

        return "user/client/item";
    }

    @GetMapping("/addtocart")
    public String addToCart(@RequestParam(value = "id",required = true) Long id,
                            @RequestParam(value = "page",required = false) Optional<Integer> page,
                            @RequestParam(value = "keyword",required = false) String keyword,
                            @RequestParam(value = "field",required = false, defaultValue = "") String field,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes){

        int currentPage = page.orElse(0);
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = clientService.addToCart(id,user);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);

        String redirectUrl = String.format("redirect:/client/items?page=%d&keyword=%s&field=%s",
                currentPage,
                keyword != null ? keyword : "",
                field);

        return redirectUrl;
    }

    @GetMapping("/removefromcart")
    public String removeFromCart(@RequestParam(value = "id",required = true) Long id,
                                 Authentication authentication){

        User user = (User) authentication.getPrincipal();
        clientService.removeFromCart(id, user);

        String redirectUrl = String.format("redirect:/client/cart");
        return redirectUrl;
    }


    @GetMapping("/cart")
    public String cartPage(Authentication authentication,Model model){
        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = clientService.getCart(user);
        model.addAttribute("responseMessage", responseMessage);
        BigDecimal totalPrice = clientService.getTotalPrice(user);
        model.addAttribute("totalPrice", totalPrice);

        return "user/client/cart";
    }

    @GetMapping("/decreasequantity")
    public String decreaseQuantity(@RequestParam(value = "id",required = true) Long id,
                                   Authentication authentication){
        User user = (User) authentication.getPrincipal();
        clientService.decreaseQuantity(id, user);

        String redirectUrl = String.format("redirect:/client/cart");
        return redirectUrl;
    }

    @GetMapping("/increasequantity")
    public String increaseQuantity(@RequestParam(value = "id",required = true) Long id,
                                          Authentication authentication){
        User user = (User) authentication.getPrincipal();
        clientService.increaseQuantity(id, user);

        String redirectUrl = String.format("redirect:/client/cart");
        return redirectUrl;
    }

    @PostMapping("/placeorder")
    public String placeOrder(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Long orderId = clientService.createOrder(user);
        String redirectUrl = String.format("redirect:/client/order/" + orderId);
        return redirectUrl;
    }

    @GetMapping("/orders")
    public String ordersPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                             @RequestParam(value = "from",required = false) LocalDate dateFrom,
                             @RequestParam(value = "to",required = false) LocalDate dateTo,
                             @RequestParam(value = "status",required = false, defaultValue = "") String status,
                             Authentication authentication,
                             Model model){

        int currentPage = page.orElse(0);
        User user = (User) authentication.getPrincipal();
        Page<Order> pageOrder = orderService.getClientOrders(currentPage, dateFrom, dateTo, status, user);
        model.addAttribute("pageOrder", pageOrder);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("from", dateFrom);
        model.addAttribute("to", dateTo);
        model.addAttribute("status", status);

        return "user/client/orders";
    }

    @GetMapping("/order/{id}")
    public String orderPage(@PathVariable("id") Long id,
                            Authentication authentication,
                            Model model){

        User user = (User) authentication.getPrincipal();
        ResponseMessage responseMessage = orderService.getClientOrder(id, user);
        Order order = (Order) responseMessage.getBody();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = order.getCreatedDate().format(formatter);

        String[] parts = formattedDateTime.split(" ");
        String orderDate = parts[0]; // Date part
        String orderTime = parts[1]; // Time part


        model.addAttribute("order",order);
        model.addAttribute("orderDate",orderDate);
        model.addAttribute("orderTime",orderTime);


        return "user/client/order";
    }
}
