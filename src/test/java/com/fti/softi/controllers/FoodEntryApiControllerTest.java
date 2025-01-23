package com.fti.softi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.services.FoodEntryService;

@ExtendWith(MockitoExtension.class)
public class FoodEntryApiControllerTest {

  @Mock
  private FoodEntryService foodEntryService;

  @InjectMocks
  private FoodEntryApiController controller;

  @Test
  @WithMockUser(username = "usr", roles = {"USER"})
  public void testGetFoodPage_FirstPage() throws Exception {
      int page = 0; // First page
      int size = 20;
      Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
      Page<FoodEntry> expectedPage = new PageImpl<>(Collections.emptyList());

      Mockito.when(foodEntryService.getFoodPageForUser(pageable)).thenReturn(expectedPage);

      ResponseEntity<Page<FoodEntry>> response = controller.getFoodPage(page);

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(expectedPage, response.getBody()); // Compare the Page objects
  }
}