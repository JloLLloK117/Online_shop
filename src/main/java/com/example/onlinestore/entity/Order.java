package com.example.onlinestore.entity;

import com.example.onlinestore.model.OrderItem;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    public enum Status {
        NEW("Новый"),
        PROCESSING("В обработке"),
        SHIPPED("Отправлен"),
        DELIVERED("Доставлен"),
        CANCELLED("Отменён");
        private final String displayName;
        Status(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;

    @PrePersist
    public void onCreate() {
        orderDate = LocalDateTime.now();
    }

}
