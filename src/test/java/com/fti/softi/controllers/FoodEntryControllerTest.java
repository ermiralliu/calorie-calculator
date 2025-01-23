package com.fti.softi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.services.FoodEntryService;

// @ExtendWith(MockitoExtension.class)
public class FoodEntryControllerTest {

  @Mock
  private FoodEntryService foodEntryService;

  @Mock
  private Model model; // Mock the Model object

  @InjectMocks
  private FoodEntryController controller;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void testListFoodEntries() {
    // Arrange (Set up mock behavior)
    List<FoodEntry> foodEntries = Arrays.asList(new FoodEntry(), new FoodEntry()); // Example list
    int dailyCalories = 1500;
    double monthlyExpenditure = 120.50;
    List<FoodEntry> weeklyEntries = Arrays.asList(new FoodEntry());
    LinkedHashMap<String, Integer> daysOverDailyCalories = new LinkedHashMap<>();
    daysOverDailyCalories.put("Monday", 2600);

    when(foodEntryService.getLastMonthForUser()).thenReturn(foodEntries);
    when(foodEntryService.getDailyCalories(foodEntries)).thenReturn(dailyCalories);
    when(foodEntryService.getExpenditure(foodEntries)).thenReturn(monthlyExpenditure);
    when(foodEntryService.filterCurrentWeek(foodEntries)).thenReturn(weeklyEntries);
    when(foodEntryService.getDaysAboveCalorieThreshold(weeklyEntries, 2500)).thenReturn(daysOverDailyCalories);

    // Act (Call the controller method)
    String viewName = controller.listFoodEntries(model);

    // Assert (Verify results)
    assertEquals("food", viewName); // Check the view name

    // Verify that model attributes were added
    Mockito.verify(model).addAttribute("foodEntries", foodEntries);
    Mockito.verify(model).addAttribute("dailyCalories", dailyCalories);
    Mockito.verify(model).addAttribute("totalExpenditure", monthlyExpenditure);
    Mockito.verify(model).addAttribute("exceededCalorieDays", daysOverDailyCalories.entrySet());
  }

  @Test
  @WithMockUser(username = "usr", roles = { "USER" })
  void testAddFoodEntry() throws Exception {
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&price=1.12&date=2024-12-27";

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isFound()); // we get 302 cause of redirect after adding
  }

  @Test
  @WithMockUser(username = "usr", roles = { "USER" })
  void testAddFoodEntryWithMissingData() throws Exception {
    String newFoodEntryUrlEncoded = "name=&calories=95&description=Tasty&date=2024-12-27"; // Missing name

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to validation
  }

  @Test
  @WithMockUser(username = "usr", roles = { "USER" })
  void testAddFoodEntryWithInvalidDate() throws Exception {
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&date=invalid-date"; // Invalid date format

    mockMvc.perform(post("/food/add")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .content(newFoodEntryUrlEncoded))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to invalid date format
  }

  @Test
  public void testListFoodEntriesByInterval_ValidDates() {
    // Arrange (Set up mock behavior)
    LocalDate startDate = LocalDate.of(2023, 10, 20);
    LocalDate endDate = LocalDate.of(2023, 10, 25);
    LocalDateTime start = startDate.atStartOfDay();
    LocalDateTime end = endDate.plusDays(1).atStartOfDay();
    List<FoodEntry> filteredEntries = new ArrayList<>();

    when(foodEntryService.getByDate(start, end)).thenReturn(filteredEntries);

    // Act (Call the controller method)
    String viewName = controller.listFoodEntriesByInterval(startDate, endDate, model);

    // Assert (Verify results)
    assertEquals("food-interval", viewName); // Check the view name

    // Verify model attributes
    verify(model).addAttribute("foodEntries", filteredEntries);
    verify(model).addAttribute("startDate", startDate.toString());
    verify(model).addAttribute("endDate", endDate.toString());
  }

  @Test
  public void testListFoodEntriesByInterval_StartDateAfterEndDate() {
    // Arrange (Set up mock behavior)
    LocalDate startDate = LocalDate.of(2023, 10, 25);
    LocalDate endDate = LocalDate.of(2023, 10, 20);

    // Act (Call the controller method)
    String viewName = controller.listFoodEntriesByInterval(startDate, endDate, model);

    // Assert (Verify redirect)
    assertEquals("redirect:/food", viewName); // Check the redirect

    // No need to verify model attributes as redirect happens first
  }
}