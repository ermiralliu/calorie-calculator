package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;

public interface AdminService {

  boolean foodIsPresent(long id);

  void deleteFoodEntryById(long id);

  public void updateFoodEntry(  long foodId, String name, String description, 
    Double price, Integer calories, LocalDateTime createdAt );

  public List<String> getUsersExceedingMonthlyExpenditureLimit(double monthlyLimit);

  public List<CalorieDto> averageDailyCaloriesLast7Days();

  public LinkedHashMap<String, Integer> getWeeklyEntryComparison();

  List<User> getAllUsers();

  List<FoodEntry> getAllForUser(long user_id);
  FoodEntry getFood(long foodId);

}
