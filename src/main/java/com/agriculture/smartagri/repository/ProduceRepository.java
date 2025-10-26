package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Produce;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    List<Produce> findByFarmerId(Long userId); // farmer now maps to User
}
