package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String buyerDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User buyer = userOpt.get();
            model.addAttribute("title", "Buyer Dashboard");
            model.addAttribute("username", buyer.getName());
            model.addAttribute("buyer", buyer);
            
            // Get buyer's statistics
            long orderCount = orderService.countOrdersByBuyer(buyer);
            long acceptedOrderCount = orderService.getAcceptedOrdersForBuyer(buyer).size();
            
            model.addAttribute("orderCount", orderCount);
            model.addAttribute("acceptedOrderCount", acceptedOrderCount);
            
            // Get recent orders
            List<Order> recentOrders = orderService.getOrdersByBuyer(buyer);
            if (recentOrders.size() > 5) {
                recentOrders = recentOrders.subList(0, 5);
            }
            model.addAttribute("recentOrders", recentOrders);
        }
        
        return "buyer/dashboard";
    }

    @GetMapping("/marketplace")
    public String buyerMarketplace(Model model, 
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) BigDecimal minPrice,
                                 @RequestParam(required = false) BigDecimal maxPrice) {
        List<SaleListing> listings;
        
        if (category != null && !category.isEmpty()) {
            listings = marketplaceService.getListingsByCategory(category);
        } else if (search != null && !search.isEmpty()) {
            listings = marketplaceService.searchListingsByProductName(search);
        } else if (minPrice != null && maxPrice != null) {
            listings = marketplaceService.getListingsByPriceRange(minPrice, maxPrice);
        } else {
            listings = marketplaceService.getAllActiveListings();
        }
        
        model.addAttribute("title", "Marketplace");
        model.addAttribute("listings", listings);
        model.addAttribute("category", category);
        model.addAttribute("search", search);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        return "buyer/marketplace-view";
    }

    @GetMapping("/order/{listingId}")
    public String showOrderForm(@PathVariable Long listingId, Model model, Authentication authentication) {
        Optional<SaleListing> listingOpt = marketplaceService.getSaleListingById(listingId);
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (listingOpt.isPresent() && userOpt.isPresent()) {
            SaleListing listing = listingOpt.get();
            User buyer = userOpt.get();
            
            Order order = new Order();
            order.setBuyerName(buyer.getName());
            order.setContactInfo(buyer.getEmail());
            order.setSaleListing(listing);
            order.setBuyer(buyer);
            
            model.addAttribute("title", "Place Order");
            model.addAttribute("order", order);
            model.addAttribute("listing", listing);
            model.addAttribute("buyer", buyer);
            model.addAttribute("username", buyer.getName());
        }
        
        return "buyer/order-form";
    }

    @PostMapping("/order")
    public String placeOrder(@ModelAttribute @Valid Order order, BindingResult result, 
                           Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "buyer/order-form";
        }
        
        try {
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (userOpt.isPresent()) {
                order.setBuyer(userOpt.get());
                orderService.createOrder(order);
                redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
                return "redirect:/buyer/orders";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to place order: " + e.getMessage());
        }
        
        return "redirect:/buyer/marketplace";
    }

    @GetMapping("/orders")
    public String buyerOrders(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User buyer = userOpt.get();
            model.addAttribute("title", "My Orders");
            model.addAttribute("orders", orderService.getOrdersByBuyer(buyer));
            model.addAttribute("username", buyer.getName());
            model.addAttribute("buyer", buyer);
        }
        
        return "buyer/buyer-order-tracking";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Order cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel order: " + e.getMessage());
        }
        return "redirect:/buyer/orders";
    }

    @GetMapping("/profile")
    public String buyerProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User buyer = userOpt.get();
            model.addAttribute("title", "My Profile");
            model.addAttribute("buyer", buyer);
            model.addAttribute("username", buyer.getName());
        }
        
        return "buyer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute @Valid User buyer, BindingResult result, 
                              Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "buyer/profile";
        }
        
        try {
            userService.updateUser(buyer);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        
        return "redirect:/buyer/profile";
    }
}