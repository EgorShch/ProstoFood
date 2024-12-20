package com.example.Products.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    long productId;

    String name;

    int price;

    String description;

    String category;

    String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;

    @Transient
    int quantity;
}
