package com.itemManagement.controller.rest;

import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ItemRestController {

    private final ItemService itemService;

    @Autowired
    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<ResponseMessage> getItems(){
        ResponseMessage responseMessage = itemService.getItems();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<ResponseMessage> getItems(@PathVariable("id") Long id){
        ResponseMessage responseMessage = itemService.getItem(id);
        return ResponseEntity.ok(responseMessage);
    }


}
