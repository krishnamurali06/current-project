package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No authenticated user found.");
            return "redirect:/auth/login";
        }

        String username = authentication.getName();
        System.out.println("✅ Logged in user: " + username);

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            System.out.println("❌ User not found in database: " + username);
            return "redirect:/auth/login?error";
        }

        System.out.println("🎯 Role: " + user.getRole());
        model.addAttribute("user", user);

        return "home"; // Must match home.html in templates
    }
}