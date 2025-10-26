package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<SaleListing, Long> {
    
    List<SaleListing> findByFarmer(User farmer);
    
    List<SaleListing> findByStatus(String status);
    
    List<SaleListing> findByCategory(String category);
    
    List<SaleListing> findByFarmerAndStatus(User farmer, String status);
    
    @Query("SELECT s FROM SaleListing s WHERE s.status = 'ACTIVE' ORDER BY s.createdAt DESC")
    List<SaleListing> findActiveListings();
    
    @Query("SELECT s FROM SaleListing s WHERE s.category = :category AND s.status = 'ACTIVE'")
    List<SaleListing> findActiveByCategory(@Param("category") String category);
    
    @Query("SELECT s FROM SaleListing s WHERE s.pricePerKg BETWEEN :minPrice AND :maxPrice AND s.status = 'ACTIVE'")
    List<SaleListing> findActiveByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT s FROM SaleListing s WHERE s.productName LIKE %:productName% AND s.status = 'ACTIVE'")
    List<SaleListing> findActiveByProductNameContaining(@Param("productName") String productName);
    
    @Query("SELECT s FROM SaleListing s WHERE s.farmer.location LIKE %:location% AND s.status = 'ACTIVE'")
    List<SaleListing> findActiveByFarmerLocation(@Param("location") String location);
    
    @Query("SELECT COUNT(s) FROM SaleListing s WHERE s.farmer = :farmer")
    long countByFarmer(@Param("farmer") User farmer);
    
    @Query("SELECT COUNT(s) FROM SaleListing s WHERE s.status = 'ACTIVE'")
    long countActiveListings();
}
