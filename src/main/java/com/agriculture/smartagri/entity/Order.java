package com.agriculture.smartagri.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders") // Avoids conflict with SQL keyword "order"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerName;
    private String contactInfo;
    private Double quantityOrdered;

    @ManyToOne
    @JoinColumn(name = "produce_id")
    private Produce produce;

    private String status = "Pending";

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public Double getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(Double quantityOrdered) { this.quantityOrdered = quantityOrdered; }

    public Produce getProduce() { return produce; }
    public void setProduce(Produce produce) { this.produce = produce; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}