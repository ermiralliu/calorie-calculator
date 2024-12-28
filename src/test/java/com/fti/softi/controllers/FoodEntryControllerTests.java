package com.fti.softi.controllers;

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

  @Test
  void testAddFoodEntry() throws Exception {
    // String newFoodEntryJson = """  // this was json, but we're not using json at all during this. We're using urlencoded
    //   {
    //     "name": "Apple",
    //     "calories": 95,
    //     "date": "2024-12-27"
    //   }
    // """;
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&date=2024-12-27";

    mockMvc.perform(post("/food/add")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .content(newFoodEntryUrlEncoded))
      .andExpect(status().isFound()); // we get 302 cause of redirect after adding
  }

  @Test
  void testAddFoodEntryWithMissingData() throws Exception {
    String newFoodEntryUrlEncoded = "name=&calories=95&description=Tasty&date=2024-12-27"; // Missing name

    mockMvc.perform(post("/food/add")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(newFoodEntryUrlEncoded))
            .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to validation
  }

  @Test
  void testAddFoodEntryWithInvalidDate() throws Exception {
    String newFoodEntryUrlEncoded = "name=Apple&calories=95&description=Tasty&date=invalid-date"; // Invalid date format

    mockMvc.perform(post("/food/add")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(newFoodEntryUrlEncoded))
            .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to invalid date format
  }

}
