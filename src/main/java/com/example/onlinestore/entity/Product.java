package com.example.onlinestore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Column(nullable = false,length = 200)
    @NotBlank private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    @Column(nullable = false)
    @NotNull @Positive private double price;

    @NotBlank(message = "категория обязательна")
    @Column(nullable = false,length = 100)
    @NotBlank private String category;

    @Column(nullable = true)
    private String imageUrl = "/images/default.jpg";

    public Product(String name, String description, double price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
