package com.fti.softi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.services.AdminService;

// @WebMvcTest(AdminController.class)
class AdminControllerTest {

  @Mock
  private AdminService adminService;
  @Mock
  private Model model;
  @InjectMocks
  private AdminController adminController;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
  }

  // Test for editing a food entry
  @Test
  void testEditFoodEntry() throws Exception {

    Long id = 1L;
    String name = "Apple";
    String description = "Fresh apple";
    Double price = 1.0;
    Integer calories = 100;

    // Mock the food entry repository behavior
    when(adminService.foodIsPresent(id)).thenReturn(true);

    // POST request and verify the redirect
    mockMvc.perform(post("/admin/food/update")
        .param("id", id.toString())
        .param("name", name)
        .param("description", description)
        .param("price", price.toString())
        .param("calories", calories.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/admin/food/update?id="+id));

    // Verify that the food entry was updated
    verify(adminService, times(1)).updateFoodEntry(id, name, description, price, calories, null);
  }

  // Test for deleting a food entry
  @Test
  void testDeleteFoodEntry() throws Exception {

    Long id = 1L;

    // POST request and verify the redirect
    mockMvc.perform(post("/admin/food/delete")
        .param("id", id.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/admin"));

    // Verify that the food entry was deleted
    verify(adminService, times(1)).deleteFoodEntryById(id);
  }

  @Test
    void testGetUserPage() {
        Long userId = 123L;
        List<FoodEntry> foodEntries = new ArrayList<>(); // Create some sample food entries
        foodEntries.add(new FoodEntry()); // Add some dummy entries
        foodEntries.add(new FoodEntry());

        when(adminService.getAllForUser(userId)).thenReturn(foodEntries);

        String viewName = adminController.getUserPage(userId, model);

        assertEquals("admin-see-user", viewName);
        verify(model).addAttribute("admin", true);
        verify(model).addAttribute("foodEntries", foodEntries);
    }

}
