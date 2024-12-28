package com.fti.softi.controllers;

import static org.mockito.ArgumentMatchers.any;
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
    // Arrange: mock the food entries returned from the repository
    List<FoodEntry> foodEntries = Arrays.asList(
        FoodEntry.builder()
            .id(1L)
            .name("Apple")
            .description("Fresh apple")
            .calories(100)
            .price(1.0)
            .createdAt(LocalDateTime.now().withHour(0))
            .build(),
        FoodEntry.builder()
            .id(2L)
            .name("Banana")
            .description("Ripe banana")
            .calories(150)
            .price(1.2)
            .createdAt(LocalDateTime.now().withHour(0))
            .build());
    when(foodEntryRepository.findAll()).thenReturn(foodEntries);

    // GET request and verify the response
    mockMvc.perform(get("/admin"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin"))
        .andExpect(model().attributeExists("foodEntries"))
        .andExpect(model().attribute("foodEntries", foodEntries));
  }

  // Test for adding a food entry
  @Test
  void testAddFoodEntry() throws Exception {
    // Arrange: prepare input data
    String name = "Apple";
    String description = "Fresh apple";
    Double price = 1.0;
    Integer calories = 100;

    // POST request and verify the redirect
    mockMvc.perform(post("/admin/food-entries/add")
        .param("name", name)
        .param("description", description)
        .param("price", price.toString())
        .param("calories", calories.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/admin/food-entries"));

    // Verify that food entry was saved
    verify(foodEntryRepository, times(1)).save(any(FoodEntry.class));
  }

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
  @Test
  void testDeleteFoodEntry() throws Exception {

    Long id = 1L;

    // POST request and verify the redirect
    mockMvc.perform(post("/admin/food-entries/delete")
        .param("id", id.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/admin/food-entries"));

    // Verify that the food entry was deleted
    verify(foodEntryRepository, times(1)).deleteById(id);
  }
}
