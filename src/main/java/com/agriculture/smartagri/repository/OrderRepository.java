package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}