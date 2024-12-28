package com.fti.softi.config;

import com.fti.softi.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTests {

    @Test
    void testGetUsername() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertEquals("testuser@email.com", customUserDetails.getUsername());
    }

    @Test
    void testGetAuthorities() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertNotNull(customUserDetails.getAuthorities()); // Should return authorities for the user
    }

    @Test
    void testGetPassword() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertNull(customUserDetails.getPassword()); // Assuming you don't store a password in this class
    }

    @Test
    void testIsAccountNonExpired() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertTrue(customUserDetails.isAccountNonExpired()); // Default should be true
    }
}
