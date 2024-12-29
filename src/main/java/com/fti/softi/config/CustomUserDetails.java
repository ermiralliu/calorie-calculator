package com.fti.softi.config;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fti.softi.models.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User{
	private static final long serialVersionUID = 4280594317366620191L;
	private long user_id;

	public CustomUserDetails(User user) {
    super( user.getEmail(), 
      user.getPassword(),
      user.getRoles().stream()
      .map(role -> new SimpleGrantedAuthority("ROLE_"+ role.getName()))
      .collect(Collectors.toSet())
    );
    this.user_id = user.getId();
	}
  
  public long getId(){
    return user_id;
  }
}
