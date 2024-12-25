package com.fti.softi.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fti.softi.models.User;

public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 4280594317366620191L;
	
	private User user;
	Collection<? extends GrantedAuthority> authorities;
	
	public CustomUserDetails(User user) {
		this.user = user;
		this.authorities = user.getRoles().stream()
      .map(role -> new SimpleGrantedAuthority(role.getName()))
      .collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	public User getUser() {
		return user;
	}

}
