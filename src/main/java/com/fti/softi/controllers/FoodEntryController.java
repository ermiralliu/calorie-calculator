package com.fti.softi.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.CurrentUserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class FoodEntryController {
  private final FoodEntryRepository foodEntryRepository;
  private final CurrentUserService currentUserService;

  @GetMapping
  public String listFoodEntries(Model model) {
    Long userId = currentUserService.getCurrentUserId();
    List<FoodEntry> foodEntries = foodEntryRepository.findByUserId(userId);

    int dailyCalories = foodEntries.stream()
        .filter(entry -> entry.getCreatedAt().isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) // start
                                                                                                                   // of
                                                                                                                   // month
                                                                                                                   // and
                                                                                                                   // start
                                                                                                                   // of
                                                                                                                   // day
                                                                                                                   // will
                                                                                                                   // be
                                                                                                                   // moved
                                                                                                                   // as
                                                                                                                   // helper
                                                                                                                   // functions
            && entry.getCreatedAt().isBefore(LocalDateTime.now()))
        .mapToInt(FoodEntry::getCalories)
        .sum();
    // using streams will be moved to a service

    double totalExpenditure = foodEntries.stream()
        .filter(
            entry -> entry.getCreatedAt()
                .isAfter(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
                && entry.getCreatedAt().isBefore(LocalDateTime.now()))
        .mapToDouble(FoodEntry::getPrice)
        .sum();
    System.out.println('\n' + dailyCalories + '\n');
    System.out.println('\n' + totalExpenditure + '\n');
    model.addAttribute("foodEntries", foodEntries);
    model.addAttribute("dailyCalories", dailyCalories);
    model.addAttribute("totalExpenditure", totalExpenditure);
    // Map<Integer, Integer> daysOverDailyCalories = foodEntries.stream()
    // .collect(Collectors.groupingBy( entry->entry.getCreatedAt().getDayOfYear(),
    // Collectors.summingInt(FoodEntry::getCalories)))
    // .entrySet().stream()
    // .filter(Food);
    int minCalories = 2000; // this will be changed later
    var daysOverDailyCalories = foodEntries.stream()
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
    model.addAttribute("exceededCalorieDays", daysOverDailyCalories.entrySet()); // will be fixed later
    return "food";
  }

  @PostMapping("/add")
  public String addFoodEntry(
      @RequestParam("name") String name, @RequestParam("description") String description,
      @RequestParam("price") Double price, @RequestParam("calories") Integer calories,
      @RequestParam(name = "createdAt", required = false) LocalDateTime dateTime) {
    User user = currentUserService.getCurrentUser();
    if (dateTime == null)
      dateTime = LocalDateTime.now();
    FoodEntry foodEntry = FoodEntry.builder()
        .name(name)
        .user(user)
        .description(description)
        .price(price)
        .calories(calories)
        .createdAt(dateTime)
        .build();
    foodEntryRepository.save(foodEntry);
    return "redirect:/food";
  }
}
