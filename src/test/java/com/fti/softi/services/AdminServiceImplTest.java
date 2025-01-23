package com.fti.softi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.services.impl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

  @Mock
  private FoodEntryRepository foodEntryRepository;

  @InjectMocks
  private AdminServiceImpl adminService;

  private FoodEntry foodEntry;

  @BeforeEach
  public void setUp() {
    foodEntry = FoodEntry.builder()
        .id(1L)
        .name("Test Food")
        .description("Description")
        .price(10.0)
        .calories(200)
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  public void testFoodIsPresent_ExistingId() {
    Mockito.when(foodEntryRepository.findById(1L)).thenReturn(Optional.of(foodEntry));

    assertTrue(adminService.foodIsPresent(1L));
  }

  @Test
  public void testFoodIsPresent_NonexistentId() {
    Mockito.when(foodEntryRepository.findById(2L)).thenReturn(Optional.empty());

    assertFalse(adminService.foodIsPresent(2L));
  }

  @Test
  public void testDeleteFoodEntryById_Success() {
    adminService.deleteFoodEntryById(1L);
    Mockito.verify(foodEntryRepository).deleteById(1L);
  }

  @Test
  public void testUpdateFoodEntry_ExistingFoodEntry() {
    Mockito.when(foodEntryRepository.findById(1L)).thenReturn(Optional.of(foodEntry));

    String newName = "Updated Food";
    String newDescription = "New Description";
    Double newPrice = 12.5;
    Integer newCalories = 250;
    LocalDateTime newCreatedAt = LocalDateTime.now().plusHours(1);

    adminService.updateFoodEntry(1L, newName, newDescription, newPrice, newCalories, newCreatedAt);

    foodEntry.setName(newName);
    foodEntry.setDescription(newDescription);
    foodEntry.setPrice(newPrice);
    foodEntry.setCalories(newCalories);
    foodEntry.setCreatedAt(newCreatedAt);

    Mockito.verify(foodEntryRepository).save(foodEntry);
  }

  @Test
  public void testUpdateFoodEntry_NonexistentFoodEntry() {
    Mockito.when(foodEntryRepository.findById(2L)).thenReturn(Optional.empty());
    adminService.updateFoodEntry(2L, "New Name", null, null, null, null);
  }

  @Test
  public void testGetWeeklyEntryComparison() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = now.minusDays(7);
    LocalDateTime previousWeekStart = startOfWeek.minusDays(7);

    Mockito.when(foodEntryRepository.countByCreatedAtBetween(startOfWeek, now)).thenReturn(10L);
    Mockito.when(foodEntryRepository.countByCreatedAtBetween(previousWeekStart, startOfWeek)).thenReturn(5L);

    LinkedHashMap<String, Integer> expected = new LinkedHashMap<>();
    expected.put("Last 7 Days", 10);
    expected.put("Previous Week", 5);

    assertEquals(expected, adminService.getWeeklyEntryComparison());
  }

  @Test
  public void testAverageDailyCaloriesLast7Days() {
    // Prepare mock data (replace with your actual data retrieval logic)
    Object[] obj = new Object[]{"Test User", 150.0};
    List<Object[]> averageCaloriesData = new ArrayList<Object[]>();
    averageCaloriesData.add(obj);
    Mockito.when(foodEntryRepository.findAverageCaloriesPerUser()).thenReturn(averageCaloriesData);

    // Expected CalorieDto objects
    List<CalorieDto> expected = List.of(new CalorieDto("Test User", "150.00"));

    // Call the method and assert the result
    List<CalorieDto> actual = adminService.averageDailyCaloriesLast7Days();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetUsersExceedingMonthlyExpenditureLimit() {
    // Set monthly limit
    double monthlyLimit = 100.0;

    // Prepare mock data (replace with your actual data retrieval logic)
    LocalDateTime monthStart = LocalDateTime.of(2024, 12, 01, 0, 0); // Example start of previous month (December 2024)
    LocalDateTime monthEnd = LocalDateTime.of(2025, 01, 01, 0, 0); // Example start of current month (January 2025)
    List<FoodEntry> lastMonthEntries = new ArrayList<FoodEntry>();
    User mockUser = new User();
    mockUser.setEmail("bosi ktu");
    for(int i= 0; i<29; i++){
      FoodEntry entry = FoodEntry.builder()
        .name("Entry "+i)
        .description("Description")
        .calories(i)
        .user(mockUser)
        .price( (double) i)
        .createdAt(monthStart.plusDays(i))
        .build();
      lastMonthEntries.add(entry);
    }
    Mockito.when(foodEntryRepository.findByCreatedAtBetween(monthStart, monthEnd)).thenReturn(lastMonthEntries);

    // Expected user email (user who created foodEntry2)
    List<String> expected = List.of(lastMonthEntries.get(0).getUser().getEmail()); // Replace with actual logic to get user email

    // Call the method and assert the result
    List<String> actual = adminService.getUsersExceedingMonthlyExpenditureLimit(monthlyLimit);
    assertEquals(expected, actual);
  }
}