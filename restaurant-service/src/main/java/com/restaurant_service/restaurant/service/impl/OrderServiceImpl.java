package com.restaurant_service.restaurant.service.impl;

import com.restaurant_service.restaurant.entity.Order;
import com.restaurant_service.restaurant.repository.OrderRepository;
import com.restaurant_service.restaurant.service.OrderService;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import com.restaurant_service.restaurant.dto.OrderResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository    orderRepository;


    @Override
    public Order placeOrder(Order order) {
        Order save = orderRepository.save(order);
        return save;
    }

    @Override
    public String cancelOrder(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            return "Order cancelled successfully.";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
    }

    @Override
    public String updateOrder(String orderId, String name, int qty, double price) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setName(name);
            order.setQty(qty);
            order.setPrice(price);
            orderRepository.save(order);
            return "Order updated successfully.";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
    }

    @Override
    public String getOrderDetails(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            response.setOrderDate(order.getOrderDate().toString());
            return response.toString();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
    }

    @Override
    public String getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responses = orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            response.setOrderDate(order.getOrderDate().toString());
            return response;
        }).collect(Collectors.toList());
        return responses.toString();
    }


}
