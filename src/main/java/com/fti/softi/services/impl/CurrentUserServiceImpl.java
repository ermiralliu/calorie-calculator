package com.fti.softi.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fti.softi.config.CustomUserDetails;
import com.fti.softi.services.CurrentUserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

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
