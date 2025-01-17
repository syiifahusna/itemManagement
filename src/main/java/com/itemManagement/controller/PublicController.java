package com.itemManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class PublicController {

    @GetMapping("/")
    public String index(){
        return "public/index";
    }

    @GetMapping("/about")
    public String aboutPage(){
        return "public/about";
    }


}
