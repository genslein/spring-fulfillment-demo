package com.demo.fulfillment.models.subtypes;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItem {
    private String sku;

    private Integer quantity;

    private Double unitPrice;
}
