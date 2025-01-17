package com.itemManagement.controller;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.Order;
import com.itemManagement.entity.User;
import com.itemManagement.payload.ItemRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.ItemService;
import com.itemManagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    private final ItemService itemService;
    private final OrderService orderService;

    @Autowired
    public EmployeeController(ItemService itemService, OrderService orderService) {
        this.itemService = itemService;
        this.orderService = orderService;
    }

    @GetMapping("/management/items")
    public String itamsManagementPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                      @RequestParam(value = "keyword",required = false) String keyword,
                                      @RequestParam(value = "field",required = false, defaultValue = "") String field,
                                      Model model){
        int currentPage = page.orElse(0);
        Page<Item> pageItem = itemService.getItems(currentPage, keyword, field);
        model.addAttribute("pageItem", pageItem);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("field", field);
        return "user/employee/items_management";
    }

    @GetMapping("/management/item/new")
    public String itemFormPage(Model model){
        model.addAttribute("itemRequest",new ItemRequest());
        return "user/employee/item_form";
    }

    @GetMapping("/management/item/edit/{id}")
    public String itemFormPage(@PathVariable("id") Long id, Model model){
        ResponseMessage responseMessage = itemService.getItem(id);
        Item item = (Item) responseMessage.getBody();
        model.addAttribute("itemRequest",item);
        return "user/employee/item_form";
    }

    @PostMapping("/management/item/save")
    public String saveItem(@Valid @ModelAttribute ItemRequest itemRequest, RedirectAttributes redirectAttributes){
        if(itemRequest.getId() == null){
            ResponseMessage responseMessage = itemService.newItem(itemRequest);
            redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
            return "redirect:/employee/management/item/new";
        }else{
            ResponseMessage responseMessage = itemService.updateItem(itemRequest);
            redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
            return "redirect:/employee/management/item/edit/" + itemRequest.getId();
        }
    }

    @PostMapping("/management/item/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = itemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/employee/management/items";
    }

    @GetMapping("/management/orders")
    public String ordersManagementPage(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                       @RequestParam(value = "from",required = false) LocalDate dateFrom,
                                       @RequestParam(value = "to",required = false) LocalDate dateTo,
                                       @RequestParam(value = "status",required = false, defaultValue = "") String status,
                                       Model model){

        int currentPage = page.orElse(0);
        Page<Order> pageOrder = orderService.getOrders(currentPage, dateFrom, dateTo, status);
        model.addAttribute("pageOrder", pageOrder);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("from", dateFrom);
        model.addAttribute("to", dateTo);
        model.addAttribute("status", status);

        return "user/employee/orders_management";
    }




    @GetMapping("/management/order/edit/{id}")
    public String orderFormPage(@PathVariable("id") Long id, Model model){
        ResponseMessage responseMessage = orderService.getOrder(id);
        Order order = (Order) responseMessage.getBody();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = order.getCreatedDate().format(formatter);

        String[] parts = formattedDateTime.split(" ");
        String orderDate = parts[0]; // Date part
        String orderTime = parts[1]; // Time part


        model.addAttribute("order",order);
        model.addAttribute("orderDate",orderDate);
        model.addAttribute("orderTime",orderTime);
        return "user/employee/order_form";
    }

    @PostMapping("/management/order/edit/updatestatus/{id}")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String statusRequest,
                               RedirectAttributes redirectAttributes){
        ResponseMessage responseMessage = orderService.updateOrderStatus(id,statusRequest);
        redirectAttributes.addFlashAttribute("responseMessage",responseMessage);
        return "redirect:/employee/management/order/edit/"+id;
    }
}
