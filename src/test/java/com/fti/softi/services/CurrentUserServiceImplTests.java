package com.fti.softi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;
import com.fti.softi.services.impl.CurrentUserServiceImpl;

class CurrentUserServiceImplTests {
  
  // private final UserRepository userRepository = mock(UserRepository.class);
  private final CurrentUserService currentUserService = new CurrentUserServiceImpl();

  @Test
  void testGetCurrentUserId() {
    // Mocking a User object
    User mockUser = User.builder()
        .email("testuser@email.com")
        .id(1L)
        .build();

    // Mocking CustomUserDetails
    CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
    when(mockUserDetails.getId()).thenReturn(mockUser.getId());

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

  @Test
  void testGetCurrentUserIdWhenNotAuthenticated() {
    SecurityContextHolder.clearContext();
    Long userId = currentUserService.getCurrentUserId();

    assertNull(userId); // Expecting null if not authenticated
  }
}
