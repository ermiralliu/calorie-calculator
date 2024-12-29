package com.fti.softi.controllers;

import java.time.LocalDateTime;
import java.util.List;

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
