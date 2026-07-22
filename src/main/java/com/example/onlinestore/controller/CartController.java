package com.example.onlinestore.controller;

import com.example.onlinestore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add/{id}")
    @ResponseBody
    public Map<String, Object> addToCartAjax(@PathVariable Long id, HttpSession session) {
        cartService.addToCart(session, id);

        // Возвращаем новые данные корзины
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartItemCount", cartService.getCartItems(session).size());
        response.put("cartTotalAmount", cartService.getTotalAmount(session));
        return response;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("totalAmount", cartService.getTotalAmount(session));
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(session, id);
        return "redirect:/cart";
    }
}