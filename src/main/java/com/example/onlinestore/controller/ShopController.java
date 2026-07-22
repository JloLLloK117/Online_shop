package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Product;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
public class ShopController {

    private final ProductRepository productRepository;
    private final CartService cartService;

    public ShopController(ProductRepository productRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @GetMapping("/shop")
    public String shop(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String query,
            Model model,
            HttpSession session
    ) {
        List<Product> products;


        String cleanQuery = (query != null) ? query.trim() : "";

        if (!cleanQuery.isEmpty()) {

            if (category != null && !category.trim().isEmpty()) {
                products = productRepository.searchByCategoryAndQuery(category.trim(), cleanQuery);
                model.addAttribute("selectedCategory", category.trim());
                model.addAttribute("searchQuery", cleanQuery);
            }

            else {
                products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(cleanQuery, cleanQuery);
                model.addAttribute("searchQuery", cleanQuery);
            }
        }

        else if (category != null && !category.trim().isEmpty()) {
            products = productRepository.findByCategory(category.trim());
            model.addAttribute("selectedCategory", category.trim());
        }

        else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("cartItemCount", cartService.getCartItems(session).size());
        model.addAttribute("cartTotalAmount", cartService.getTotalAmount(session));

        return "shop";
    }

    @GetMapping("/api/suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);

        return products.stream()
                .map(Product::getName)
                .limit(5) // Ограничиваем до 5 подсказок
                .toList();
    }
}
