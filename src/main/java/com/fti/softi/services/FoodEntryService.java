package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;

public interface FoodEntryService {

  public List<FoodEntry> getAllForUser();
  public List<FoodEntry> getByDate(LocalDateTime start, LocalDateTime end);
  public Page<FoodEntry> getFoodPageForUser(Pageable pageable);

  public int getDailyCalories(List<FoodEntry> foodEntries);
  public int getDailyCalories();
  
  public double getMonthlyExpenditure(List<FoodEntry> foodEntries); //only for this month
  public double getMonthlyExpenditure();

  public boolean insertFoodEntry( String name, String description,
      double price, double calories, LocalDateTime createdAt);

  public LinkedHashMap<String, Integer> getWeeklyEntryComparison(); // Last 7 days vs previous week
  public List<CalorieDto> getAverageCaloriesPerUserPerDay();
  public List<String> getUsersExceedingMonthlyLimit(double monthlyLimit);

  public LinkedHashMap<String, Integer> getDaysAboveCalorieThreshold(List<FoodEntry> foodEntries, int minCalories);
  //Other methods to be added later
}
