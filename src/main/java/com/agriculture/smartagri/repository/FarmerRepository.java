package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Farmer findByEmail(String email);
}