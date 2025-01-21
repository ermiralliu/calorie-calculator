package com.fti.softi.controllers;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fti.softi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.CurrentUserService;

// @WebMvcTest(AdminController.class)
class AdminControllerTest {

  @Mock
  private FoodEntryRepository foodEntryRepository;
  @Mock
  private CurrentUserService currentUserService;
  @InjectMocks
  private AdminController adminController;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
  }

  // Test for listing food entries
  @Test
  void testListFoodEntries() throws Exception {
    // Arrange: Use a fixed point in time for testing
    LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 20, 12, 0); // Example fixed date and time
    FoodEntry entry1 = FoodEntry.builder()
            .id(1L)
            .name("Apple")
            .description("Fresh apple")
            .calories(100)
            .price(1.0)
            .createdAt(fixedNow.minusHours(1)) // Within today's range
            .build();
    FoodEntry entry2 = FoodEntry.builder()
            .id(2L)
            .name("Banana")
            .description("Ripe banana")
            .calories(150)
            .price(1.2)
            .createdAt(fixedNow.minusHours(2)) // Within today's range
            .build();
    List<FoodEntry> foodEntries = Arrays.asList(entry1, entry2);

    when(foodEntryRepository.findAll()).thenReturn(foodEntries);

    // Calculate expected daily calories and total expenditure
    int expectedDailyCalories = foodEntries.stream()
            .filter(entry -> entry.getCreatedAt().isAfter(fixedNow.withHour(0).withMinute(0).withSecond(0)))
            .mapToInt(FoodEntry::getCalories)
            .sum();

    double expectedTotalExpenditure = foodEntries.stream()
            .filter(entry -> entry.getCreatedAt().isAfter(fixedNow.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)))
            .mapToDouble(FoodEntry::getPrice)
            .sum();

    // Act & Assert: Perform the GET request and verify
    mockMvc.perform(get("/admin"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin")) // Verify correct view name
            .andExpect(model().attributeExists("foodEntries", "dailyCalories", "totalExpenditure"))
            .andExpect(model().attribute("foodEntries", foodEntries))
            .andExpect(model().attribute("dailyCalories", expectedDailyCalories))
            .andExpect(model().attribute("totalExpenditure", expectedTotalExpenditure));
  }

  // // Test for adding a food entry
  // @Test
  // void testAddFoodEntry() throws Exception {
  //   // Arrange: prepare input data
  //   String name = "Apple";
  //   String description = "Fresh apple";
  //   Double price = 1.0;
  //   Integer calories = 100;

  //   // Mock the current user
  //   User mockUser = User.builder()
  //           .id(1L)
  //           .build();
  //   when(currentUserService.getCurrentUser()).thenReturn(Optional.of(mockUser));

  //   // Act & Assert: Perform POST request and verify the redirect
  //   mockMvc.perform(post("/admin/food-entries/add")
  //                   .param("name", name)
  //                   .param("description", description)
  //                   .param("price", price.toString())
  //                   .param("calories", calories.toString()))
  //           .andExpect(status().is3xxRedirection())
  //           .andExpect(header().string("Location", "/admin/food-entries"));

  //   // Verify that the food entry was saved with correct attributes
  //   verify(foodEntryRepository, times(1)).save(argThat(foodEntry ->
  //           foodEntry.getName().equals(name) &&
  //                   foodEntry.getDescription().equals(description) &&
  //                   foodEntry.getPrice().equals(price) &&
  //                   foodEntry.getCalories().equals(calories) &&
  //                   foodEntry.getUser().equals(mockUser)
  //   ));
  // }




  // Test for editing a food entry
  @Test
  void testEditFoodEntry() throws Exception {

    Long id = 1L;
    String name = "Apple";
    String description = "Fresh apple";
    Double price = 1.0;
    Integer calories = 100;
    FoodEntry foodEntry = FoodEntry.builder()
        .id(id)
        .name(name)
        .description(description)
        .calories(calories)
        .price(price)
        .build();
    // Mock the food entry repository behavior
    when(foodEntryRepository.findById(id)).thenReturn(java.util.Optional.of(foodEntry));

    // POST request and verify the redirect
    mockMvc.perform(post("/admin/food-entries/edit")
        .param("id", id.toString())
        .param("name", name)
        .param("description", description)
        .param("price", price.toString())
        .param("calories", calories.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/admin/food-entries"));

    // Verify that the food entry was updated
    verify(foodEntryRepository, times(1)).save(foodEntry);
  }

  // Test for deleting a food entry
  // @Test
  // void testDeleteFoodEntry() throws Exception {

  //   Long id = 1L;

  //   // POST request and verify the redirect
  //   mockMvc.perform(post("/admin/food-entries/delete")
  //       .param("id", id.toString()))
  //       .andExpect(status().is3xxRedirection())
  //       .andExpect(header().string("Location", "/admin/food-entries"));

  //   // Verify that the food entry was deleted
  //   verify(foodEntryRepository, times(1)).deleteById(id);
  // }
}
