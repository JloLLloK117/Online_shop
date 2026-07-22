package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Order;
import com.example.onlinestore.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {

    private final OrderRepository orderRepository;

    public OrderAdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String listOrders(Model model){
        model.addAttribute("orders", orderRepository.findAllByOrderByOrderDateDesc());
        return "admin/orders";
    }
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Order.Status status){
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/admin/orders";
    }
}
