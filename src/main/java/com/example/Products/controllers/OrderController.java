package com.example.Products.controllers;

import com.example.Products.models.Order;
import com.example.Products.models.OrderItemDTO;
import com.example.Products.models.Product;
import com.example.Products.models.User;
import com.example.Products.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @GetMapping
    public List<Order> orders(){
        return orderService.orders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(Principal principal, @RequestBody OrderItemDTO orderItemDTO){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to add products to the cart");
        }
        orderItemDTO.setQuantity(1);
        orderService.addProductToCart(principal.getName(), orderItemDTO);
        return ResponseEntity.ok("Da");
    }

    @DeleteMapping()
    public ResponseEntity<Order> removeProduct(Principal principal, @RequestBody Long product){
        return ResponseEntity.ok(orderService.removerProductFromCart(principal.getName(), product));
    }

}
