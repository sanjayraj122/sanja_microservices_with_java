package com.Zomato_App.Zomato.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    private String name;
    private int qty;
    private double price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private String status;
    private int estimateDeliveryWindow; // in minutes

    @PrePersist
    public void generateOrderId() {
        if (this.orderId == null) {
            this.orderId = java.util.UUID.randomUUID().toString();
        }
    }
}

