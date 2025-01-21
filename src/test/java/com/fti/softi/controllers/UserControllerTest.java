package com.fti.softi.controllers;

import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.services.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private CurrentUserService currentUserService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  void testGetInsertView() throws Exception {
    mockMvc.perform(get("/user/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("user/registerForm"));
  }

  @Test
  void testPostUser_Success() throws Exception {
    String name = "John Doe";
    String username = "johndoe";
    String email = "john.doe@example.com";
    String password = "password";
    Integer calorieLimit = 2000;

    User user = User.builder()
            .name(name)
            .username(username)
            .email(email)
            .password(password)
            .dailyCalorieLimit(calorieLimit)
            .build();

    when(userRepository.findByEmail(email)).thenReturn(null);
    when(userRepository.save(any(User.class))).thenReturn(user);

    mockMvc.perform(post("/user/register")
                    .param("name", name)
                    .param("username", username)
                    .param("email", email)
                    .param("password", password)
                    .param("calorieLimit", calorieLimit.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testPostUser_EmailAlreadyExists() throws Exception {
    String email = "existing@example.com";
    when(userRepository.findByEmail(email)).thenReturn(new User());

    mockMvc.perform(post("/user/register")
                    .param("email", email))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user/register"));
  }


  // @Test
  // void testGetUserView_UserFound() throws Exception {
  //   User user = User.builder()
  //           .name("John Doe")
  //           .username("johndoe")
  //           .email("john.doe@example.com")
  //           .dailyCalorieLimit(2000)
  //           .build();

  //   when(currentUserService.getCurrentUser()).thenReturn(Optional.of(user));

  //   mockMvc.perform(get("/user"))
  //           .andExpect(status().isOk())
  //           .andExpect(view().name("user"))
  //           .andExpect(model().attribute("user", user))
  //           .andExpect(model().attribute("dailyCalorieLimit", user.getDailyCalorieLimit()))
  //           .andExpect(model().attribute("username", user.getUsername()));
  // }
  // @Test
  // void testGetUserView_UserNotFound() throws Exception {
  //   when(currentUserService.getCurrentUser()).thenReturn(java.util.Optional.empty());

  //   mockMvc.perform(get("/user"))
  //           .andExpect(status().isOk())
  //           .andExpect(view().name("error"))
  //           .andExpect(model().attribute("message", "User not found."));
  // }

  // @Test
  // void testUpdateUser_Success() throws Exception {
  //   String updatedName = "John Updated";
  //   String updatedPassword = "newpassword";
  //   String updatedCalorieLimit = "2500";

  //   User user = User.builder()
  //           .name("John Doe")
  //           .username("johndoe")
  //           .email("john.doe@example.com")
  //           .build();

  //   when(currentUserService.getCurrentUser()).thenReturn(java.util.Optional.of(user));
  //   when(userRepository.save(any(User.class))).thenReturn(user);

  //   mockMvc.perform(put("/user")
  //                   .param("name", updatedName)
  //                   .param("password", updatedPassword)
  //                   .param("calorieLimit", updatedCalorieLimit))
  //           .andExpect(status().is3xxRedirection())
  //           .andExpect(redirectedUrl("/user"));

  //   verify(userRepository, times(1)).save(any(User.class)); // Verify that the user is updated
  // }

  // @Test
  // void testUpdateUser_UserNotFound() throws Exception {
  //   when(currentUserService.getCurrentUser()).thenReturn(java.util.Optional.empty());

  //   mockMvc.perform(put("/user"))
  //           .andExpect(status().is3xxRedirection())
  //           .andExpect(redirectedUrl("/error"));
  // }
}
