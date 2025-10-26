package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.CropService;
import com.agriculture.smartagri.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/crop")
public class CropController {

    @Autowired
    private CropService cropService;

    @Autowired
    private UserService userService;

    // Show the crop form
    @GetMapping("/add")
    public String showCropForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            model.addAttribute("title", "Add Crop");
            model.addAttribute("crop", new Crop());
            model.addAttribute("username", userOpt.get().getName());
            model.addAttribute("farmer", userOpt.get());
        }
        
        return "farmer/crop-management";
    }

    // Handle crop form submission
    @PostMapping("/add")
    @Transactional
    public String saveCrop(@ModelAttribute("crop") @Valid Crop crop,
                          BindingResult result,
                          Authentication authentication,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                model.addAttribute("username", userOpt.get().getName());
                model.addAttribute("farmer", userOpt.get());
            }
            return "farmer/crop-management";
        }

        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            crop.setFarmer(farmer);
            cropService.createCrop(crop);
            redirectAttributes.addFlashAttribute("success", "Crop added successfully!");
            return "redirect:/farmer/view-crops";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add crop: " + e.getMessage());
            return "redirect:/crop/add";
        }
    }

    // Edit crop form
    @GetMapping("/edit/{id}")
    public String editCropForm(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            Optional<Crop> cropOpt = cropService.getCropById(id);
            if (cropOpt.isPresent()) {
                Crop crop = cropOpt.get();
                // Check if the crop belongs to the logged-in farmer
                if (crop.getFarmer().getId().equals(userOpt.get().getId())) {
                    model.addAttribute("title", "Edit Crop");
                    model.addAttribute("crop", crop);
                    model.addAttribute("username", userOpt.get().getName());
                    model.addAttribute("farmer", userOpt.get());
                    return "farmer/crop-management";
                }
            }
        }
        
        return "redirect:/farmer/view-crops";
    }

    // Update crop
    @PostMapping("/edit/{id}")
    @Transactional
    public String updateCrop(@PathVariable Long id,
                           @ModelAttribute("crop") @Valid Crop crop,
                           BindingResult result,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "farmer/crop-management";
        }

        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<Crop> existingCropOpt = cropService.getCropById(id);
            if (existingCropOpt.isPresent()) {
                Crop existingCrop = existingCropOpt.get();
                // Check if the crop belongs to the logged-in farmer
                if (existingCrop.getFarmer().getId().equals(farmer.getId())) {
                    crop.setId(id);
                    crop.setFarmer(farmer);
                    cropService.updateCrop(crop);
                    redirectAttributes.addFlashAttribute("success", "Crop updated successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only edit your own crops!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Crop not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update crop: " + e.getMessage());
        }

        return "redirect:/farmer/view-crops";
    }

    // Delete crop
    @PostMapping("/delete/{id}")
    public String deleteCrop(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<Crop> cropOpt = cropService.getCropById(id);
            if (cropOpt.isPresent()) {
                Crop crop = cropOpt.get();
                // Check if the crop belongs to the logged-in farmer
                if (crop.getFarmer().getId().equals(farmer.getId())) {
                    cropService.deleteCrop(id);
                    redirectAttributes.addFlashAttribute("success", "Crop deleted successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only delete your own crops!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Crop not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete crop: " + e.getMessage());
        }

        return "redirect:/farmer/view-crops";
    }

    // Update crop health status
    @PostMapping("/update-health/{id}")
    public String updateCropHealth(@PathVariable Long id, 
                                 @RequestParam String healthStatus,
                                 Authentication authentication, 
                                 RedirectAttributes redirectAttributes) {
        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<Crop> cropOpt = cropService.getCropById(id);
            if (cropOpt.isPresent()) {
                Crop crop = cropOpt.get();
                // Check if the crop belongs to the logged-in farmer
                if (crop.getFarmer().getId().equals(farmer.getId())) {
                    cropService.updateCropHealthStatus(id, healthStatus);
                    redirectAttributes.addFlashAttribute("success", "Crop health status updated successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only update your own crops!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Crop not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update crop health: " + e.getMessage());
        }

        return "redirect:/farmer/view-crops";
    }
}
