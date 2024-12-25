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
//		Integer dailyCalories = foodEntryRepository.getTotalCaloriesForUserAndDate(userId, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
		Integer dailyCalories = foodEntries.stream()
				.filter(
						entry -> entry.getCreatedAt().compareTo( LocalDateTime.now()) <= 0  
						&& entry.getCreatedAt().compareTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) >= 0)
				.mapToInt( entry -> entry.getCalories())
				.sum();
		
		//		Double totalExpenditure = foodEntryRepository.getTotalMonthlyExpenditureForUserAndMonth(userId, LocalDate.now().getMonthValue());
		Double totalExpenditure = foodEntryRepository.sumPriceByDateBetween(userId, 
				LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0), 
				LocalDateTime.now()
			);
		System.out.println('\n'+ dailyCalories + '\n');
		System.out.println('\n'+ totalExpenditure+'\n');
		model.addAttribute("foodEntries", foodEntries);
		model.addAttribute("dailyCalorieMessage", dailyCalories);
		model.addAttribute("monthlyExpenditureMessage", totalExpenditure);
		return "food";
	}

	@PostMapping("/add")
	public String addFoodEntry(
			@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("price") Double price, @RequestParam("calories") Integer calories) {
		User user = currentUserService.getCurrentUser();
		FoodEntry foodEntry = new FoodEntry(name, user, description, price, calories);
		foodEntryRepository.save(foodEntry);
		return "redirect:/food";
	}
}
