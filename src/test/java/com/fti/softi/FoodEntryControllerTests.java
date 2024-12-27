package com.fti.softi;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.repositories.FoodEntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FoodEntryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodEntryRepository foodEntryRepository;

    @Test
    void testAddFoodEntry() throws Exception {
        String newFoodEntryJson = """
            {
                "name": "Apple",
                "calories": 95,
                "date": "2024-12-27"
            }
        """;

        mockMvc.perform(post("/food-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodEntryJson))
                .andExpect(status().isCreated());
    }
}
