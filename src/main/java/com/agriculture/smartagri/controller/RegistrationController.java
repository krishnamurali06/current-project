package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Handle registration submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register"; // Return form if validation errors exist
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Email already registered");
            return "auth/register";
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Username already exists");
            return "auth/register";
        }

        userService.registerUser(user);
        return "redirect:/auth/login?registered"; // Redirect to login page with registration success message
    }
}
