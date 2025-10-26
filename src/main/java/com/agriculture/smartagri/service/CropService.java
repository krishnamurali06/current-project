package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.Crop;
import com.agriculture.smartagri.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    // Fetch crops by userId (farmer)
    public List<Crop> getCropsByFarmer(Long userId) {
        return cropRepository.findByFarmerId(userId);
    }

    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }
}
