// package com.fti.softi.controllers;

// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;

// import com.fti.softi.dtos.CalorieDto;
// import com.fti.softi.models.FoodEntry;
// import com.fti.softi.repositories.FoodEntryRepository;
// // import com.fti.softi.services.CurrentUserService;
// import com.fti.softi.services.FoodEntryService;

// import lombok.AllArgsConstructor;

// @Controller
// @AllArgsConstructor
// @RequestMapping("/admin")
// public class AdminController {
//   private final FoodEntryRepository foodEntryRepository;
//   private final CurrentUserService currentUserService;
//   private final FoodEntryService foodEntryService;

//   @GetMapping
//   public String adminHome(Model model) {
//     List<FoodEntry> foodEntries = foodEntryRepository.findAll(); // Fetch all food entries

//     // Integer dailyCalories = foodEntries.stream()
//     //     .filter(entry -> entry.getCreatedAt().isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
//     //         && entry.getCreatedAt().isBefore(LocalDateTime.now()))
//     //     .mapToInt(FoodEntry::getCalories)
//     //     .sum();

//     // Double totalExpenditure = foodEntries.stream()
//     //     .filter(entry -> entry.getCreatedAt()
//     //         .isAfter(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
//     //         && entry.getCreatedAt().isBefore(LocalDateTime.now()))
//     //     .mapToDouble(FoodEntry::getPrice)
//     //     .sum();

//     var weeklyComparison = foodEntryService.getWeeklyEntryComparison();
//     List<CalorieDto> avgCalories = foodEntryService.getAverageCaloriesPerUserPerDay();

//     model.addAttribute("foodEntries", foodEntries);
//     model.addAttribute("dailyCalories", dailyCalories);
//     model.addAttribute("totalExpenditure", totalExpenditure);
//     model.addAttribute("weeklyComparison", weeklyComparison);
//     model.addAttribute("avgCalories", avgCalories);

//     return "admin";
//   }

//   @PostMapping("/food-entries/edit")
//   public String editFoodEntry(
//       @RequestParam("id") Long id,
//       @RequestParam("name") String name,
//       @RequestParam("description") String description,
//       @RequestParam("price") Double price,
//       @RequestParam("calories") Integer calories) {

//     FoodEntry foodEntry = foodEntryRepository.findById(id)
//         .orElseThrow(() -> new IllegalArgumentException("Invalid food entry ID"));
//     foodEntry.setName(name);
//     foodEntry.setDescription(description);
//     foodEntry.setPrice(price);
//     foodEntry.setCalories(calories);
//     foodEntryRepository.save(foodEntry);
//     return "redirect:/admin/food-entries";
//   }

//   @PostMapping("/food-entries/delete")
//   public String deleteFoodEntry(@RequestParam("id") Long id) {
//     foodEntryRepository.deleteById(id);
//     return "redirect:/admin/food-entries";
//   }
//   @GetMapping("/test")
//   @ResponseBody
//   public Object test(){
//     return foodEntryService.getAverageCaloriesPerUserPerDay();
//   }
// }
