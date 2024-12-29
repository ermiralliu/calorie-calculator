package com.fti.softi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.CurrentUserService;
import com.fti.softi.services.FoodEntryService;

// @SpringBootTest
// @AutoConfigureMockMvc
public class FoodEntryControllerTests {
  @Mock
  private FoodEntryRepository foodEntryRepository;
  @Mock
  private CurrentUserService currentUserService;
  @Mock
  private FoodEntryService foodEntryService;
  @InjectMocks
  private FoodEntryController foodEntryController;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(foodEntryController).build();
  }

  @Test
  @WithMockUser(username = "usr", roles = {"USER"})
  void testAddFoodEntry() throws Exception {
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&price=1.12&date=2024-12-27";

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isFound()); // we get 302 cause of redirect after adding
  }

  @Test
  @WithMockUser(username = "usr", roles = {"USER"})
  void testAddFoodEntryWithMissingData() throws Exception {
    String newFoodEntryUrlEncoded = "name=&calories=95&description=Tasty&date=2024-12-27"; // Missing name

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to validation
  }

  @Test
  @WithMockUser(username = "usr", roles = {"USER"})
  void testAddFoodEntryWithInvalidDate() throws Exception {
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&date=invalid-date"; // Invalid date format

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to invalid date format
  }

}
