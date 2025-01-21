package com.fti.softi.repositories;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fti.softi.models.FoodEntry;

import jakarta.transaction.Transactional;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
  List<FoodEntry> findByUserId(Long userId);

  @Query(value = "SELECT SUM(price) FROM food_entry WHERE user_id= :userId AND (created_at BETWEEN :startDate AND :endDate)", nativeQuery = true)
  Double sumPriceByDateBetween(
      @Param("userId") Long userId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO food_entry (user_id, name, description, price, calories, created_at) VALUES (:userId, :name, :description, :price, :calories, :createdAt)", nativeQuery = true)
  int insertFoodEntrybyId( // the return value shows number of rows affected
      @Param("userId") long userId,
      @Param("name") String name,
      @Param("description") String description,
      @Param("price") double price,
      @Param("calories") double calories,
      @Param("createdAt") LocalDateTime createdAt); // After some testing we will prove if this is okay

  @Query("SELECT fe FROM FoodEntry fe WHERE fe.createdAt BETWEEN :start AND :end")
  List<FoodEntry> findByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query(value = "SELECT fe FROM FoodEntry fe WHERE fe.createdAt BETWEEN :start AND :end", 
    countQuery = "SELECT COUNT(fe) FROM FoodEntry fe WHERE fe.createdAt BETWEEN :start AND :end")
  long countByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query(value = """
      SELECT u.name AS name,
             COALESCE(SUM(daily_calories) * 1.0 / COUNT(DISTINCT entry_date), 0.0) AS caloriesPerDay
      FROM users u
      LEFT JOIN (
          SELECT user_id,
                 strftime('%Y-%m-%d', datetime(created_at / 1000, 'unixepoch')) AS entry_date,
                 SUM(CAST(calories AS REAL)) AS daily_calories
          FROM food_entry
          GROUP BY user_id, strftime('%Y-%m-%d', datetime(created_at / 1000, 'unixepoch'))
      ) AS fe ON u.id = fe.user_id
      GROUP BY u.id, u.name
      """, nativeQuery = true)
  List<Object[]> findAverageCaloriesPerUser();

  @Query(value = "SELECT * FROM food_entry WHERE user_id = :userId AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
  List<FoodEntry> findByUserIdAndDateRangeNative(
      @Param("userId") Long userId,
      @Param("startDate") Long startDate,
      @Param("endDate") Long endDate);

  default List<FoodEntry> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
    long startMillis = startDate.toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
    long endMillis = endDate.toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
    return findByUserIdAndDateRangeNative(userId, startMillis, endMillis);
  }

  Page<FoodEntry> findByUserId(Long userId, Pageable pageable);
}
