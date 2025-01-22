package com.fti.softi.services;

import java.util.LinkedHashMap;
import java.util.List;

import com.fti.softi.dtos.CalorieDto;

public interface AdminService {

  void deleteFoodEntryById(long id);

  void updateFoodEntryById(long id);

  public List<String> getUsersExceedingMonthlyLimit(double monthlyLimit);

  public List<CalorieDto> getAverageCaloriesPerUserPerDay();

  public LinkedHashMap<String, Integer> getWeeklyEntryComparison();

}
