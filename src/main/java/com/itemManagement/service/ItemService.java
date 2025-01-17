package com.itemManagement.service;

import com.itemManagement.entity.Item;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ItemRequest;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> getItems(int page, String keyword, String field) {
        Pageable pageable = PageRequest.of(page,50);
        switch (field) {
            case "name" :
                return itemRepository.findByNameContainingIgnoreCaseAndStatusTrue(keyword,pageable);
            case "price" :
                return itemRepository.findByPriceAndStatusTrue(new BigDecimal(keyword),pageable);
            case "description" :
                return itemRepository.findByDescriptionContainingIgnoreCaseAndStatusTrue(keyword,pageable);
            default :
                return itemRepository.findAllByStatusTrue(pageable);
        }
    }

    public ResponseMessage getItems() {
        List<Item> itemList = itemRepository.findAllByStatusTrue();
        if(itemList.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "No item found", null);
        }
        return new ResponseMessage(HttpStatus.OK,OperationStatusEnum.SUCCESS,"Item found",itemList);
    }

    public ResponseMessage getItem(Long id){

        Optional<Item> optionalItem = itemRepository.findByIdAndStatusTrue(id);
        return optionalItem.map(item ->
                        new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                                "Found item", item))
                .orElseGet(() ->
                        new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                                "Item not found", null));
    }

    @Transactional
    public ResponseMessage newItem(ItemRequest itemRequest){

        //probably add some kind of validation here in future
        Item item = new Item(
                itemRequest.getName(),
                itemRequest.getDescription(),
                itemRequest.getPrice(),
                true
        );

        itemRepository.save(item);

        return new ResponseMessage(HttpStatus.CREATED, OperationStatusEnum.SUCCESS,
                "New item has been created",null);
    }

    @Transactional
    public ResponseMessage updateItem(ItemRequest itemRequest){

        Optional<Item> optionalItem = itemRepository.findByIdAndStatusTrue(itemRequest.getId());
        if(optionalItem.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Item does not exist",null);
        }

        Item item = optionalItem.get();
        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setPrice(itemRequest.getPrice());

        itemRepository.save(item);

        //update images here

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                "Item has been updated",null);
    }

    @Transactional
    public ResponseMessage deleteItem(Long id){
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                    "Item does not exist", null);
        }

        Item item = optionalItem.get();
        item.setStatus(false);

        itemRepository.save(item);

        return new ResponseMessage(HttpStatus.OK, OperationStatusEnum.SUCCESS,
                "Item has been deleted", null);
    }

}
