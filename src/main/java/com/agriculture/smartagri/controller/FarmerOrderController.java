package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/farmer/orders")
public class FarmerOrderController {

    @Autowired
    private OrderService orderService;

    // View all orders related to the currently logged-in farmer (User)
    @GetMapping
    public String viewOrders(Model model, Authentication authentication) {
        List<Order> orders = orderService.getOrdersForFarmer(authentication);
        model.addAttribute("orders", orders);
        return "farmer/farmer-order-dashboard";
    }

    // Update the order status (e.g. Accepted/Rejected/Dispatched)
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/farmer/orders";
    }
}
