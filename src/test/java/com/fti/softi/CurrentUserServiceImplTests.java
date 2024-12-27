package com.fti.softi;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.CurrentUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CurrentUserServiceImplTests {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CurrentUserServiceImpl currentUserService = new CurrentUserServiceImpl();

    @Test
    void testGetCurrentUser() {
        // Mocking a User object
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setId(1L);  // Set an ID for the user

        // Mocking CustomUserDetails
        CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
        when(mockUserDetails.getUser()).thenReturn(mockUser);

        // Mocking Authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);

        // Setting the mocked authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Calling the method under test
        User result = currentUserService.getCurrentUser();

        // Assertions
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetCurrentUserId() {
        // Mocking a User object
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setId(1L);  // Set an ID for the user

        // Mocking CustomUserDetails
        CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
        when(mockUserDetails.getUser()).thenReturn(mockUser);

        // Mocking Authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);

        // Setting the mocked authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Calling the method under test
        Long userId = currentUserService.getCurrentUserId();

        // Assertions
        assertNotNull(userId);
        assertEquals(1L, userId);
    }
}
