package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.service.CropService;
import com.agriculture.smartagri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/farmer")
public class FarmerDashboardController {

    @Autowired
    private CropService cropService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String farmerDashboard(Model model, Authentication authentication) {
        model.addAttribute("title", "Farmer Dashboard");
        model.addAttribute("username", authentication.getName());
        return "farmer/dashboard";
    }

    @GetMapping("/view-crops")
    public String viewCrops(Model model, Authentication authentication) {
        String username = authentication.getName();

        Long userId = userService.findByUsername(username)
                .map(user -> user.getId())
                .orElse(null);

        if (userId == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        model.addAttribute("title", "My Crops");
        model.addAttribute("crops", cropService.getCropsByFarmer(userId));
        model.addAttribute("username", username); // ✅ Add username to avoid NPE

        return "farmer/view-crops";
    }
}
