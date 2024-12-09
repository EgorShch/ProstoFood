package com.example.Products.services;

import com.example.Products.models.Product;
import com.example.Products.respositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product>products(){
        return productRepository.findAll();
    }

    public Product getProduct(long id){
        return productRepository.findById(id).orElse(null);
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProduct(long id){
        productRepository.deleteById(id);
    }

}
