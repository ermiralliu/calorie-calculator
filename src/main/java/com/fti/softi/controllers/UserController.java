package com.fti.softi.controllers;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fti.softi.models.User;
import com.fti.softi.repositories.UserRepository;


@Controller
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository; 

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping("/register")
	public String getInsertView() {
		return "register";
	}
	
	@PostMapping
	public String postUser(
			@RequestParam("name") String name, 
			@RequestParam("email") String email,
			@RequestParam("password") String password, 
			RedirectAttributes redirectAttributes
	) {
		if(userRepository.findByEmail(email) != null) {
			redirectAttributes.addAttribute("Email is already in use");
			return "redirect:/user/register";
		}
		var user = User.builder()
				.name(name)
				.email(email)
				.password(password)
				.build();
		
		var finalUser = userRepository.save(user);	//returns a copy that contains an id
		redirectAttributes.addFlashAttribute("user", finalUser);
		return "redirect:/login";	// Okay good, POST-Redirect-GET design DONE
	}

	@PutMapping
	public String editUser(
			@RequestParam("name") String name,
			@RequestParam("password") String password, 
			RedirectAttributes redirectAttributes
		) {
		return "redirect:/user";
	}
}
