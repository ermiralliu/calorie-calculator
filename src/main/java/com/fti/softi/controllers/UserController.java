package com.fti.softi.controllers;

import java.util.HashSet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fti.softi.models.Role;
import com.fti.softi.models.User;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository; 
	private final RoleRepository roleRepository; 
	
	@GetMapping("/register")
	public String getInsertView() {
		return "register";
	}
	
	@PostMapping("/register")
	public String postUser(
			@RequestParam("name") String name,
			@RequestParam("username")String username,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("calorieLimit") Integer calorieLimit,

			RedirectAttributes redirectAttributes
	) {
		if(userRepository.findByEmail(email) != null) {
			redirectAttributes.addFlashAttribute("message","Email is already in use");
			return "redirect:/user/register";
		}
		
		var userRoles = new HashSet<Role>();
		userRoles.add(roleRepository.findByName("USER"));
		var encryptor = new BCryptPasswordEncoder();
		var user = User.builder()
				.name(name)
				.username(username)
				.email(email)
				.password(encryptor.encode(password))
				.dailyCalorieLimit(calorieLimit)
				.roles(userRoles)
				.build();
		
		var finalUser = userRepository.save(user);	//returns a copy that contains an id
		redirectAttributes.addFlashAttribute("user", finalUser);
		return "redirect:/login";	// Okay good, POST-Redirect-GET design DONE
	}

	@PutMapping
	public String editUser(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			@RequestParam("calorieLimit")String calorieLimit,
			RedirectAttributes redirectAttributes
		) {
		return "redirect:/user";

	}
}
