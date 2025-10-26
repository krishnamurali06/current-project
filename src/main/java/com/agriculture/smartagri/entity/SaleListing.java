package com.agriculture.smartagri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sale_listings")
public class SaleListing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotNull(message = "Price per kg is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerKg;
    
    @NotNull(message = "Quantity available is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Double quantityAvailable;
    
    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;
    
    private String description;
    private String imageUrl;
    private String status = "ACTIVE"; // ACTIVE, SOLD_OUT, INACTIVE
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;
    
    @OneToMany(mappedBy = "saleListing", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Order> orders;
    
    // Constructors
    public SaleListing() {}
    
    public SaleListing(String productName, String category, BigDecimal pricePerKg, 
                      Double quantityAvailable, LocalDate harvestDate, User farmer) {
        this.productName = productName;
        this.category = category;
        this.pricePerKg = pricePerKg;
        this.quantityAvailable = quantityAvailable;
        this.harvestDate = harvestDate;
        this.farmer = farmer;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public BigDecimal getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(BigDecimal pricePerKg) { this.pricePerKg = pricePerKg; }
    
    public Double getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(Double quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    
    public LocalDate getHarvestDate() { return harvestDate; }
    public void setHarvestDate(LocalDate harvestDate) { this.harvestDate = harvestDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }
    
    public java.util.List<Order> getOrders() { return orders; }
    public void setOrders(java.util.List<Order> orders) { this.orders = orders; }
}
