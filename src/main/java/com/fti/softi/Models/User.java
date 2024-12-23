package com.fti.softi.Models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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