package com.example.onlinestore.service;

import com.example.onlinestore.entity.Product;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService{

    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session){
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if(cart==null){
            cart = new ConcurrentHashMap<>();
            session.setAttribute("cart",cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        Map<Long, CartItem> cart = getCart(session);
        CartItem existingItem = cart.get(productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {

            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    1,
                    product.getImageUrl()
            );
            cart.put(productId, newItem);
        }
    }

    public void removeFromCart(HttpSession session,Long productId){
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
    }

    public List<CartItem> getCartItems(HttpSession session){
        return new ArrayList<>(getCart(session).values());
    }

    public double getTotalAmount(HttpSession session) {
        return getCartItems(session).stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
