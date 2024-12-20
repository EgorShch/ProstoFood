package com.example.Products.respositories;

import com.example.Products.models.Order;
import com.example.Products.models.Status;
import com.example.Products.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, Status status);
    List<Order> findByUser(User user);
}
