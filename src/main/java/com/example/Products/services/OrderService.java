package com.example.Products.services;

import com.example.Products.models.*;
import com.example.Products.respositories.OrderItemRepository;
import com.example.Products.respositories.OrderRepository;
import com.example.Products.respositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(List<OrderItemDTO> orderItemDTOS){
        Order order = orderRepository.save(new Order());
        for (OrderItemDTO orderItemDTO: orderItemDTOS){
            Product product = productRepository.findById(orderItemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));

            OrderItem orderItem = new OrderItem();
            OrderItemId orderItemId = new OrderItemId(order.getOrderId(), product.getProductId());

            orderItem.setOrderItemId(orderItemId);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItemRepository.save(orderItem);
        }
        return order;
    }

}
