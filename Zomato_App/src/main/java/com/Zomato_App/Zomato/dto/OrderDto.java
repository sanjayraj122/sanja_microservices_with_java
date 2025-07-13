package com.Zomato_App.Zomato.dto;


import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String orderId;
    private String name;
    private int qty;
    private double price;
    private Date orderDate;
    private String status;
    private int estimateDeliveryWindow;
}

