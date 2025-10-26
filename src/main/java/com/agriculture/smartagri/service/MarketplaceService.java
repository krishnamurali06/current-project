package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarketplaceService {

    @Autowired
    private SaleRepository saleRepository;

    // Create a new sale listing
    public SaleListing createSaleListing(SaleListing saleListing) {
        return saleRepository.save(saleListing);
    }

    // Get all active sale listings
    public List<SaleListing> getAllActiveListings() {
        return saleRepository.findActiveListings();
    }

    // Get sale listings by farmer
    public List<SaleListing> getListingsByFarmer(User farmer) {
        return saleRepository.findByFarmer(farmer);
    }

    // Get active sale listings by farmer
    public List<SaleListing> getActiveListingsByFarmer(User farmer) {
        return saleRepository.findByFarmerAndStatus(farmer, "ACTIVE");
    }

    // Get sale listings by category
    public List<SaleListing> getListingsByCategory(String category) {
        return saleRepository.findActiveByCategory(category);
    }

    // Get sale listings by price range
    public List<SaleListing> getListingsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return saleRepository.findActiveByPriceRange(minPrice, maxPrice);
    }

    // Search sale listings by product name
    public List<SaleListing> searchListingsByProductName(String productName) {
        return saleRepository.findActiveByProductNameContaining(productName);
    }

    // Get sale listings by farmer location
    public List<SaleListing> getListingsByFarmerLocation(String location) {
        return saleRepository.findActiveByFarmerLocation(location);
    }

    // Get sale listing by ID
    public Optional<SaleListing> getSaleListingById(Long id) {
        return saleRepository.findById(id);
    }

    // Update sale listing
    public SaleListing updateSaleListing(SaleListing saleListing) {
        return saleRepository.save(saleListing);
    }

    // Delete sale listing
    public void deleteSaleListing(Long id) {
        saleRepository.deleteById(id);
    }

    // Update sale listing status
    public SaleListing updateListingStatus(Long id, String status) {
        Optional<SaleListing> listingOpt = saleRepository.findById(id);
        if (listingOpt.isPresent()) {
            SaleListing listing = listingOpt.get();
            listing.setStatus(status);
            return saleRepository.save(listing);
        }
        throw new RuntimeException("Sale listing not found");
    }

    // Update quantity available
    public SaleListing updateQuantityAvailable(Long id, Double newQuantity) {
        Optional<SaleListing> listingOpt = saleRepository.findById(id);
        if (listingOpt.isPresent()) {
            SaleListing listing = listingOpt.get();
            listing.setQuantityAvailable(newQuantity);
            if (newQuantity <= 0) {
                listing.setStatus("SOLD_OUT");
            }
            return saleRepository.save(listing);
        }
        throw new RuntimeException("Sale listing not found");
    }

    // Count listings by farmer
    public long countListingsByFarmer(User farmer) {
        return saleRepository.countByFarmer(farmer);
    }

    // Count active listings
    public long countActiveListings() {
        return saleRepository.countActiveListings();
    }

    // Get all sale listings (including inactive)
    public List<SaleListing> getAllListings() {
        return saleRepository.findAll();
    }

    // Get sale listings by status
    public List<SaleListing> getListingsByStatus(String status) {
        return saleRepository.findByStatus(status);
    }
}
