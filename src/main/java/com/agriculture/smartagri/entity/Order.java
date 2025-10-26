package com.agriculture.smartagri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // Avoids conflict with SQL keyword "order"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Buyer name is required")
    private String buyerName;
    
    @NotBlank(message = "Contact information is required")
    private String contactInfo;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Double quantityOrdered;
    
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    private BigDecimal totalPrice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_listing_id", nullable = false)
    private SaleListing saleListing;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    
    private String status = "PENDING"; // PENDING, ACCEPTED, DECLINED, COMPLETED, CANCELLED
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Order() {}
    
    public Order(String buyerName, String contactInfo, Double quantityOrdered, 
                BigDecimal totalPrice, SaleListing saleListing, User buyer) {
        this.buyerName = buyerName;
        this.contactInfo = contactInfo;
        this.quantityOrdered = quantityOrdered;
        this.totalPrice = totalPrice;
        this.saleListing = saleListing;
        this.buyer = buyer;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public Double getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(Double quantityOrdered) { this.quantityOrdered = quantityOrdered; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public SaleListing getSaleListing() { return saleListing; }
    public void setSaleListing(SaleListing saleListing) { this.saleListing = saleListing; }
    
    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}