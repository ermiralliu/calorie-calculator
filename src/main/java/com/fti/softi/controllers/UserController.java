package com.fti.softi.controllers;

import java.util.Set;

import com.fti.softi.models.User;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			@RequestParam("username") String username,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam(name = "calorieLimit", required = false) Integer calorieLimit,
			RedirectAttributes redirectAttributes
	) {
		if (userRepository.findByEmail(email) != null) {
			redirectAttributes.addFlashAttribute("message", "Email is already in use");
			return "redirect:/user/register";
		}
    calorieLimit = 2500;

		var userRoles = Set.of(roleRepository.findByName("USER"));
		var encryptor = new BCryptPasswordEncoder();
		var user = User.builder()
				.name(name)
				.username(username)
				.email(email)
				.password(encryptor.encode(password))
				.dailyCalorieLimit(calorieLimit)
				.roles(userRoles)
				.build();

		var finalUser = userRepository.save(user);
		finalUser.setRoles(Set.of());
		redirectAttributes.addFlashAttribute("user", finalUser);

		return "redirect:/login";
	}

	// @PutMapping
	// public String updateUser(
	// 		@RequestParam(name = "name", required = false) String name,
	// 		@RequestParam(name = "password", required = false) String password,
	// 		@RequestParam(name = "calorieLimit", required = false) String calorieLimit,
	// 		RedirectAttributes redirectAttributes
	// ) {
	// 	Optional<User> optionalUser = currentUserService.getCurrentUser();

	// 	if (optionalUser.isPresent()) {
	// 		User user = optionalUser.get();

	// 		if (name != null) {
	// 			user.setName(name);
	// 		}
	// 		if (password != null) {
	// 			var encryptor = new BCryptPasswordEncoder();
	// 			user.setPassword(encryptor.encode(password));
	// 		}
	// 		if (calorieLimit != null) {
	// 			user.setDailyCalorieLimit(Integer.parseInt(calorieLimit));
	// 		}

	// 		userRepository.save(user);
	// 	} else {
	// 		redirectAttributes.addFlashAttribute("message", "User not found.");
	// 		return "redirect:/error";
	// 	}

	// 	return "redirect:/user";
	// }

	// @GetMapping
	// public String getUserView(Model model) {
	// 	Optional<User> user = currentUserService.getCurrentUserId();

	// 	if (user.isEmpty()) {
	// 		model.addAttribute("message", "User not found.");
	// 		return "error";
	// 	}

	// 	model.addAttribute("user", user.get());
	// 	model.addAttribute("dailyCalorieLimit", user.get().getDailyCalorieLimit());
	// 	model.addAttribute("username", user.get().getUsername());

	// 	return "user";
	// }
}
