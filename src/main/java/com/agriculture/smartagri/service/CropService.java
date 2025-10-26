package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    // Create a new crop
    public Crop createCrop(Crop crop) {
        return cropRepository.save(crop);
    }

    // Get crop by ID
    public Optional<Crop> getCropById(Long id) {
        return cropRepository.findById(id);
    }

    // Get all crops
    public List<Crop> getAllCrops() {
        return cropRepository.findAllOrderByPlantingDateDesc();
    }

    // Get crops by farmer
    public List<Crop> getCropsByFarmer(User farmer) {
        return cropRepository.findByFarmer(farmer);
    }

    // Get crops by farmer ID
    public List<Crop> getCropsByFarmerId(Long userId) {
        return cropRepository.findByFarmerId(userId);
    }

    // Get crops by type
    public List<Crop> getCropsByType(String type) {
        return cropRepository.findByType(type);
    }

    // Get crops by health status
    public List<Crop> getCropsByHealthStatus(String healthStatus) {
        return cropRepository.findByHealthStatus(healthStatus);
    }

    // Get crops by farmer and type
    public List<Crop> getCropsByFarmerAndType(User farmer, String type) {
        return cropRepository.findByFarmerAndType(farmer, type);
    }

    // Get crops planted between dates
    public List<Crop> getCropsByPlantingDateRange(LocalDate startDate, LocalDate endDate) {
        return cropRepository.findByPlantingDateBetween(startDate, endDate);
    }

    // Get crops with harvest date between dates
    public List<Crop> getCropsByHarvestDateRange(LocalDate startDate, LocalDate endDate) {
        return cropRepository.findByExpectedHarvestDateBetween(startDate, endDate);
    }

    // Search crops by name
    public List<Crop> searchCropsByName(String cropName) {
        return cropRepository.findByNameContaining(cropName);
    }

    // Update crop
    public Crop updateCrop(Crop crop) {
        return cropRepository.save(crop);
    }

    // Delete crop
    public void deleteCrop(Long id) {
        cropRepository.deleteById(id);
    }

    // Update crop health status
    public Crop updateCropHealthStatus(Long id, String healthStatus) {
        Optional<Crop> cropOpt = cropRepository.findById(id);
        if (cropOpt.isPresent()) {
            Crop crop = cropOpt.get();
            crop.setHealthStatus(healthStatus);
            return cropRepository.save(crop);
        }
        throw new RuntimeException("Crop not found");
    }

    // Count crops by farmer
    public long countCropsByFarmer(User farmer) {
        return cropRepository.countByFarmer(farmer);
    }

    // Count crops by type
    public long countCropsByType(String type) {
        return cropRepository.countByType(type);
    }

    // Get crops needing attention (diseased or pest infested)
    public List<Crop> getCropsNeedingAttention() {
        return cropRepository.findByHealthStatus("DISEASED");
    }

    // Get crops ready for harvest
    public List<Crop> getCropsReadyForHarvest() {
        LocalDate today = LocalDate.now();
        return cropRepository.findByExpectedHarvestDateBetween(today.minusDays(7), today.plusDays(7));
    }
}
