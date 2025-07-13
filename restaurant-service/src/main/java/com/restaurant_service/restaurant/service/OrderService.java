package com.restaurant_service.restaurant.service;

import com.restaurant_service.restaurant.entity.Order;

public interface OrderService {
    Order placeOrder(Order order);

    String cancelOrder(String orderId);

    String updateOrder(String orderId, String name, int qty, double price);

    String getOrderDetails(String orderId);

    String getAllOrders();
}
