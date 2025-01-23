package com.fti.softi.controllers;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;
import com.fti.softi.services.AdminService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
  private final AdminService adminService;

  @GetMapping
  public String adminHome(Model model) {
    // List<FoodEntry> foodEntries = foodEntryRepository.findAll(); // Fetch all food 
    
    // and those who have passed the monthly limit
    LinkedHashMap<String, Integer> weeklyComparison = adminService.getWeeklyEntryComparison();
    List<CalorieDto> avgCalories = adminService.averageDailyCaloriesLast7Days();
    List<User> users = adminService.getAllUsers();

    // model.addAttribute("foodEntries", foodEntries);
    model.addAttribute("weeklyComparison", weeklyComparison);
    model.addAttribute("avgCalories", avgCalories);
    model.addAttribute("users", users);

    return "admin";
  }

  @GetMapping("/user")
  public String getUserPage(
    @RequestParam("user_id") Long user_id,
    Model model
  ){
    List<FoodEntry> foodEntries = adminService.getAllForUser(user_id);
    model.addAttribute("admin", true);
    model.addAttribute("foodEntries", foodEntries);
    return "admin-see-user";
  }

  @GetMapping("/food/update")
  public String editFoodEntryView(
    @RequestParam("id") Long foodId,
    Model model
  ){
    FoodEntry entry = adminService.getFood(foodId);
    model.addAttribute("foodEntry", entry);
    return "updateFood";
  }

  // after changing the redirects here, change them in admin tests too
  @PostMapping("/food/update")
  public String editFoodEntry(
      @RequestParam("id") Long id,
      @RequestParam("name") String name,
      @RequestParam("description") String description,
      @RequestParam("price") Double price,
      @RequestParam("calories") Integer calories
  ) {
    if(!adminService.foodIsPresent(id)){
      // redirectAttribute qe food entry doesn't exist, although this makes no sense
      return "redirect:/admin/food/update?id=" + id;
    }
    adminService.updateFoodEntry(id, name, description, price, calories, null);
    // redirectAttribute qe futet ne dialog dhe thote: food entry updated successfully
    return "redirect:/admin/food/update?id=" + id;
  }

  @PostMapping("/food/delete")
  public String deleteFoodEntry(
    @RequestParam("id") Long id,
    RedirectAttributes redir
  ) {
    adminService.deleteFoodEntryById(id);
    redir.addFlashAttribute("message", "Food Entry with id "+id+" successfully got deleted");
    return "redirect:/admin";
  }
}
