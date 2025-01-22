package com.fti.softi.services.impl;

import java.time.DayOfWeek;
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
import com.fti.softi.services.BaseService;
import com.fti.softi.services.FoodEntryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FoodEntryServiceImpl extends BaseService implements FoodEntryService {
  private final FoodEntryRepository foodEntryRepository;

  private LocalDateTime dayStart(LocalDateTime dateTime) {
    return dateTime.withHour(0).withMinute(0).withSecond(0);
  }

  private LocalDateTime monthStart() {
    return dayStart(LocalDateTime.now().withDayOfMonth(1));
  }

  private LocalDateTime weekStart() {
    return dayStart(LocalDateTime.now().with(DayOfWeek.MONDAY));
  }

  @Override
  public List<FoodEntry> getAllForUser() {
    long userId = this.getCurrentUserId();
    return foodEntryRepository.findByUserId(userId);
  }

  // @Override
  // public List<FoodEntry> getLastWeekForUser() {
  //   long userId = this.getCurrentUserId();
  //   LocalDateTime weekStart = weekStart();
  //   return foodEntryRepository.findByUserIdAndDateRange(userId, weekStart, LocalDateTime.now());
  // }

  public List<FoodEntry> filterCurrentWeek(List<FoodEntry> foodEntries) {
    LocalDateTime weekStart = weekStart();
    List<FoodEntry> weeklyEntries = foodEntries.stream().filter(e -> e.getCreatedAt().isAfter(weekStart)).toList();
    return weeklyEntries;
  }

  @Override
  public List<FoodEntry> getLastMonthForUser() {
    long userId = this.getCurrentUserId();
    LocalDateTime monthStart = monthStart();
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

  // @Override
  // public int getDailyCalories() {
  //   long userId = this.getCurrentUserId();
  //   return getDailyCalories(foodEntryRepository.findByUserId(userId));
  // }

  // @Override
  // public double getMonthlyExpenditure(List<FoodEntry> foodEntries) {
  //   return foodEntries.stream()
  //       .filter(
  //           entry -> entry.getCreatedAt()
  //               .isAfter(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
  //               && entry.getCreatedAt().isBefore(LocalDateTime.now()))
  //       .mapToDouble(FoodEntry::getPrice)
  //       .sum();
  // }

  @Override
  public double getExpenditure(List<FoodEntry> foodEntries) {
    return foodEntries.stream().mapToDouble(FoodEntry::getPrice).sum();
  }

  // @Override
  // public double getMonthlyExpenditure() {
  //   long userId = this.getCurrentUserId();
  //   return getMonthlyExpenditure(foodEntryRepository.findByUserId(userId));
  // }

  @Override
  public boolean insertFoodEntry(String name, String description,
      double price, double calories, LocalDateTime createdAt) {
    long userId = this.getCurrentUserId();
    if (createdAt == null)
      createdAt = LocalDateTime.now();
    return foodEntryRepository.insertFoodEntrybyId(
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

  // @Override
  // public LinkedHashMap<String, Integer> getWeeklyEntryComparison() {
  //   LocalDateTime now = LocalDateTime.now();
  //   LocalDateTime startOfWeek = now.minusDays(7);
  //   LocalDateTime previousWeekStart = startOfWeek.minusDays(7);

  //   int last7Days = ((int) foodEntryRepository.countByCreatedAtBetween(startOfWeek, now));
  //   int previousWeek = ((int) foodEntryRepository.countByCreatedAtBetween(previousWeekStart, startOfWeek));

  //   LinkedHashMap<String, Integer> weeklyEntryComparison = new LinkedHashMap<>();
  //   weeklyEntryComparison.put("Last 7 Days", last7Days);
  //   weeklyEntryComparison.put("Previous Week", previousWeek);

  //   return weeklyEntryComparison; // Return the map directly
  // }

  // @Override // Konverton Listen me Object array qe merret nga databaza ne Liste me
  //           // CalorieDto
  // public List<CalorieDto> getAverageCaloriesPerUserPerDay() {
  //   return foodEntryRepository.findAverageCaloriesPerUser().stream()
  //       .map(item -> {
  //         String name = (String) item[0];
  //         String caloriesPerDay = String.format("%.2f", item[1]);
  //         return new CalorieDto(name, caloriesPerDay);
  //       })
  //       .collect(Collectors.toList());
  // }

  // @Override
  // public List<String> getUsersExceedingMonthlyLimit(double monthlyLimit) {
  //   LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1);
  //   return foodEntryRepository.findAll().stream()
  //       .collect(Collectors.groupingBy(entry -> entry.getUser().getId()))
  //       .entrySet().stream()
  //       .filter(entry -> entry.getValue().stream()
  //           .filter(e -> e.getCreatedAt().isAfter(startOfMonth))
  //           .mapToDouble(FoodEntry::getPrice).sum() > monthlyLimit)
  //       .map(entry -> entry.getValue().get(0).getUser().getUsername())
  //       .toList();
  // }

  @Override
  public Page<FoodEntry> getFoodPageForUser(Pageable pageable) {
    long userId = this.getCurrentUserId();
    return foodEntryRepository.findByUserId(userId, pageable);
  }

  @Override
  public List<FoodEntry> getByDate(LocalDateTime start, LocalDateTime end) {
    long userId = this.getCurrentUserId();
    return foodEntryRepository.findByUserIdAndDateRange(userId, start, end);
  }


}
