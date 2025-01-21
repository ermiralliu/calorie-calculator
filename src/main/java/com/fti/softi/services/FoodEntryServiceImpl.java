package com.fti.softi.services;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fti.softi.dtos.CalorieDto;
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
  }

  @Override
  public boolean insertFoodEntry(String name, String description,
      double price, double calories, LocalDateTime createdAt) {
    if (createdAt == null)
      createdAt = LocalDateTime.now();
    return foodEntryRepository.insertFoodEntrybyId(
        currentUserService.getCurrentUserId(),
        name, description, price, calories, createdAt) == 1;
  }

  @Override
  public LinkedHashMap<String, Integer> getDaysAboveCalorieThreshold(List<FoodEntry> foodEntries, int minCalories) {
    return foodEntries.stream()
        .collect(Collectors.groupingBy(FoodEntry::getDate))
        .entrySet().stream()
        .filter(entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum() >= minCalories)
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream().mapToInt(FoodEntry::getCalories).sum(),
            (e1, e2) -> e1,
            LinkedHashMap::new));
  }

  @Override
  public LinkedHashMap<String, Integer> getWeeklyEntryComparison() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = now.minusDays(7);
    LocalDateTime previousWeekStart = startOfWeek.minusDays(7);

    int last7Days = foodEntryRepository
        .findByCreatedAtBetween(startOfWeek, now)
        .size();

    int previousWeek = foodEntryRepository
        .findByCreatedAtBetween(previousWeekStart, startOfWeek)
        .size();

    LinkedHashMap<String, Integer> weeklyEntryComparison = new LinkedHashMap<>();
    weeklyEntryComparison.put("Last 7 Days", last7Days);
    weeklyEntryComparison.put("Previous Week", previousWeek);

    return weeklyEntryComparison; // Return the map directly
  }

  @Override
  public List<CalorieDto> getAverageCaloriesPerUserPerDay() {
    return foodEntryRepository.findAverageCaloriesPerUser().stream()
    .map(item -> {
        String name = (String) item[0];
        // Double caloriesPerDay = Double.valueOf((Float) item[1]);
        String caloriesPerDay = String.format("%.2f", item[1]);
        return new CalorieDto(name, caloriesPerDay);
    })
    .collect(Collectors.toList());
    // List<FoodEntry> allEntries = foodEntryRepository.findAll();

    // var entriesByUser = allEntries.stream()
    //     .collect(Collectors.groupingBy(FoodEntry::getUser));

    // return entriesByUser.entrySet().stream()
    //     .map(entry -> {
    //       User user = entry.getKey();
    //       List<FoodEntry> userEntries = entry.getValue();

    //       if (userEntries.isEmpty()) {
    //         return new CalorieDto(user.getName(), 0.0);
    //       }

    //       java.time.Instant minDate = userEntries.stream()
    //           .map(FoodEntry::getCreatedAt)
    //           .min(Comparator.naturalOrder())
    //           .orElse(null);
    //       java.time.Instant maxDate = userEntries.stream()
    //           .map(FoodEntry::getCreatedAt)
    //           .max(Comparator.naturalOrder())
    //           .orElse(null);

    //       if (minDate == null || maxDate == null) {
    //         return new CalorieDto(user.getName(), 0.0); // Handle cases where min or max date is null
    //       }

    //       long days = ChronoUnit.DAYS.between(minDate.toLocalDate(), maxDate.toLocalDate()) + 1;
    //       if (days == 0) {
    //         days = 1;
    //       }
    //       int totalCalories = userEntries.stream()
    //           .mapToInt(FoodEntry::getCalories)
    //           .sum();

    //       double averageCalories = (double) totalCalories / days;
    //       return new CalorieDto(user.getName(), averageCalories);
    //     })
    //     .collect(Collectors.toList());
  }

  @Override
  public List<String> getUsersExceedingMonthlyLimit(double monthlyLimit) {
    LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1);
    return foodEntryRepository.findAll().stream()
        .collect(Collectors.groupingBy(entry -> entry.getUser().getId()))
        .entrySet().stream()
        .filter(entry -> entry.getValue().stream()
            .filter(e -> e.getCreatedAt().isAfter(startOfMonth))
            .mapToDouble(FoodEntry::getPrice).sum() > monthlyLimit)
        .map(entry -> entry.getValue().get(0).getUser().getUsername())
        .toList();
  }

}
