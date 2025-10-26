package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Show signup form
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    // ✅ Handle signup submission
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user) {
        userService.registerUser(user); // ✅ Matches method name in UserService
        return "redirect:/auth/login?registered";
    }
}