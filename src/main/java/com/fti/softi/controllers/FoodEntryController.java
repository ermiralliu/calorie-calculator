package com.fti.softi.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
		
		Integer dailyCalories = foodEntries.stream()
				.filter(
						entry -> entry.getCreatedAt().compareTo( LocalDateTime.now()) <= 0  
						&& entry.getCreatedAt().compareTo(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) >= 0)
				.mapToInt( entry -> entry.getCalories())
				.sum();
		// using streams will be moved to a service

		Double totalExpenditure = foodEntries.stream()
				.filter(
						entry -> entry.getCreatedAt().compareTo( LocalDateTime.now()) <= 0  
						&& entry.getCreatedAt().compareTo(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)) >= 0)
				.mapToDouble( entry -> entry.getPrice())
				.sum();
			System.out.println('\n'+ dailyCalories + '\n');
		System.out.println('\n'+ totalExpenditure+'\n');
		model.addAttribute("foodEntries", foodEntries);
		model.addAttribute("dailyCalories", dailyCalories);
		model.addAttribute("totalExpenditure", totalExpenditure);
		model.addAttribute("exceededCalorieDays", new ArrayList<String>()); // will be fixed later
		return "food";
	}

	@PostMapping("/add")
	public String addFoodEntry(
			@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("price") Double price, @RequestParam("calories") Integer calories,
			@RequestParam(name="createdAt", required=false) LocalDateTime dateTime) {
		User user = currentUserService.getCurrentUser();
		if(dateTime == null)
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
