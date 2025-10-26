package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.service.MarketplaceService;
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
@RequestMapping("/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private UserService userService;

    // Show add listing form for farmers
    @GetMapping("/add")
    public String showAddListingForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            model.addAttribute("title", "Add Marketplace Listing");
            model.addAttribute("listing", new SaleListing());
            model.addAttribute("username", userOpt.get().getName());
            model.addAttribute("farmer", userOpt.get());
        }
        
        return "farmer/marketplace";
    }

    // Handle add listing submission
    @PostMapping("/add")
    public String addListing(@ModelAttribute("listing") @Valid SaleListing listing,
                           BindingResult result,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("username", userOpt.get().getName());
                redirectAttributes.addFlashAttribute("farmer", userOpt.get());
            }
            return "farmer/marketplace";
        }

        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            listing.setFarmer(farmer);
            listing.setStatus("ACTIVE");
            marketplaceService.createSaleListing(listing);
            redirectAttributes.addFlashAttribute("success", "Listing added successfully!");
            return "redirect:/farmer/marketplace";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add listing: " + e.getMessage());
            return "redirect:/marketplace/add";
        }
    }

    // View all marketplace listings
    @GetMapping("/list")
    public String viewMarketplace(Model model,
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
        
        return "shared/marketplace-list";
    }

    // Edit listing form
    @GetMapping("/edit/{id}")
    public String editListingForm(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            Optional<SaleListing> listingOpt = marketplaceService.getSaleListingById(id);
            if (listingOpt.isPresent()) {
                SaleListing listing = listingOpt.get();
                // Check if the listing belongs to the logged-in farmer
                if (listing.getFarmer().getId().equals(userOpt.get().getId())) {
                    model.addAttribute("title", "Edit Listing");
                    model.addAttribute("listing", listing);
                    model.addAttribute("username", userOpt.get().getName());
                    model.addAttribute("farmer", userOpt.get());
                    return "farmer/marketplace";
                }
            }
        }
        
        return "redirect:/farmer/marketplace";
    }

    // Update listing
    @PostMapping("/edit/{id}")
    public String updateListing(@PathVariable Long id,
                              @ModelAttribute("listing") @Valid SaleListing listing,
                              BindingResult result,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "farmer/marketplace";
        }

        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<SaleListing> existingListingOpt = marketplaceService.getSaleListingById(id);
            if (existingListingOpt.isPresent()) {
                SaleListing existingListing = existingListingOpt.get();
                // Check if the listing belongs to the logged-in farmer
                if (existingListing.getFarmer().getId().equals(farmer.getId())) {
                    listing.setId(id);
                    listing.setFarmer(farmer);
                    marketplaceService.updateSaleListing(listing);
                    redirectAttributes.addFlashAttribute("success", "Listing updated successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only edit your own listings!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Listing not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update listing: " + e.getMessage());
        }

        return "redirect:/farmer/marketplace";
    }

    // Delete listing
    @PostMapping("/delete/{id}")
    public String deleteListing(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<SaleListing> listingOpt = marketplaceService.getSaleListingById(id);
            if (listingOpt.isPresent()) {
                SaleListing listing = listingOpt.get();
                // Check if the listing belongs to the logged-in farmer
                if (listing.getFarmer().getId().equals(farmer.getId())) {
                    marketplaceService.deleteSaleListing(id);
                    redirectAttributes.addFlashAttribute("success", "Listing deleted successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only delete your own listings!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Listing not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete listing: " + e.getMessage());
        }

        return "redirect:/farmer/marketplace";
    }

    // Update listing status
    @PostMapping("/update-status/{id}")
    public String updateListingStatus(@PathVariable Long id, 
                                    @RequestParam String status,
                                    Authentication authentication, 
                                    RedirectAttributes redirectAttributes) {
        try {
            User farmer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            Optional<SaleListing> listingOpt = marketplaceService.getSaleListingById(id);
            if (listingOpt.isPresent()) {
                SaleListing listing = listingOpt.get();
                // Check if the listing belongs to the logged-in farmer
                if (listing.getFarmer().getId().equals(farmer.getId())) {
                    marketplaceService.updateListingStatus(id, status);
                    redirectAttributes.addFlashAttribute("success", "Listing status updated successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only update your own listings!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Listing not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update listing status: " + e.getMessage());
        }

        return "redirect:/farmer/marketplace";
    }
}
