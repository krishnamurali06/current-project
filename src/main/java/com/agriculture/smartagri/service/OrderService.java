package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MarketplaceService marketplaceService;

    // Create a new order
    public Order createOrder(Order order) {
        // Validate quantity
        SaleListing saleListing = order.getSaleListing();
        if (order.getQuantityOrdered() > saleListing.getQuantityAvailable()) {
            throw new RuntimeException("Requested quantity exceeds available quantity");
        }
        
        // Calculate total price
        BigDecimal totalPrice = saleListing.getPricePerKg().multiply(BigDecimal.valueOf(order.getQuantityOrdered()));
        order.setTotalPrice(totalPrice);
        
        return orderRepository.save(order);
    }

    // Get order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAllOrderByCreatedAtDesc();
    }

    // Get orders by buyer
    public List<Order> getOrdersByBuyer(User buyer) {
        return orderRepository.findByBuyer(buyer);
    }

    // Get orders by farmer
    public List<Order> getOrdersByFarmer(User farmer) {
        return orderRepository.findByFarmer(farmer);
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // Get orders by buyer and status
    public List<Order> getOrdersByBuyerAndStatus(User buyer, String status) {
        return orderRepository.findByBuyerAndStatus(buyer, status);
    }

    // Get orders by farmer and status
    public List<Order> getOrdersByFarmerAndStatus(User farmer, String status) {
        return orderRepository.findByFarmerAndStatus(farmer, status);
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            
            // If order is accepted, update the sale listing quantity
            if ("ACCEPTED".equals(status)) {
                SaleListing saleListing = order.getSaleListing();
                Double newQuantity = saleListing.getQuantityAvailable() - order.getQuantityOrdered();
                marketplaceService.updateQuantityAvailable(saleListing.getId(), newQuantity);
            }
            
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    // Accept order
    public Order acceptOrder(Long orderId) {
        return updateOrderStatus(orderId, "ACCEPTED");
    }

    // Decline order
    public Order declineOrder(Long orderId) {
        return updateOrderStatus(orderId, "DECLINED");
    }

    // Complete order
    public Order completeOrder(Long orderId) {
        return updateOrderStatus(orderId, "COMPLETED");
    }

    // Cancel order
    public Order cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }

    // Update order notes
    public Order updateOrderNotes(Long orderId, String notes) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setNotes(notes);
            order.setUpdatedAt(LocalDateTime.now());
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }

    // Delete order
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // Count orders by buyer
    public long countOrdersByBuyer(User buyer) {
        return orderRepository.countByBuyer(buyer);
    }

    // Count orders by farmer
    public long countOrdersByFarmer(User farmer) {
        return orderRepository.countByFarmer(farmer);
    }

    // Count orders by status
    public long countOrdersByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    // Get orders created between dates
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Get pending orders for farmer
    public List<Order> getPendingOrdersForFarmer(User farmer) {
        return orderRepository.findByFarmerAndStatus(farmer, "PENDING");
    }

    // Get accepted orders for buyer
    public List<Order> getAcceptedOrdersForBuyer(User buyer) {
        return orderRepository.findByBuyerAndStatus(buyer, "ACCEPTED");
    }
}
