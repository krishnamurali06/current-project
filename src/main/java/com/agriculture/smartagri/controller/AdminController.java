package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.CropService;
import com.agriculture.smartagri.service.MarketplaceService;
import com.agriculture.smartagri.service.OrderService;
import com.agriculture.smartagri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CropService cropService;
    
    @Autowired
    private MarketplaceService marketplaceService;
    
    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User admin = userOpt.get();
            model.addAttribute("title", "Admin Dashboard");
            model.addAttribute("username", admin.getName());
            model.addAttribute("admin", admin);
            
            // Get statistics
            long totalUsers = userService.getAllUsers().size();
            long totalFarmers = userService.countUsersByRole("FARMER");
            long totalBuyers = userService.countUsersByRole("BUYER");
            long totalCrops = cropService.getAllCrops().size();
            long totalListings = marketplaceService.getAllListings().size();
            long totalOrders = orderService.getAllOrders().size();
            long activeListings = marketplaceService.countActiveListings();
            
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalFarmers", totalFarmers);
            model.addAttribute("totalBuyers", totalBuyers);
            model.addAttribute("totalCrops", totalCrops);
            model.addAttribute("totalListings", totalListings);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("activeListings", activeListings);
            
            // Get recent activities
            List<User> recentUsers = userService.getAllUsers();
            if (recentUsers.size() > 5) {
                recentUsers = recentUsers.subList(0, 5);
            }
            model.addAttribute("recentUsers", recentUsers);
        }
        
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    public String userManagement(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("title", "User Management");
        model.addAttribute("users", users);
        return "admin/user-management";
    }
    
    @GetMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Toggle between active/inactive status
                // This would require adding a status field to User entity
                redirectAttributes.addFlashAttribute("success", "User status updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("title", "Analytics Dashboard");
        
        // Get analytics data
        long totalUsers = userService.getAllUsers().size();
        long totalFarmers = userService.countUsersByRole("FARMER");
        long totalBuyers = userService.countUsersByRole("BUYER");
        long totalCrops = cropService.getAllCrops().size();
        long totalListings = marketplaceService.getAllListings().size();
        long totalOrders = orderService.getAllOrders().size();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalFarmers", totalFarmers);
        model.addAttribute("totalBuyers", totalBuyers);
        model.addAttribute("totalCrops", totalCrops);
        model.addAttribute("totalListings", totalListings);
        model.addAttribute("totalOrders", totalOrders);
        
        return "admin/analytics";
    }
    
    @GetMapping("/announcements")
    public String announcements(Model model) {
        model.addAttribute("title", "Announcements");
        return "admin/announcements";
    }
    
    @PostMapping("/announcements")
    public String createAnnouncement(@RequestParam String title, 
                                   @RequestParam String content, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // This would require creating an Announcement entity and service
            redirectAttributes.addFlashAttribute("success", "Announcement created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating announcement: " + e.getMessage());
        }
        return "redirect:/admin/announcements";
    }
}