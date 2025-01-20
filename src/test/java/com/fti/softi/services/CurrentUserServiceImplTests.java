package com.fti.softi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;

class CurrentUserServiceImplTests {
  
  private final UserRepository userRepository = mock(UserRepository.class);
  private final CurrentUserService currentUserService = new CurrentUserServiceImpl(userRepository);
  
  @Test
  void testGetCurrentUser() {
    // Mocking a User object
    User mockUser = User.builder()
        .id(1L)
        .email("testuser@email.com")
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
    Optional<User> result = currentUserService.getCurrentUser();


    // Assertions
    assert(result.isPresent());
    User resultUser = result.get();
    assertEquals("testuser@email.com", resultUser.getEmail());
    assertEquals(1L, resultUser.getId());
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
  void testGetCurrentUserWhenNotAuthenticated() {
    SecurityContextHolder.clearContext();
    Optional<User> result = currentUserService.getCurrentUser();

    assertFalse(result.isPresent()); // Expecting null if not authenticated
  }

  @Test
  // this reports problems, cause user is null and we're trying to get an attribute from a null value
  // for security, maybe I'll do the check first
  void testGetCurrentUserIdWhenNotAuthenticated() {
    SecurityContextHolder.clearContext();
    Long userId = currentUserService.getCurrentUserId();

    assertNull(userId); // Expecting null if not authenticated
  }
}
