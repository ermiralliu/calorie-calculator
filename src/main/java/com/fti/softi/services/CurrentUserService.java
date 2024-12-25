package com.fti.softi.services;

import com.fti.softi.models.User;

public interface CurrentUserService {
	User getCurrentUser();
	Long getCurrentUserId();
}
