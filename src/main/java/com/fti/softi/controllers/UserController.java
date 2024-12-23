package com.fti.softi.controllers;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@GetMapping("/insert")
	public String getInsertView() {
//		var user2 = userRepository.findByEmail("ama@haha.com");
//		model.addAttribute("user", user2);
		return "user/insert";
	}
	@PostMapping("/insert")
	public String postUser(
			@RequestParam("name") String name, 
			@RequestParam("email") String email,
			@RequestParam("password") String password, 
			RedirectAttributes redirectAttributes
	) {
		var user = User.builder()
				.name(name)
				.email(email)
				.password(password)
				.build();
				
//		var user = new User(name, email, password);
//		System.out.println(user.);
		var finalUser = userRepository.save(user);	//returns a copy that contains an id
		redirectAttributes.addFlashAttribute("user", finalUser);
		return "redirect:/user/insert";	// Okay good, POST-Redirect-GET design DONE
	}

}
