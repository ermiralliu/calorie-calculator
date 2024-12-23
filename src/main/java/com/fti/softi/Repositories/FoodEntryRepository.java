package com.fti.softi.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.Models.FoodEntry;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserId(Long userId); 
}
