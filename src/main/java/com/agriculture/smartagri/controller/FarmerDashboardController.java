package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.CropService;
import com.agriculture.smartagri.service.MarketplaceService;
import com.agriculture.smartagri.service.OrderService;
import com.agriculture.smartagri.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/farmer")
public class FarmerDashboardController {

    @Autowired
    private CropService cropService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String farmerDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User farmer = userOpt.get();
            model.addAttribute("title", "Farmer Dashboard");
            model.addAttribute("username", farmer.getName());
            model.addAttribute("farmer", farmer);
            
            // Get farmer's statistics
            long cropCount = cropService.countCropsByFarmer(farmer);
            long listingCount = marketplaceService.countListingsByFarmer(farmer);
            long orderCount = orderService.countOrdersByFarmer(farmer);
            long pendingOrderCount = orderService.getPendingOrdersForFarmer(farmer).size();
            
            model.addAttribute("cropCount", cropCount);
            model.addAttribute("listingCount", listingCount);
            model.addAttribute("orderCount", orderCount);
            model.addAttribute("pendingOrderCount", pendingOrderCount);
            
            // Get recent crops
            List<Crop> recentCrops = cropService.getCropsByFarmer(farmer);
            if (recentCrops.size() > 5) {
                recentCrops = recentCrops.subList(0, 5);
            }
            model.addAttribute("recentCrops", recentCrops);
            
            // Get recent orders
            List<com.agriculture.smartagri.entity.Order> recentOrders = orderService.getOrdersByFarmer(farmer);
            if (recentOrders.size() > 5) {
                recentOrders = recentOrders.subList(0, 5);
            }
            model.addAttribute("recentOrders", recentOrders);
        }
        
        return "farmer/dashboard";
    }

    @GetMapping("/view-crops")
    public String viewCrops(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User farmer = userOpt.get();
            model.addAttribute("title", "My Crops");
            model.addAttribute("crops", cropService.getCropsByFarmer(farmer));
            model.addAttribute("username", farmer.getName());
            model.addAttribute("farmer", farmer);
        }
        
        return "farmer/view-crops";
    }

    @GetMapping("/marketplace")
    public String farmerMarketplace(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User farmer = userOpt.get();
            model.addAttribute("title", "My Marketplace Listings");
            model.addAttribute("listings", marketplaceService.getListingsByFarmer(farmer));
            model.addAttribute("username", farmer.getName());
            model.addAttribute("farmer", farmer);
        }
        
        return "farmer/marketplace";
    }

    @GetMapping("/orders")
    public String farmerOrders(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User farmer = userOpt.get();
            model.addAttribute("title", "My Orders");
            model.addAttribute("orders", orderService.getOrdersByFarmer(farmer));
            model.addAttribute("username", farmer.getName());
            model.addAttribute("farmer", farmer);
        }
        
        return "farmer/farmer-order-dashboard";
    }

    @PostMapping("/orders/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            orderService.acceptOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Order accepted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to accept order: " + e.getMessage());
        }
        return "redirect:/farmer/orders";
    }

    @PostMapping("/orders/{orderId}/decline")
    public String declineOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            orderService.declineOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Order declined successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to decline order: " + e.getMessage());
        }
        return "redirect:/farmer/orders";
    }

    @GetMapping("/profile")
    public String farmerProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User farmer = userOpt.get();
            model.addAttribute("title", "My Profile");
            model.addAttribute("farmer", farmer);
            model.addAttribute("username", farmer.getName());
        }
        
        return "farmer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute @Valid User farmer, BindingResult result, 
                              Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "farmer/profile";
        }
        
        try {
            userService.updateUser(farmer);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        
        return "redirect:/farmer/profile";
    }
}
