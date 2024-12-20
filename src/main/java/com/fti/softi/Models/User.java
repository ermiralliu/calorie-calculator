package com.fti.softi.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	public User() { // This isn't really used, but the app displays an error otherwise
		id = null;
		email = null;
	}
	
	public User(String name, String email, String password) { // Parameterized constructor 
		this.id = null;
		this.name = name; 
		this.email = email;
		this.password = password;
	}
}