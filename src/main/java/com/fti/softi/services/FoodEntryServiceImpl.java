package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FoodEntryServiceImpl implements FoodEntryService {
  private final FoodEntryRepository foodEntryRepository;
  private final CurrentUserService currentUserService;

  @Override
  public List<FoodEntry> getAllForUser() {
    return foodEntryRepository.findByUserId(currentUserService.getCurrentUserId());
  };

  @Override
  public int getDailyCalories(List<FoodEntry> foodEntries) {
    return foodEntries.stream()
        .filter(entry -> entry.getCreatedAt().isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
            && entry.getCreatedAt().isBefore(LocalDateTime.now()))
        .mapToInt(FoodEntry::getCalories)
        .sum();
  }

  @Override
  public int getDailyCalories() {
    return getDailyCalories(foodEntryRepository.findByUserId(currentUserService.getCurrentUserId()));
  }

  @Override
  public double getMonthlyExpenditure(List<FoodEntry> foodEntries) {
    return foodEntries.stream()
        .filter(
            entry -> entry.getCreatedAt()
                .isAfter(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
                && entry.getCreatedAt().isBefore(LocalDateTime.now()))
        .mapToDouble(FoodEntry::getPrice)
        .sum();
  }

  @Override
  public double getMonthlyExpenditure() {
    return getMonthlyExpenditure(foodEntryRepository.findByUserId(currentUserService.getCurrentUserId()));
  };

  @Override
  public boolean insertFoodEntry(String name, String description,
      double price, double calories, LocalDateTime createdAt) {
    if (createdAt == null)
        createdAt = LocalDateTime.now();
    return foodEntryRepository.insertFoodEntrybyId(
        currentUserService.getCurrentUserId(),
        name, description, price, calories, createdAt) == 1;
  };
  @Override
  public LinkedHashMap<String, Integer> getDaysAboveCalorieThreshold(List<FoodEntry> foodEntries, int minCalories){
    return foodEntries.stream()
        .collect(Collectors.groupingBy(FoodEntry::getDate))
        .entrySet().stream()
        .filter(entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum() >= minCalories)
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(
          Map.Entry::getKey,
          entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum(),
          (e1, e2) -> e1, 
          LinkedHashMap::new
        ));
  };
}
