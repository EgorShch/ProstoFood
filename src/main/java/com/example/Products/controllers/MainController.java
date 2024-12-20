package com.example.Products.controllers;

import com.example.Products.models.*;
import com.example.Products.services.OrderService;
import com.example.Products.services.ProductService;
import com.example.Products.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class MainController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/product")
    public String getProducts(Model model){
        Map<String, List<Product>> groupedProducts = productService.products()
                .stream().collect(Collectors.groupingBy(Product::getCategory));
        model.addAttribute("groupedProducts", groupedProducts);
        return "catalog";
    }

    @GetMapping("/products")
    public String getProductsByPage(
            @RequestParam(defaultValue = "1") int page, // Номер страницы (по умолчанию 1)
            Model model) {

        // Получаем список всех категорий продуктов
        List<String> categories = productService.products()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Проверяем корректность номера страницы
        if (page < 1 || page > categories.size()) {
            model.addAttribute("errorMessage", "Invalid page number.");
            return "error"; // Страница ошибки
        }

        // Определяем категорию для текущей страницы
        String currentCategory = categories.get(page - 1);

        // Фильтруем продукты для выбранной категории
        List<Product> products = productService.products()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(currentCategory))
                .collect(Collectors.toList());

        // Передаем данные в шаблон
        model.addAttribute("category", currentCategory);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.size());

        return "category"; // Возвращаем шаблон категории
    }


    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal){
        List<Order> orders = orderService.findByUser(principal.getName());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/category/{categoryName}")
    public String getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page, // Номер страницы
            @RequestParam(defaultValue = "10") int size, // Количество товаров на странице
            Model model) {
        // Фильтруем продукты по категории
        List<Product> products = productService.products()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());

        // Реализация пагинации
        int totalProducts = products.size(); // Общее количество товаров
        int fromIndex = Math.min(page * size, totalProducts); // Индекс начала
        int toIndex = Math.min(fromIndex + size, totalProducts); // Индекс конца
        List<Product> paginatedProducts = products.subList(fromIndex, toIndex);

        // Вычисляем общее количество страниц
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        model.addAttribute("category", categoryName);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "category";
    }


    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal){
        String login = principal.getName();

        List<Order> orders = orderService.getAmount(login);

        model.addAttribute("orders", orders);
        model.addAttribute("user", userService.getByLogin(login));

        return "profile";
    }

    @GetMapping("/cart")
    public String getCart(Model model, Principal principal){
        List<Product> products = orderService.getProducts(principal.getName());

        int total = products.stream()
                .mapToInt(product -> product.getPrice() * product.getQuantity())
                .sum();

        model.addAttribute("products", products);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable Long id, Model model){
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getByCategory(product.getCategory()));
        return "product";
    }


}
