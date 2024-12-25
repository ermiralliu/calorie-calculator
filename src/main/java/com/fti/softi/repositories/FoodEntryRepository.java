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

	@Query("SELECT SUM(fe.price) FROM FoodEntry fe WHERE user.id= :userId AND (fe.createdAt BETWEEN :startDate AND :endDate)") 
	Double sumPriceByDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
