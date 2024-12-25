package com.fti.softi.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final Long id;
	
	@Column(unique=true)
	private final String email;
	
	private String name; 
	
	private String password;

	@ManyToMany
	@JoinTable (
		name = "user_role",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;
	
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<FoodEntry> foodEntries;
  // fetchType lazy only fetches when the foodEntries are accessed for the first time
  // which avoids performance issues
	
	public User() { // This isn't really used, but the app displays an error otherwise
		id = null;
		email = null;
	}
	
	public User(String name, String email, String password) { // Parameterized constructor 
		this.id = null;
		this.name = name; 
		this.email = email;
		this.password = password;
		this.roles = null;
		this.foodEntries = null;
	}
}