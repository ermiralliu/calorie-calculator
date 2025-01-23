package com.fti.softi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;
// import com.fti.softi.services.FoodEntryService;

@SpringBootTest
// @DataJpaTest // this is supposed to be done with this annotation and not SBT;
//however I'm finding it difficult to configure
class FoodEntryRepositoryTests {

    @Autowired
    private FoodEntryRepository foodEntryRepository;
    @Autowired
    private UserRepository userRepository;
    // @Autowired
    // private FoodEntryService foodEntryService;

    private User testUser;
    // @Autowired
    // private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Set up a user before each test to avoid duplication
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@email.com")
                .roles(Set.of()) // You can add roles if needed
                .build();
        userRepository.save(testUser);
    }

    @Test
    void testFindByUserId() {
        FoodEntry foodEntry = FoodEntry.builder()
                .createdAt(LocalDateTime.now())
                .name("Apple")
                .calories(95)
                .price(0.99)
                .user(testUser)
                .build();
        foodEntryRepository.save(foodEntry);

        List<FoodEntry> foodEntries = foodEntryRepository.findByUserId(testUser.getId());
        List<String> names = foodEntries.stream()
          .map(FoodEntry::getName)
          .toList();
        assertFalse(foodEntries.isEmpty());
        assertTrue(names.contains("Apple"));
        // assertEquals("Apple", foodEntries.get(0).getName());
    }

    @Test
    void testFindById() {
        FoodEntry foodEntry = FoodEntry.builder()
                .createdAt(LocalDateTime.now())
                .name("Orange")
                .calories(80)
                .price(1.5)
                .user(testUser)
                .build();
        foodEntryRepository.save(foodEntry);

        FoodEntry foundFoodEntry = foodEntryRepository.findById(foodEntry.getId()).orElse(null);
        assertEquals(foodEntry.getName(), foundFoodEntry.getName());
        assertEquals(foodEntry.getCalories(), foundFoodEntry.getCalories());
    }

    @Test
    void testDeleteById() {
        FoodEntry foodEntry = FoodEntry.builder()
                .createdAt(LocalDateTime.now())
                .name("Grapes")
                .calories(70)
                .price(2.0)
                .user(testUser)
                .build();
        foodEntryRepository.save(foodEntry);

        Long id = foodEntry.getId();
        foodEntryRepository.deleteById(id);

        assertFalse(foodEntryRepository.findById(id).isPresent(), "Food entry should be deleted.");
    }

}
