package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buyer/orders")
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String viewBuyerOrders(@RequestParam(required = false) String buyerName, Model model) {
        if (buyerName == null || buyerName.isBlank()) {
            return "buyer/buyer-order-tracking"; // Show form only
        }

        List<Order> orders = orderService.getOrdersByBuyerName(buyerName);
        model.addAttribute("orders", orders);
        model.addAttribute("buyerName", buyerName);
        return "buyer/buyer-order-tracking";
    }
}