package com.example.onlinestore.model;

import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;

    public CartItem() {}

    public CartItem(Long productId, String name, String description,
                    double price, int quantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
