package com.demo.fulfillment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Availability {

    @GetMapping("/live")
    public String alive(){
        return "Fulfillment Application online";
    }

    @GetMapping("/ready")
    public String well(){
        return "Fulfillment Application ready for orders";
    }
}
