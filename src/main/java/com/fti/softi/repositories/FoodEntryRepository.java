package com.fti.softi.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.models.FoodEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserId(Long userId);

    // Gjej totalin e kalorive per nje perdorues ne nje dite specifike
    @Query("SELECT SUM(f.calories) FROM FoodEntry f WHERE f.user.id = :userId AND DATE(f.createdAt) = :date")
    Integer getTotalCaloriesForUserAndDate(@Param("userId") Long userId, @Param("date") String date);
    
    @Query("SELECT SUM(f.price) FROM FoodEntry f WHERE f.user.id = :userId AND MONTH(f.createdAt) = :month")
    Double getTotalMonthlyExpenditureForUserAndMonth(@Param("userId") Long userId, @Param("month") Integer month);
}
