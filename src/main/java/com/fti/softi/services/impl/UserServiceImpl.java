package com.fti.softi.services.impl;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fti.softi.models.User;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  public final UserRepository userRepository;
  public final RoleRepository roleRepository;

  @Override
  public boolean userExists(String email){
    return userRepository.findByEmail(email) != null;
  }

  @Override
  public User addUser(User newUser) {
    var userRoles = Set.of(roleRepository.findByName("USER"));
		var encryptor = new BCryptPasswordEncoder();
    newUser.setRoles(userRoles);
    newUser.setPassword(encryptor.encode(newUser.getPassword()));

    User inserted = userRepository.save(newUser);

    var redirUser = User.builder()
    .id(inserted.getId())
    .name(inserted.getName())
    .email(inserted.getEmail())
    .roles(Set.of())
    .build();

    return redirUser;
  }
  
}
