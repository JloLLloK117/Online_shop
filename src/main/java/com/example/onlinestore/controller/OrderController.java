package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Order;
import com.example.onlinestore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String showOrderForm(HttpSession session, Model model) {
        return "order-form";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam String customerName,
                              @RequestParam String address,
                              @RequestParam String phone,
                              HttpSession session,
                              Model model) {
        try{
            Order order = orderService.createOrder(customerName, address, phone, session);
            model.addAttribute("order", order);
            return "order-confirmation";
        }
        catch(Exception e){
            model.addAttribute("error","Не удалось оформить заказ: " + e.getMessage());
            return "order-form";
        }
    }
}
