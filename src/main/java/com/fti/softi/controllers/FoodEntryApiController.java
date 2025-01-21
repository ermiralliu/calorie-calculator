package com.fti.softi.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.services.FoodEntryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/food")
public class FoodEntryApiController {
  private final FoodEntryService foodEntryService;

  @GetMapping
  public Page<FoodEntry> getFoodPage(@RequestParam("page") int page) {
    int size = 20;
    Pageable pageable = PageRequest.of(page, size , Sort.by("createdAt").descending());
    return foodEntryService.getFoodPageForUser(pageable);
  }
}
