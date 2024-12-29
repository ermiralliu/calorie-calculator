package com.fti.softi.services;

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
	public User getCurrentUser() {
		long user_id = getCurrentUserId();
		return userRepository.getReferenceById(user_id);
	}

	public Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails))
			return null;
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal(); // we getUser
		long user_id = userDetails.getId();
		return user_id;
	}

}
