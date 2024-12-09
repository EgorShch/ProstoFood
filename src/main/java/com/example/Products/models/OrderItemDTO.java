package com.example.Products.models;

import lombok.Data;

@Data
public class OrderItemDTO {
    Long productId;
    int quantity;
}
