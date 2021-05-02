package com.demo.fulfillment.controllers;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.services.OrderService;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(name = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class Orders {

    @Autowired
    OrderService orderService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the Customer Orders",
                    content = {
                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = Pair.class))
            }),
            @ApiResponse(responseCode = "400",
                    description = "No customer orders",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Issue searching for customer orders",
                    content = @Content)
    })
    @ResponseBody List<Pair<Customer, Order>> getCustomerOrders() {
        return orderService.getEachCustomerFirstOrder();
    }

    @GetMapping
    @ResponseBody List<Order> searchOrders(@QuerydslPredicate Predicate predicate) {
        return orderService.searchOrders(predicate);
    }
}
