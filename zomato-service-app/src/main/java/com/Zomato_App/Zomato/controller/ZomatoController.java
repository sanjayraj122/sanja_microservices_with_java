package com.Zomato_App.Zomato.controller;

import com.Zomato_App.Zomato.client.RestaurantServiceClient;
import com.Zomato_App.Zomato.dto.OrderDto;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zomato/orders")
public class ZomatoController {

    @Autowired
    private RestaurantServiceClient restaurantServiceClient;

    @PostMapping("/placeOrder")
    public OrderDto placeOrder(@RequestBody OrderDto orderDto) {
        return restaurantServiceClient.createOrder(orderDto);
    }

    @GetMapping
    public List<OrderDto> fetchAllOrders() {
        return restaurantServiceClient.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDto fetchOrder(@PathVariable String id) {
        return restaurantServiceClient.getOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderDto modifyOrder(@PathVariable String id, @RequestBody OrderDto orderDto) {
        return restaurantServiceClient.updateOrder(id, orderDto);
    }

    @DeleteMapping("/{id}")
    public String cancelOrder(@PathVariable String id) {
        restaurantServiceClient.deleteOrder(id);
        return "Order cancelled successfully.";
    }
}

