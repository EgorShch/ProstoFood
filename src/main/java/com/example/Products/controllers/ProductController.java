package com.example.Products.controllers;

import com.example.Products.models.Product;
import com.example.Products.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public List<Product> products(){
        return productService.products();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id){
        return productService.getProduct(id);
    }

    @PostMapping()
    public String saveProduct(@RequestBody Product product){
        productService.saveProduct(product);
        return "Успешно!";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return "Успешно!";
    }

}
