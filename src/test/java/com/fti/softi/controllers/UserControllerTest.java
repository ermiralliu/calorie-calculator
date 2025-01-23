package com.fti.softi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fti.softi.models.User;
import com.fti.softi.services.UserService;
import com.fti.softi.testConfig.ThymeleafTestConfig;

@Import(ThymeleafTestConfig.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  void testPostUser_Success() throws Exception {
    String name = "Test User";
    String email = "test@example.com";
    String password = "password";

    when(userService.userExists(email)).thenReturn(false);

    User savedUser = User.builder()
      .name(name)
      .email(email)
      .password("encodedPassword")
      .build();
    when(userService.addUser(any(User.class))).thenReturn(savedUser);

    mockMvc.perform(post("/user/register")
        .param("name", name)
        .param("email", email)
        .param("password", password))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));

    verify(userService, times(1)).addUser(any(User.class));

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userService).addUser(userCaptor.capture());
    User capturedUser = userCaptor.getValue();
    assertEquals(name, capturedUser.getName());
    assertEquals(email, capturedUser.getEmail());
    // Do not check the password directly as it is encoded
  }

  @Test
  void testPostUser_EmailAlreadyExists() throws Exception {
    String email = "existing@example.com";
    when(userService.userExists(email)).thenReturn(true);

    mockMvc.perform(post("/user/register")
        .param("name", "name")
        .param("email", email)
        .param("password", ""))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/register"));
  }
}
