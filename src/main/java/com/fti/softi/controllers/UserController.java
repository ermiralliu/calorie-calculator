package com.fti.softi.controllers;

import com.fti.softi.models.User;
import com.fti.softi.services.UserService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

	@PostMapping("/register")
	public String postUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam(name = "calorieLimit", required = false) Integer calorieLimit,
			RedirectAttributes redirectAttributes
	) {
		if (userService.userExists(email)) {
			redirectAttributes.addFlashAttribute("message", "Email is already in use");
			return "redirect:/register";
		}
    calorieLimit = 2500;

		var user = User.builder()
				.name(name)
				.email(email)
				.password(password)
				.build();

		var redirUser = userService.addUser(user);

		redirectAttributes.addFlashAttribute("user", redirUser);

		return "redirect:/login";
	}

}
