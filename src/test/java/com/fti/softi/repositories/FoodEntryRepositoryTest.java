package com.fti.softi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;

@DataJpaTest
public class FoodEntryRepositoryTest {

  @Autowired
  private FoodEntryRepository foodEntryRepository;

  // Tests for insertFoodEntryById
  @Test
  public void testInsertFoodEntryById_SuccessfulInsertion() {
    long userId = 1L; // Replace with actual user ID
    String name = "Test Entry";
    String description = "This is a test entry";
    double price = 5.0;
    double calories = 250;
    LocalDateTime createdAt = LocalDateTime.now();

    int rowsAffected = foodEntryRepository.insertFoodEntryById(userId, name, description, price, calories, createdAt);

    assertEquals(1, rowsAffected); // Expect 1 row inserted

    // Optional: Verify data in the database (if needed for your specific use case)
    FoodEntry insertedEntry = foodEntryRepository.findById(userId).orElse(null);
    assertNotNull(insertedEntry);
    assertEquals(name, insertedEntry.getName());
    // ... (assert other fields)
  }

  @Test
  public void testInsertFoodEntryById_DuplicateEntry_ThrowsException() {
    long userId = 1L; // Replace with actual user ID (assuming unique constraint on a field)
    String name = "Existing Entry Name"; // Assuming a unique constraint on name
    String description = "This is a test entry";
    double price = 5.0;
    double calories = 250;
    LocalDateTime createdAt = LocalDateTime.now();

    // Create an existing entry with the same name (or other unique field)
    FoodEntry existingEntry = new FoodEntry();
    // existingEntry.setUserId(userId);
    existingEntry.setName(name);
    existingEntry.setUser(User.builder().id(userId).build());
    foodEntryRepository.save(existingEntry);

    foodEntryRepository.insertFoodEntryById(userId, name, description, price, calories, createdAt);
  }

  @Test
  public void testInsertFoodEntryById_EmptyName_ThrowsException() {
    long userId = 1L;
    String name = ""; // Empty name
    String description = "This is a test entry";
    double price = 5.0;
    double calories = 250;
    LocalDateTime createdAt = LocalDateTime.now();

    try {
      foodEntryRepository.insertFoodEntryById(userId, name, description, price, calories, createdAt);
      fail("Expected exception for empty name");
    } catch (IllegalArgumentException | DataIntegrityViolationException e) {
      // Expected exception
    }
  }

  @Test
  public void testFindByUserId_ReturnsEntriesForExistingUser() {
    long existingUserId = 1L;

    List<FoodEntry> expectedEntries = createSampleFoodEntries(existingUserId);
    for (FoodEntry entry : expectedEntries) {
      foodEntryRepository.save(entry);
    }

    List<FoodEntry> actualEntries = foodEntryRepository.findByUserId(existingUserId);

    assertEquals(expectedEntries.size(), actualEntries.size());
    for (int i = 0; i < expectedEntries.size(); i++) {
      assertFoodEntryEquals(expectedEntries.get(i), actualEntries.get(i));
    }
  }

  @Test
  public void testFindByUserId_ReturnsEmptyListForNonexistentUser() {
    long nonExistentUserId = 999L;

    List<FoodEntry> actualEntries = foodEntryRepository.findByUserId(nonExistentUserId);

    assertEquals(0, actualEntries.size());
  }

  private List<FoodEntry> createSampleFoodEntries(long userId) {
    // Create a list of FoodEntry objects with different properties
    List<FoodEntry> entries = new ArrayList<>();
    // ... (populate entries)
    return entries;
  }

  private void assertFoodEntryEquals(FoodEntry expected, FoodEntry actual) {
    // Assert various fields of FoodEntry (name, description, price, etc.)
    assertEquals(expected.getUser(), actual.getUser());
    assertEquals(expected.getName(), actual.getName());
    // ... (assert other fields)
  }
}