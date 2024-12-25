package com.fti.softi.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.models.User;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

	public CurrentUserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails))
			return null;
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal(); // we getUser
		User user = userDetails.getUser();
		return user;
	}
	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

}
