package com.example.Products.controllers;

import com.example.Products.models.Order;
import com.example.Products.models.OrderItemDTO;
import com.example.Products.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class FirstController {

    private OrderService orderService;

   /* @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World!");
    }*/

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody List<OrderItemDTO> orderItemDTOS){
        Order order = orderService.createOrder(orderItemDTOS);
        return ResponseEntity.ok(order);
    }


}
