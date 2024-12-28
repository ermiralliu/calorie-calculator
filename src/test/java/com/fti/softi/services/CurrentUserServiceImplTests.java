package com.fti.softi.services;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class CurrentUserServiceImplTests {
  // @Autowired
  private CurrentUserService currentUserService = new CurrentUserServiceImpl();

  @Test
  void testGetCurrentUser() {
    // Mocking a User object
    User mockUser = User.builder()
        .id(1L)
        .email("testuser@email.com")
        .build();

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
    assertEquals("testuser@email.com", result.getEmail());
    assertEquals(1L, result.getId());
  }

  @Test
  void testGetCurrentUserId() {
    // Mocking a User object
    User mockUser = User.builder()
        .email("testuser@email.com")
        .id(1L)
        .build();

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

  @Test
  void testGetCurrentUserWhenNotAuthenticated() {
    SecurityContextHolder.clearContext();

    User result = currentUserService.getCurrentUser();

    assertNull(result); // Expecting null if not authenticated
  }

  @Test
  void testGetCurrentUserIdWhenNotAuthenticated() {
    SecurityContextHolder.clearContext();

    Long userId = currentUserService.getCurrentUserId();

    assertNull(userId); // Expecting null if not authenticated
  }
}
