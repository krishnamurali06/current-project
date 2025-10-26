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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    // Show registration form
    @GetMapping("/auth/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    // Handle registration submission
    @PostMapping("/auth/signup")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/signup";
        }

        try {
            userService.registerUser(user);
            return "redirect:/auth/login?registered";
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Username")) {
                model.addAttribute("usernameError", e.getMessage());
            } else if (e.getMessage().contains("Email")) {
                model.addAttribute("emailError", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "auth/signup";
        }
    }

    // Show farmer registration form
    @GetMapping("/auth/signup/farmer")
    public String showFarmerRegistrationForm(Model model) {
        User user = new User();
        user.setRole("FARMER");
        model.addAttribute("user", user);
        model.addAttribute("isFarmer", true);
        return "auth/signup";
    }

    // Show buyer registration form
    @GetMapping("/auth/signup/buyer")
    public String showBuyerRegistrationForm(Model model) {
        User user = new User();
        user.setRole("BUYER");
        model.addAttribute("user", user);
        model.addAttribute("isBuyer", true);
        return "auth/signup";
    }

    // Handle farmer registration
    @PostMapping("/auth/signup/farmer")
    public String registerFarmer(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isFarmer", true);
            return "auth/signup";
        }

        user.setRole("FARMER");
        try {
            userService.registerUser(user);
            return "redirect:/auth/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("isFarmer", true);
            if (e.getMessage().contains("Username")) {
                model.addAttribute("usernameError", e.getMessage());
            } else if (e.getMessage().contains("Email")) {
                model.addAttribute("emailError", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "auth/signup";
        }
    }

    // Handle buyer registration
    @PostMapping("/auth/signup/buyer")
    public String registerBuyer(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isBuyer", true);
            return "auth/signup";
        }

        user.setRole("BUYER");
        try {
            userService.registerUser(user);
            return "redirect:/auth/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("isBuyer", true);
            if (e.getMessage().contains("Username")) {
                model.addAttribute("usernameError", e.getMessage());
            } else if (e.getMessage().contains("Email")) {
                model.addAttribute("emailError", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "auth/signup";
        }
    }
}
