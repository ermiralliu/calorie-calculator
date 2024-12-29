package com.fti.softi.services;

import java.util.Optional;

import com.fti.softi.models.User;

public interface CurrentUserService {
	Optional<User> getCurrentUser();
	Long getCurrentUserId();
}
