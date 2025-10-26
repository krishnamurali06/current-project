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

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Smart Agriculture Management System");
        return "home";
    }

    @GetMapping("/home")
    public String homePageWithAuth(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/auth/login?error";
        }

        model.addAttribute("user", user);
        model.addAttribute("title", "Smart Agriculture Management System");
        return "home";
    }
}