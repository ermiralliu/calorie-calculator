package com.fti.softi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.impl.FoodEntryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FoodEntryServiceImplTest {

  @Mock
  private FoodEntryRepository foodEntryRepository;
  
  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private FoodEntryServiceImpl foodEntryService;

  private List<FoodEntry> foodEntries;
  private long userId;
  private Pageable pageable;

  @BeforeEach
  public void setUp() {
    foodEntries = new ArrayList<>();
    userId = 1L;
    pageable = PageRequest.of(0, 10);
  }

  @Test
  public void testGetAllForUser() {
    Mockito.when(foodEntryRepository.findByUserId(userId)).thenReturn(foodEntries);
    Mockito.when(currentUserService.getCurrentUserId()).thenReturn(userId);
    List<FoodEntry> actualEntries = foodEntryService.getAllForUser();

    assertEquals(foodEntries, actualEntries);
  }

  @Test
  public void testFilterCurrentWeek_EmptyList() {
    List<FoodEntry> emptyList = new ArrayList<>();

    List<FoodEntry> filteredEntries = foodEntryService.filterCurrentWeek(emptyList);

    assertEquals(emptyList, filteredEntries);
  }

  @Test
  public void testFilterCurrentWeek_WithEntriesBeforeWeekStart() {
    LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
    FoodEntry entry1 = new FoodEntry(userId, null, "Entry 1", "Desc", 10.0, 200, weekStart.minusDays(2));
    FoodEntry entry2 = new FoodEntry(userId, null, "Entry 2", "Desc", 15.0, 300, weekStart.minusDays(1));
    foodEntries.addAll(Arrays.asList(entry1, entry2));

    List<FoodEntry> expected = Collections.emptyList();

    List<FoodEntry> filteredEntries = foodEntryService.filterCurrentWeek(foodEntries);

    assertEquals(expected, filteredEntries);
  }

  @Test
  public void testFilterCurrentWeek_WithEntriesDuringWeek() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime weekStart = now.minusDays(7);
    FoodEntry entry1 = new FoodEntry(23L, null, "Entry 1", "Desc", 10.0, 200, weekStart.plusDays(2));
    FoodEntry entry2 = new FoodEntry(44L, null, "Entry 2", "Desc", 15.0, 300, now.minusDays(1));
    foodEntries.addAll(Arrays.asList(entry1, entry2));

    List<FoodEntry> expected = Collections.singletonList(entry2);

    List<FoodEntry> filteredEntries = foodEntryService.filterCurrentWeek(foodEntries);

    assertEquals(expected, filteredEntries);
  }

  @Test
  public void testGetLastMonthForUser() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime monthStart = now.minusMonths(1).withDayOfMonth(1);
    Mockito.lenient().when(foodEntryRepository.findByUserIdAndDateRange(userId, monthStart, now))
        .thenReturn(foodEntries);

    List<FoodEntry> actualEntries = foodEntryService.getLastMonthForUser();

    assertEquals(foodEntries, actualEntries);
  }

  @Test
  public void testGetDailyCalories_NoEntriesAfterMidnight() {
    List<FoodEntry> emptyList = new ArrayList<>();

    int dailyCalories = foodEntryService.getDailyCalories(emptyList);

    assertEquals(0, dailyCalories);
  }

  // Existing tests are included for reference

  @Test
  public void testExpenditure_EmptyList() {
    List<FoodEntry> emptyList = new ArrayList<>();

    double expenditure = foodEntryService.getExpenditure(emptyList);

    assertEquals(0.0, expenditure);
  }

  @Test
  public void testExpenditure_WithEntries() {
    List<FoodEntry> entries = Arrays.asList(
      new FoodEntry(2L, null, "Entry 1", "Desc", 10.0, 80, LocalDateTime.now()),
      new FoodEntry(3L, null, "Entry 2", "Desc", 15.0, 75, LocalDateTime.now().plusDays(1)));

    double expectedExpenditure = entries.stream().mapToDouble(FoodEntry::getPrice).sum();

    double expenditure = foodEntryService.getExpenditure(entries);

    assertEquals(expectedExpenditure, expenditure);
  }

  @Test
  public void testInsertFoodEntry() {
    String name = "Test Entry";
    String description = "This is a test entry";
    double price = 5.0;
    double calories = 250;
    LocalDateTime createdAt = LocalDateTime.now();

    Mockito.lenient().when(foodEntryRepository.insertFoodEntryById(userId, name, description, price, calories, createdAt))
        .thenReturn(1);

    boolean success = foodEntryService.insertFoodEntry(name, description, price, calories, createdAt);

    assertTrue(success);
  }

  @Test
  public void testGetDaysAboveCalorieThreshold_EmptyList() {
    List<FoodEntry> emptyList = new ArrayList<>();
    int calorieThreshold = 100;

    LinkedHashMap<String, Integer> result = foodEntryService.getDaysAboveCalorieThreshold(emptyList, calorieThreshold);

    assertEquals(new LinkedHashMap<>(), result);
  }

  @Test
  public void testGetDaysAboveCalorieThreshold_NoDaysAboveThreshold() {
    List<FoodEntry> entries = Arrays.asList(
        new FoodEntry(2L, null, "Entry 1", "Desc", 10.0, 80, LocalDateTime.now()),
        new FoodEntry(3L, null, "Entry 2", "Desc", 15.0, 75, LocalDateTime.now().plusDays(1)));
    int calorieThreshold = 100;

    LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();

    LinkedHashMap<String, Integer> result = foodEntryService.getDaysAboveCalorieThreshold(entries, calorieThreshold);

    assertEquals(expected, result);
  }

  @Test
  public void testGetDaysAboveCalorieThreshold_WithDaysAboveThreshold() {
    List<FoodEntry> entries = Arrays.asList(
      new FoodEntry(2L, null, "Entry 1", "Desc", 10.0, 80, LocalDateTime.now()),
      new FoodEntry(3L, null, "Entry 2", "Desc", 15.0, 75, LocalDateTime.now().plusDays(1)));
    int calorieThreshold = 100;

    LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
    expected.put(entries.get(0).getDate().toString(), 120);
    expected.put(entries.get(1).getDate().toString(), 150);

    LinkedHashMap<String, Integer> result = foodEntryService.getDaysAboveCalorieThreshold(entries, calorieThreshold);

    assertEquals(expected, result);
  }

  @Test
  public void testGetFoodPageForUser_MockRepository() {
    List<FoodEntry> expectedEntries = new ArrayList<>();
    Page<FoodEntry> mockPage = new PageImpl<>(expectedEntries);
    Mockito.lenient().when(foodEntryRepository.findByUserId(userId, pageable)).thenReturn(mockPage);

    Page<FoodEntry> actualPage = foodEntryService.getFoodPageForUser(pageable);

    assertEquals(expectedEntries, actualPage.getContent());
  }

  @Test
  public void testGetByDate() {
    LocalDateTime start = LocalDateTime.now().minusDays(3);
    LocalDateTime end = LocalDateTime.now();
    Mockito.lenient().when(foodEntryRepository.findByUserIdAndDateRange(userId, start, end)).thenReturn(foodEntries);

    List<FoodEntry> actualEntries = foodEntryService.getByDate(start, end);

    assertEquals(foodEntries, actualEntries);
  }
}