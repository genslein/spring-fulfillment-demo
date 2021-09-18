package com.demo.fulfillment.controllers;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.services.OrderService;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class Orders {

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the Customer Orders",
                    content = {
                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                array = @ArraySchema(
                                        schema = @Schema(implementation = Pair.class)
                                ))}),
            @ApiResponse(responseCode = "400",
                    description = "No customer orders",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Issue searching for customer orders",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @ResponseBody List<Pair<Order, Customer>> getCustomerOrders() {
        return orderService.getEachCustomerOrder();
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List all Orders",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Order.class)
                                    ))}),
            @ApiResponse(responseCode = "400",
                    description = "No Orders found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500",
                    description = "Issue searching for Orders",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @ResponseBody List<Order> searchOrders(@QuerydslPredicate(root = Order.class) Predicate predicate) {
        return orderService.searchOrders(predicate);
    }
}
