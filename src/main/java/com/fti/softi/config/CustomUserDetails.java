package com.fti.softi.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fti.softi.models.User;

public class CustomUserDetails implements UserDetails{
	private static final long serialVersionUID = 4280594317366620191L;
	
	private User user;
	Collection<? extends GrantedAuthority> authorities;
	
	public CustomUserDetails(User user) {
		this.user = user;
		System.out.println(user.getRoles().stream().map(role->role.getName()).collect(Collectors.toSet()));
		this.authorities = user.getRoles().stream()
      .map(role -> {
      		System.out.println(role.getName());
      		return new SimpleGrantedAuthority("ROLE_"+ role.getName());
      	})
      .collect(Collectors.toSet());
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
