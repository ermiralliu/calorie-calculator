package com.fti.softi.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.services.AdminService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
  private final AdminService foodEntryService;

  @GetMapping
  public String adminHome(Model model) {
    // List<FoodEntry> foodEntries = foodEntryRepository.findAll(); // Fetch all food 
    
    // and those who have passed the monthly limit
    LinkedHashMap<String, Integer> weeklyComparison = foodEntryService.getWeeklyEntryComparison();
    List<CalorieDto> avgCalories = foodEntryService.averageDailyCaloriesLast7Days();

    // model.addAttribute("foodEntries", foodEntries);
    model.addAttribute("weeklyComparison", weeklyComparison);
    model.addAttribute("avgCalories", avgCalories);

    return "admin";
  }

  @PutMapping("/food")
  public String editFoodEntry(
      @RequestParam("id") Long id,
      @RequestParam("name") String name,
      @RequestParam("description") String description,
      @RequestParam("price") Double price,
      @RequestParam("calories") Integer calories
  ) {
    if(!foodEntryService.foodIsPresent(id)){
      // redirectAttribute qe food entry doesn't exist, although this makes no sense
      return "redirect:/admin/food-entries";
    }
    foodEntryService.updateFoodEntry(id, name, description, price, calories, null);
    // redirectAttribute qe futet ne dialog dhe thote: food entry updated successfully
    return "redirect:/admin/food-entries";
  }

  @DeleteMapping("/food")
  public String deleteFoodEntry(@RequestParam("id") Long id) {
    foodEntryService.deleteFoodEntryById(id);
    return "redirect:/admin/food-entries";
  }
}
