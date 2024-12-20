package com.example.Products.services;

import com.example.Products.models.*;
import com.example.Products.respositories.OrderItemRepository;
import com.example.Products.respositories.OrderRepository;
import com.example.Products.respositories.ProductRepository;
import com.example.Products.respositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    UserRepository userRepository;

    private User getUser(String login){
        return userRepository.findByLogin(login);
    }

    public List<Order> orders(){
        return orderRepository.findAll();
    }

    public Order findById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> findByUser(String name){
        User user = userRepository.findByLogin(name);
        return orderRepository.findByUser(user);
    }

    public void updateOrder(String login, OrderItemDTO dto){
        Order cart = getOrCreateCart(getUser(login));
        OrderItemId id = new OrderItemId(cart.getOrderId(), dto.getProductId());
        OrderItem orderItem = orderItemRepository.findByOrderItemId(id);
        orderItem.setQuantity(dto.getQuantity());
        orderItemRepository.save(orderItem);
    }

    // Получить или же создать корзину
    public Order getOrCreateCart(User user){
        return orderRepository.findByUserAndStatus(user, Status.DRAFT)
                .orElseGet(() -> {
            Order newCart = new Order();
            newCart.setUser(user);
            newCart.setStatus(Status.DRAFT);
            return orderRepository.save(newCart);
        });
    }

    public List<Product> getProducts(String login){
        User user = getUser(login);
        Order cart = getOrCreateCart(user);

        List<OrderItem> orderItems = orderItemRepository.findByOrder(cart);
        List<Product> products = new ArrayList<>();

        for (OrderItem orderItem: orderItems){
            Product product = orderItem.getProduct();
            product.setQuantity(orderItem.getQuantity());
            products.add(product);
        }
        return products;
    }

    @Transactional
    public Order addProductToCart(String name, OrderItemDTO orderItemDTO){
        User user = getUser(name);
        if (user == null) log.info("User is NULL");
        log.info("OrderDTO = {}", orderItemDTO.getProductId());
        Order cart = getOrCreateCart(user);
        log.info("Корзина = {}", cart.toString());
        Product product = productRepository.findById(orderItemDTO.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));

        OrderItem orderItem;
        OrderItemId orderItemId = new OrderItemId(cart.getOrderId(), product.getProductId());

        orderItem = orderItemRepository.findByOrderItemId(orderItemId);
        if (orderItem != null){
            orderItem.setQuantity(orderItem.getQuantity() + 1);
        }
        else{
            orderItem = new OrderItem();

            orderItem.setOrderItemId(orderItemId);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setOrder(cart);
            orderItem.setProduct(product);
        }

        orderItemRepository.save(orderItem);

        return orderRepository.save(cart);
    }

    public Order removerProductFromCart(String name, Long productId){
        Order cart = getOrCreateCart(getUser(name));
        OrderItemId id = new OrderItemId(cart.getOrderId(), productId);
        orderItemRepository.deleteById(id);
        return cart;
    }

    // Оформить заказ
    public void acceptOrder(String login){
        Order cart = getOrCreateCart(getUser(login));
        cart.setStatus(Status.PREPARING);
        cart.setOrderDate(LocalDateTime.now());
        orderRepository.save(cart);
    }

    public void changeStatus(Order order, Status status){
        order.setStatus(status);
    }

    public List<Order> getAmount(String login){
        List<Order> orders = orderRepository.findByUser(getUser(login));
        orders.removeIf(order -> order.getStatus() == Status.DRAFT);
        for (Order order: orders){

            int total = 0;
            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
            for (OrderItem orderItem: orderItems){
                total += orderItem.getQuantity() * productRepository.findById(
                                orderItem.getProduct().getProductId())
                        .get()
                        .getPrice();
            }
            order.setTotalAmount(total);
        }
        return orders;
    }

    @Transactional
    public Order createOrder(List<OrderItemDTO> orderItemDTOS){
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);
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

    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null){
            orderItemRepository.deleteByOrder(order);
            orderRepository.deleteById(id);
        }
    }

}
