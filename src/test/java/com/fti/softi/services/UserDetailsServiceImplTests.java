package com.fti.softi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.impl.UserDetailsServiceImpl;

class UserDetailsServiceImplTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    void testLoadUserByUsername() {

        User mockUser = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .password("encryptedPassword")
                .roles(Set.of())
                .build();

        when(userRepository.findByEmail("testuser@email.com")).thenReturn(mockUser);

        // Call the method under test
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@email.com");

        assertNotNull(userDetails);
        assertEquals("testuser@email.com", userDetails.getUsername());
        assertEquals("encryptedPassword", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsernameThrowsExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@email.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@email.com");
        });
    }
}
