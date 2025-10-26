package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {

    // Finds all crops linked to a user's ID (farmer_id)
    List<Crop> findByFarmerId(Long userId);
}
