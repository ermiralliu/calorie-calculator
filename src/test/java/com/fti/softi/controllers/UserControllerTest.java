package com.fti.softi.controllers;

import com.fti.softi.models.Role;
import com.fti.softi.models.User;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // Test for user registration view
    @Test
    void testGetInsertView() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    // Test for registering a user
    @Test
    void testPostUser() throws Exception {
        // Arrange: mock role and user repository behavior
        Role userRole = new Role("USER");
        when(roleRepository.findByName("USER")).thenReturn(userRole);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);  // Simulate email not in use

        String name = "John Doe";
        String email = "test@example.com";
        String password = "password123";

        // POST request and verify the redirect
        mockMvc.perform(post("/user/register")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/login"));

        // Verify the user is saved in the repository
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test for user registration with an already existing email
    @Test
    void testPostUserWithExistingEmail() throws Exception {
        // mock user repository to simulate an existing user with the same email
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());

        // POST request and verify the redirect
        mockMvc.perform(post("/user/register")
                        .param("name", "John Doe")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/user/register"))
                .andExpect(model().attributeExists("message"));
    }

    // Test for editing a user (this can be expanded as needed)
    @Test
    void testEditUser() throws Exception {
        // PUT request and verify the redirect
        mockMvc.perform(put("/user")
                        .param("name", "Updated Name")
                        .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/user"));

    }
}
