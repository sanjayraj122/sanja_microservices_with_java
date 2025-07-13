package com.restaurant_service.restaurant.repository;

import com.restaurant_service.restaurant.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

}
