package com.fti.softi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @WebMvcTest(MainController.class)
@SpringBootTest
class MainControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(new MainController()).build();
  }

  @Test
  void testHomeRedirect() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/food"));
  }

  @Test
  void testLoginView() throws Exception {
    mockMvc.perform(get("/login"))  // Perform a GET request to the "/login" URL
            .andExpect(status().isOk())  // Expect a status of 200 (OK)
            .andExpect(view().name("login"));  // Expect the view name to be "login"
  }

  @Test
  void testRegisterRedirect() throws Exception {
    mockMvc.perform(get("/register"))
            .andExpect(status().isOk());
  }
}
