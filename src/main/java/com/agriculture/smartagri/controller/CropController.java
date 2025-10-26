package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.CropService;
import com.agriculture.smartagri.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/crop")
public class CropController {

    private final CropService cropService;
    private final UserService userService;

    // Constructor injection
    public CropController(CropService cropService, UserService userService) {
        this.cropService = cropService;
        this.userService = userService;
    }

    // Show the crop form
    @GetMapping("/add")
    public String showCropForm(Model model) {
        model.addAttribute("title", "Add Crop");
        model.addAttribute("crop", new Crop());
        return "farmer/cropmanagement";
    }

    // Handle crop form submission
    @PostMapping("/add")
    @Transactional
    public String saveCrop(@ModelAttribute("crop") Crop crop,
                           Authentication authentication,
                           Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        // Fetch the logged-in user from DB
        User farmer = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        // Assign farmer and save crop
        crop.setFarmer(farmer);
        cropService.saveCrop(crop);

        // Reset form and show success message
        model.addAttribute("message", "Crop added successfully!");
        model.addAttribute("crop", new Crop());

        return "farmer/cropmanagement";
    }

    // View crops for the logged-in farmer
    @GetMapping("/my-crops")
    public String viewMyCrops(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User farmer = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        // Fetch crops for this farmer
        List<Crop> crops = cropService.getCropsByFarmer(farmer.getId());
        if (crops == null) {
            crops = new ArrayList<>();
        }

        model.addAttribute("title", "My Crops");
        model.addAttribute("crops", crops);
        model.addAttribute("username", farmer.getUsername());

        return "farmer/view-crops";
    }
}
