package com.fti.softi.services.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.CurrentUserService;
import com.fti.softi.services.FoodEntryService;
import com.fti.softi.utils.DateUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FoodEntryServiceImpl implements FoodEntryService {
  private final FoodEntryRepository foodEntryRepository;
  private final CurrentUserService currentUserService;

  @Override
  public List<FoodEntry> getAllForUser() {
    long userId = currentUserService.getCurrentUserId();
    return foodEntryRepository.findByUserId(userId);
  }

  public List<FoodEntry> filterCurrentWeek(List<FoodEntry> foodEntries) {
    LocalDateTime weekStart = DateUtils.currentWeekStart();
    List<FoodEntry> weeklyEntries = foodEntries.stream().filter(e -> e.getCreatedAt().isAfter(weekStart)).toList();
    return weeklyEntries;
  }

  @Override
  public List<FoodEntry> getLastMonthForUser() {
    long userId = currentUserService.getCurrentUserId();
    LocalDateTime monthStart = DateUtils.currentMonthStart().minusMonths(1);
    return foodEntryRepository.findByUserIdAndDateRange(userId, monthStart, LocalDateTime.now());
  }

  @Override
  public int getDailyCalories(List<FoodEntry> foodEntries) {
    return foodEntries.stream()
        .filter(entry -> entry.getCreatedAt().isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
            && entry.getCreatedAt().isBefore(LocalDateTime.now()))
        .mapToInt(FoodEntry::getCalories)
        .sum();
  }

  @Override
  public double getExpenditure(List<FoodEntry> foodEntries) {
    return foodEntries.stream().mapToDouble(FoodEntry::getPrice).sum();
  }

  @Override
  public boolean insertFoodEntry(String name, String description,
      double price, double calories, LocalDateTime createdAt) {
    long userId = currentUserService.getCurrentUserId();
    if (createdAt == null)
      createdAt = LocalDateTime.now();
    return foodEntryRepository.insertFoodEntryById(
        userId,
        name, description, price, calories, createdAt) == 1;
  }

  @Override
  public LinkedHashMap<String, Integer> getDaysAboveCalorieThreshold(List<FoodEntry> foodEntries, int calorieThreshold) {
    return foodEntries.stream()
        .collect(Collectors.groupingBy(FoodEntry::getDate))
        .entrySet().stream()
        .filter(entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum() >= calorieThreshold)
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum(),
            (e1, e2) -> e1,
            LinkedHashMap::new));
  }

  @Override
  public Page<FoodEntry> getFoodPageForUser(Pageable pageable) {
    long userId = currentUserService.getCurrentUserId();
    return foodEntryRepository.findByUserId(userId, pageable);
  }

  @Override
  public List<FoodEntry> getByDate(LocalDateTime start, LocalDateTime end) {
    long userId = currentUserService.getCurrentUserId();
    return foodEntryRepository.findByUserIdAndDateRange(userId, start, end);
  }
  
}
