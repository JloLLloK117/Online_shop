package com.example.onlinestore.service;

import com.example.onlinestore.entity.Order;
import com.example.onlinestore.model.OrderItem;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(String customerName, String phone, String address, HttpSession session){
        List<CartItem> cartItems = cartService.getCartItems(session);
        if(cartItems.isEmpty()){
            throw new RuntimeException("Корзина пуста");
        }
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setTotalAmount(cartService.getTotalAmount(session));

        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setProductName(cartItem.getName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setProductImageUrl(cartItem.getImageUrl());
            order.getItems().add(item);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.getCartItems(session).clear();

        return savedOrder;
    }

}
