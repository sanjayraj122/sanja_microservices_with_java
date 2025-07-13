package com.restaurant_service.restaurant.controller;

import com.restaurant_service.restaurant.entity.Order;
import com.restaurant_service.restaurant.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable String orderId, @RequestBody Order order) {
        String result = orderService.updateOrder(orderId, order.getName(), order.getQty(), order.getPrice());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        String result = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<String> getOrderDetails(@PathVariable String orderId) {
        String result = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<String> getAllOrders() {
        String result = orderService.getAllOrders();
        return ResponseEntity.ok(result);
    }


}
