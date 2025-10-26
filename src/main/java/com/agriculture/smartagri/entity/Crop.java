package com.agriculture.smartagri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Crop name is required")
    private String name;
    
    @NotBlank(message = "Crop type is required")
    private String type;
    
    @NotNull(message = "Planting date is required")
    private LocalDate plantingDate;
    
    private LocalDate expectedHarvestDate;
    
    @DecimalMin(value = "0.0", message = "Expected yield must be non-negative")
    private BigDecimal expectedYield;
    
    private String healthStatus = "HEALTHY"; // HEALTHY, DISEASED, PEST_INFESTED, DROUGHT_AFFECTED
    private String soilType;
    private String irrigationMethod;
    private String fertilizerUsed;
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    // Constructors
    public Crop() {}
    
    public Crop(String name, String type, LocalDate plantingDate, User farmer) {
        this.name = name;
        this.type = type;
        this.plantingDate = plantingDate;
        this.farmer = farmer;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getPlantingDate() { return plantingDate; }
    public void setPlantingDate(LocalDate plantingDate) { this.plantingDate = plantingDate; }
    
    public LocalDate getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }

    public BigDecimal getExpectedYield() { return expectedYield; }
    public void setExpectedYield(BigDecimal expectedYield) { this.expectedYield = expectedYield; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    
    public String getSoilType() { return soilType; }
    public void setSoilType(String soilType) { this.soilType = soilType; }
    
    public String getIrrigationMethod() { return irrigationMethod; }
    public void setIrrigationMethod(String irrigationMethod) { this.irrigationMethod = irrigationMethod; }
    
    public String getFertilizerUsed() { return fertilizerUsed; }
    public void setFertilizerUsed(String fertilizerUsed) { this.fertilizerUsed = fertilizerUsed; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }
}
