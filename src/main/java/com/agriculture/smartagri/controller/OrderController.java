package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.entity.Produce;
import com.agriculture.smartagri.service.OrderService;
import com.agriculture.smartagri.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProduceService produceService;

    @GetMapping("/place")
    public String showOrderForm(Model model) {
        model.addAttribute("produceList", produceService.getAllProduce());
        return "buyer/order-form";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String buyerName,
                              @RequestParam String contactInfo,
                              @RequestParam Double quantityOrdered,
                              @RequestParam Long produceId) {

        Produce selectedProduce = produceService.getAllProduce().stream()
            .filter(p -> p.getId().equals(produceId))
            .findFirst()
            .orElse(null);

        if (selectedProduce == null) {
            return "redirect:/order/place?error=invalid_produce";
        }

        Order order = new Order();
        order.setBuyerName(buyerName);
        order.setContactInfo(contactInfo);
        order.setQuantityOrdered(quantityOrdered);
        order.setProduce(selectedProduce);
        order.setStatus("Pending");

        orderService.saveOrder(order);
        return "redirect:/order/place?success=true";
    }
}