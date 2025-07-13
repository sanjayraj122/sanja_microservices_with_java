package com.restaurant_service.restaurant.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;
    private String name;
    private int qty;
    private double price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private String status;
    private int estimateDeliveryWindow;

    @PrePersist
    public void generateOrderId() {
        if (this.orderId == null) {
            this.orderId = java.util.UUID.randomUUID().toString();
        }
    }


}
