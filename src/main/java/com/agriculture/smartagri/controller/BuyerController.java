package com.agriculture.smartagri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyerController {

    @GetMapping("/buyer/dashboard")
    public String buyerDashboard(Model model) {
        model.addAttribute("title", "Buyer Dashboard");
        return "buyer/dashboard";
    }
}