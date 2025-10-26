package com.agriculture.smartagri.repository;

import com.agriculture.smartagri.entity.Order;
import com.agriculture.smartagri.entity.SaleListing;
import com.agriculture.smartagri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByBuyer(User buyer);
    
    List<Order> findBySaleListing(SaleListing saleListing);
    
    List<Order> findByStatus(String status);
    
    List<Order> findByBuyerAndStatus(User buyer, String status);
    
    @Query("SELECT o FROM Order o WHERE o.saleListing.farmer = :farmer")
    List<Order> findByFarmer(@Param("farmer") User farmer);
    
    @Query("SELECT o FROM Order o WHERE o.saleListing.farmer = :farmer AND o.status = :status")
    List<Order> findByFarmerAndStatus(@Param("farmer") User farmer, @Param("status") String status);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.buyer = :buyer")
    long countByBuyer(@Param("buyer") User buyer);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.saleListing.farmer = :farmer")
    long countByFarmer(@Param("farmer") User farmer);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") String status);
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrderByCreatedAtDesc();
}