package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;

public interface FoodEntryService {
  public List<FoodEntry> getAllForUser();

  public int getDailyCalories(List<FoodEntry> foodEntries);
  public int getDailyCalories();
  
  public double getMonthlyExpenditure(List<FoodEntry> foodEntries); //only for this month
  public double getMonthlyExpenditure();

  public boolean insertFoodEntry(String name, String description,
   double price, double calories, LocalDateTime createdAt);

  LinkedHashMap<String, Integer> getWeeklyEntryComparison(); // Last 7 days vs previous week
  List<CalorieDto> getAverageCaloriesPerUserPerDay();
  List<String> getUsersExceedingMonthlyLimit(double monthlyLimit);

  LinkedHashMap<String, Integer> getDaysAboveCalorieThreshold(List<FoodEntry> foodEntries, int minCalories);
  //Other methods to be added later
}
