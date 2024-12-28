package com.fti.softi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
  void testAdminAccess() throws Exception {
    mockMvc.perform(get("/admin"))
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testUnauthorizedAccess() throws Exception {
    mockMvc.perform(get("/admin"))
      .andExpect(status().isForbidden());
  }
}
