package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> findByFarmer(User farmer);
    
    List<Crop> findByFarmerId(Long userId);
    
    List<Crop> findByType(String type);
    
    List<Crop> findByHealthStatus(String healthStatus);
    
    List<Crop> findByFarmerAndType(User farmer, String type);
    
    @Query("SELECT c FROM Crop c WHERE c.plantingDate BETWEEN :startDate AND :endDate")
    List<Crop> findByPlantingDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM Crop c WHERE c.expectedHarvestDate BETWEEN :startDate AND :endDate")
    List<Crop> findByExpectedHarvestDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM Crop c WHERE c.name LIKE %:cropName%")
    List<Crop> findByNameContaining(@Param("cropName") String cropName);
    
    @Query("SELECT COUNT(c) FROM Crop c WHERE c.farmer = :farmer")
    long countByFarmer(@Param("farmer") User farmer);
    
    @Query("SELECT COUNT(c) FROM Crop c WHERE c.type = :type")
    long countByType(@Param("type") String type);
    
    @Query("SELECT c FROM Crop c ORDER BY c.plantingDate DESC")
    List<Crop> findAllOrderByPlantingDateDesc();
}
