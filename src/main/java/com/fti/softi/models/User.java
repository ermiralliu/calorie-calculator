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
	public final Long id;
	
	@Column(unique=true)
	public final String email;
	
	private String name; 
	
	private String password;

	private Boolean isAdmin;

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
		isAdmin = false;
	}
	
	public User(String name, String email, String password) { // Parameterized constructor 
		this.id = null;
		this.name = name; 
		this.email = email;
		this.password = password;
		this.isAdmin = false;
		this.foodEntries = null;
	}
}