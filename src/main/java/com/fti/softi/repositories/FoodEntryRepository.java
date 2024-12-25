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

	// Gjej totalin e kalorive per nje perdorues ne nje dite specifike
//	@Query("SELECT SUM(f.calories) FROM FoodEntry f WHERE f.user.id = :userId AND DATE(f.createdAt) = DATE(:date)")
//	Integer getTotalCaloriesForUserAndDate(@Param("userId") Long userId, @Param("date") String date);

//    @Query("SELECT SUM(f.price) FROM Food_entry f WHERE f.user.id = :userId AND MONTH(f.createdAt) = :month")
//    Double getTotalMonthlyExpenditureForUserAndMonth(@Param("userId") Long userId, @Param("month") Integer month);
//    
//	Double sumPriceByDateBetween(LocalDate startDate, LocalDate endDate);	

	@Query("SELECT SUM(fe.price) FROM FoodEntry fe WHERE user.id= :userId AND (fe.createdAt BETWEEN :startDate AND :endDate)") 
	Double sumPriceByDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
