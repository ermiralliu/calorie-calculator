package com.fti.softi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.Models.FoodEntry;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    // Custom query method examples can be added here
}
