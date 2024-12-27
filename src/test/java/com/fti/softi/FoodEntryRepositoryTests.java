package com.fti.softi;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FoodEntryRepositoryTests {

    @Autowired
    private FoodEntryRepository foodEntryRepository;

    @Test
    void testFindAll() {
        FoodEntry foodEntry = new FoodEntry();
        foodEntry.setName("Banana");
        foodEntry.setCalories(105);
        foodEntryRepository.save(foodEntry);

        List<FoodEntry> foodEntries = foodEntryRepository.findAll();
        assertFalse(foodEntries.isEmpty());
        assertEquals("Banana", foodEntries.get(0).getName());
    }
}
