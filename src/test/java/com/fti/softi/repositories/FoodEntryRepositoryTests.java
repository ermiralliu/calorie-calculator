package com.fti.softi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;

@SpringBootTest
// @DataJpaTest // this is supposed to be done with this annotation and not SBT; 
//however I'm finding it difficult to configure
class FoodEntryRepositoryTests {

    @Autowired
    private FoodEntryRepository foodEntryRepository;
    @Autowired
    private UserRepository userRepository;
    // @Autowired
    // private RoleRepository roleRepository;

    @Test
    void testFindAll() {
      // Role role = new Role(1L, "USER", null);
      // roleRepository.save(role);

      User user = User.builder()  // I should make a helper class, which contains these dummies cause it's getting tiring
        .id(1L)
        .name("user")
        .email("user@email.com")
        .roles(Set.of())
        .build();
      userRepository.save(user);

      FoodEntry foodEntry = FoodEntry.builder()
        .id(1L) // but main doesn't need one ?!?!?!, there's probably some configuration missing
        .createdAt(LocalDateTime.now())
        .name("Banana")
        .calories(105)
        .price(31.23)
        .user(user)
        .build();
      foodEntryRepository.save(foodEntry);

      List<FoodEntry> foodEntries = foodEntryRepository.findAll();
      assertFalse(foodEntries.isEmpty());
      assertEquals("Banana", foodEntries.get(0).getName());
    }
}
