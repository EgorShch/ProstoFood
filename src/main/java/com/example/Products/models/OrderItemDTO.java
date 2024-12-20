package com.example.Products.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    Long productId;
    int quantity;

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
