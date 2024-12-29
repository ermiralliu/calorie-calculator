package com.fti.softi.services;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
  private final UserRepository userRepository;

	@Override
	public Optional<User> getCurrentUser() {
		Long user_id = getCurrentUserId();
    if(user_id == null)
      return Optional.empty();
		return userRepository.findById(user_id);
	}
  @Override
	public Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails))
			return null;
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal(); // we getUser
		long user_id = userDetails.getId();
		return user_id;
	}

}
