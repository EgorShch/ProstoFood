package com.example.Products.respositories;

import com.example.Products.models.OrderItem;
import com.example.Products.models.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
