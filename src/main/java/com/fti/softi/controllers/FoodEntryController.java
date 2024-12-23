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
import com.fti.softi.repositories.UserRepository;

@Controller
@RequestMapping("/food")
public class FoodEntryController {
    private final FoodEntryRepository foodEntryRepository;
    private final UserRepository userRepository;

    public FoodEntryController(FoodEntryRepository foodEntryRepository, UserRepository userRepository) {
        this.foodEntryRepository = foodEntryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public String listFoodEntries(Model model, @RequestParam("userId") Long userId) {
        List<FoodEntry> foodEntries = foodEntryRepository.findByUserId(userId);
        model.addAttribute("foodEntries", foodEntries);
        return "food/list";
    }

    @PostMapping("/add")
    public String addFoodEntry(
            @RequestParam("userId") Long userId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("calories") Integer calories
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        FoodEntry foodEntry = new FoodEntry(name, user, description, price, calories);
        foodEntry.setCreatedAt(LocalDateTime.now());
        foodEntryRepository.save(foodEntry);
        return "redirect:/food/list?userId=" + userId;
    }
}
