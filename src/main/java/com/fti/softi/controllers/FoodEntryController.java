package com.fti.softi.controllers;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.services.FoodEntryService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class FoodEntryController {
  private final FoodEntryService foodEntryService;

  @GetMapping
  public String listFoodEntries(Model model) {
    List<FoodEntry> foodEntries = foodEntryService.getAllForUser();
    int dailyCalories = foodEntryService.getDailyCalories(foodEntries);
    double totalExpenditure = foodEntryService.getMonthlyExpenditure(foodEntries);

    model.addAttribute("foodEntries", foodEntries);
    model.addAttribute("dailyCalories", dailyCalories);
    model.addAttribute("totalExpenditure", totalExpenditure);

    int minCalories = 2000; // this will be changed later. 
    // Probably will be set by user and held in CurrentUserService
    var daysOverDailyCalories = foodEntryService.getDaysAboveCalorieThreshold(foodEntries, minCalories);
    model.addAttribute("exceededCalorieDays", daysOverDailyCalories.entrySet()); // will be fixed later
    return "food";
  }

  @GetMapping("/get-interval")
  public String listFoodEntriesByInterval(
          @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
          @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
          Model model) {

    List<FoodEntry> foodEntries = foodEntryService.getAllForUser();

    List<FoodEntry> filteredEntries = foodEntries.stream()
            .filter(entry -> {
              LocalDate entryDate = LocalDate.parse(entry.getDate());
              return !entryDate.isBefore(startDate) && !entryDate.isAfter(endDate);
            })
            .toList();

    int dailyCalories = foodEntryService.getDailyCalories(filteredEntries);
    double totalExpenditure = foodEntryService.getMonthlyExpenditure(filteredEntries);

    model.addAttribute("foodEntries", filteredEntries);
    model.addAttribute("dailyCalories", dailyCalories);
    model.addAttribute("totalExpenditure", totalExpenditure);

    int minCalories = 2000;
    var daysOverDailyCalories = foodEntryService.getDaysAboveCalorieThreshold(filteredEntries, minCalories);
    model.addAttribute("exceededCalorieDays", daysOverDailyCalories.entrySet());

    return "food";
  }



  @PostMapping("/add")
  public String addFoodEntry(
      @RequestParam("name") String name, @RequestParam("description") String description,
      @RequestParam("price") Double price, @RequestParam("calories") Integer calories,
      @RequestParam(name = "createdAt", required = false) LocalDateTime dateTime) {

    boolean success = foodEntryService.insertFoodEntry(name, description, price, calories, dateTime);
    if(success)
      System.out.println("Database insertion successful");
    return "redirect:/food";
  }
}
