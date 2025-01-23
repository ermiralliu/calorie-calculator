package com.fti.softi.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fti.softi.models.Role;
import com.fti.softi.models.User;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;
    private User newUser;

    @BeforeEach
    public void setUp() {
        existingUser = User.builder()
          .email("test@example.com")
          .password("password")
          .build();
        newUser = User.builder()
          .email("newuserTest@example.com")
          .password("newpassword")
          .build();
    }

    @Test
    public void testUserExists_ExistingEmail() {
        Mockito.when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
        assertTrue(userService.userExists(existingUser.getEmail()));
    }

    @Test
    public void testUserExists_NonexistentEmail() {
        Mockito.when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);

        assertFalse(userService.userExists(newUser.getEmail()));
    }

    @Test
    public void testAddUser_Success() {
        Set<Role> userRoles = Set.of(new Role("USER"));
        Mockito.when(roleRepository.findByName("USER")).thenReturn(userRoles.iterator().next());
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.addUser(newUser);

        // Assert properties of the returned user (excluding password)
        assertEquals(newUser.getId(), savedUser.getId());
        assertEquals(newUser.getName(), savedUser.getName());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
        assertTrue(savedUser.getRoles().isEmpty()); // Redacted roles in returned user
    }
}