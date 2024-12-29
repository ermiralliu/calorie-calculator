package com.fti.softi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MainControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(new MainController()).build();
  }

  @Test
  void testGetWelcome() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/food"));
    ;
  }
}
