package com.example.Products.controllers;

import com.example.Products.models.OrderItemDTO;
import com.example.Products.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@Slf4j
public class CartController {

    private OrderService orderService;

    @PostMapping("/api/cart/update")
    public ResponseEntity<Void> updateQuantity(@RequestBody OrderItemDTO request, Principal principal) {
        //log.info(request.toString());
        orderService.updateOrder(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/cart/remove/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId, Principal principal) {
        orderService.removerProductFromCart(principal.getName(), productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/cart/checkout")
    public ResponseEntity<Void> checkout(Principal principal) {
        orderService.acceptOrder(principal.getName());
        return ResponseEntity.ok().build();
    }


}
