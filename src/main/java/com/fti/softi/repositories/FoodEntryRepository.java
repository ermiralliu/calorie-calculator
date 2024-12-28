package com.fti.softi.repositories;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fti.softi.models.FoodEntry;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
	List<FoodEntry> findByUserId(Long userId);

	@Query(value = "SELECT SUM(price) FROM food_entry WHERE user_id= :userId AND (created_at BETWEEN :startDate AND :endDate)", nativeQuery = true) 
	Double sumPriceByDateBetween(
    @Param("userId") Long userId, 
    @Param("startDate") LocalDateTime startDate, 
    @Param("endDate") LocalDateTime endDate
  );

  @Query(value = "INSERT INTO food_entry (user_id, name, description, price, calories, created_at) VALUES (:userIdd, :name, :description, :price, :calories, :createdAt)", nativeQuery = true) 
  FoodEntry insertFoodEntrybyId(
    @Param("userId") Long userId, 
    @Param("name") String name, 
    @Param("description") String description,
    @Param("price") String price,
    @Param("calories") String calories,
    @Param("createdAt") LocalDateTime createdAt
  );
}
