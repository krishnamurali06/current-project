package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Save a new order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get all orders (for admin or testing)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Retrieve all orders for the currently logged-in farmer (from User entity)
    public List<Order> getOrdersForFarmer(Authentication authentication) {
        String username = authentication.getName(); // Logged-in username

        return orderRepository.findAll().stream()
                .filter(order -> order.getProduce() != null && order.getProduce().getFarmer() != null)
                .filter(order -> order.getProduce().getFarmer().getUsername().equals(username))
                .toList();
    }

    // Update order status (e.g., from Pending → Completed)
    public void updateOrderStatus(Long orderId, String status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }

    // Get all orders by buyer name
    public List<Order> getOrdersByBuyerName(String buyerName) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getBuyerName() != null && order.getBuyerName().equalsIgnoreCase(buyerName))
                .toList();
    }
}
