package com.example.Products.services;

import com.example.Products.models.OrderItemDTO;
import com.example.Products.models.Product;
import com.example.Products.respositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Product> getProductDTO(List<OrderItemDTO> DTOs){
        List<Product> products = new ArrayList<>();
        for (OrderItemDTO dto: DTOs){
            products.add(getProduct(dto.getProductId()));
        }
        return products;
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProduct(long id){
        productRepository.deleteById(id);
    }

    public List<Product> getByCategory(String category){
        return productRepository.findByCategory(category);
    }

}
