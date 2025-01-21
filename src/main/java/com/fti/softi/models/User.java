package com.fti.softi.models;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "users")
public class User implements Serializable{
  
  private static final long serialVersionUID = 10L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final Long id;
	
	@Column(unique=true)
	private final String email;

	@Column(unique = true)
	private final String username;
	
	private String name; 
	
  @Builder.Default
  @JsonIgnore
	private String password = "";//helps with tests to have empty strings instead of null

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable (
		name = "user_role",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
  @Builder.Default
  @JsonBackReference // Prevents infinite recursion
	private Set<Role> roles = Set.of();
	
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonBackReference // Prevents infinite recursion
  private Set<FoodEntry> foodEntries;
  // fetchType lazy only fetches when the foodEntries are accessed for the first time
  // which avoids performance issues
  @Builder.Default
  private Integer dailyCalorieLimit = 2500;

	public User() { // This isn't really used, but the app displays an error otherwise
		id = null;
		email = null;
		username=null;
	}
}