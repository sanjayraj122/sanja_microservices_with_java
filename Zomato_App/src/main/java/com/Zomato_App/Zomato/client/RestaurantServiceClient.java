package com.Zomato_App.Zomato.client;


import com.Zomato_App.Zomato.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "RESTAURANT-SERVICE",path = "/orders")
public interface RestaurantServiceClient {

    @PostMapping("/placeOrder")
    OrderDto createOrder(@RequestBody OrderDto orderDto);

    @GetMapping("/getAllOrders")
    List<OrderDto> getAllOrders();

    @GetMapping("/{id}")
    OrderDto getOrderById(@PathVariable("id") String id);

    @PutMapping("/{id}")
    OrderDto updateOrder(@PathVariable("id") String id, @RequestBody OrderDto orderDto);

    @DeleteMapping("/{id}")
    void deleteOrder(@PathVariable("id") String id);

}
