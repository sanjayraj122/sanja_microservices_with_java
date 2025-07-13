package com.restaurant_service.restaurant.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private String orderId;
    private String name;
    private int qty;
    private double price;
    private String orderDate;
    private String status;
    private int estimateDeliveryWindow; // in minutes
}
