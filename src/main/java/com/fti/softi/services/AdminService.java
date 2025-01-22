package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.fti.softi.dtos.CalorieDto;

public interface AdminService {

  void deleteFoodEntryById(long id);

  public void updateFoodEntry(  long foodId, String name, String description, 
    Double price, Integer calories, LocalDateTime createdAt );

  public List<String> getUsersExceedingMonthlyLimit(double monthlyLimit);

  public List<CalorieDto> getAverageCaloriesPerUserPerDay();

  public LinkedHashMap<String, Integer> getWeeklyEntryComparison();

}
