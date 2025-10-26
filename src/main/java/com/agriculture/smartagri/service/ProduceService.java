package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.Produce;
import com.agriculture.smartagri.repository.ProduceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProduceService {

    @Autowired
    private ProduceRepository produceRepository;

    public List<Produce> getProduceByFarmer(Long userId) {
        return produceRepository.findByFarmerId(userId);
    }

    public Produce saveProduce(Produce produce) {
        return produceRepository.save(produce);
    }

    public List<Produce> getAllProduce() {
        return produceRepository.findAll();
    }

    public List<Produce> filterProduce(String category, Double minPrice, Double maxPrice) {
        return produceRepository.findAll().stream()
            .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
            .filter(p -> minPrice == null || p.getPricePerKg() >= minPrice)
            .filter(p -> maxPrice == null || p.getPricePerKg() <= maxPrice)
            .toList();
    }
}
