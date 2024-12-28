package com.fti.softi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fti.softi.models.User;

class CustomUserDetailsTests {

    @Test
    void testGetUsername() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .roles(Set.of())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertEquals("testuser@email.com", customUserDetails.getUsername());
    }

    @Test
    void testGetAuthorities() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .roles(Set.of())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertNotNull(customUserDetails.getAuthorities()); // Should return authorities for the user
    }

    @Test
    void testGetPassword() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .roles(Set.of())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertNull(customUserDetails.getPassword()); // Assuming you don't store a password in this class
    }

    @Test
    void testIsAccountNonExpired() {
        User user = User.builder()
                .id(1L)
                .email("testuser@email.com")
                .roles(Set.of())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertTrue(customUserDetails.isAccountNonExpired()); // Default should be true
    }
}
