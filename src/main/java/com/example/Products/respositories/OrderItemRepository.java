package com.example.Products.respositories;

import com.example.Products.models.Order;
import com.example.Products.models.OrderItem;
import com.example.Products.models.OrderItemId;
import com.example.Products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    void deleteByOrder(Order order);
    void deleteByOrderItemId(OrderItemId orderItemId);
    OrderItem findByOrderItemId(OrderItemId orderItemId);
    List<OrderItem> findByOrder(Order order);
}
