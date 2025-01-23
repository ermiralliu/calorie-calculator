package com.fti.softi.services;

import com.fti.softi.models.User;

public interface UserService {
  boolean userExists(String email);
  User addUser(User newUser);
}
