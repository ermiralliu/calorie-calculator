package com.fti.softi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {

	public MainController() {}
	
	@GetMapping("/login")
	public String loginView() {
		return "login";
	}
	
	@GetMapping("/register")
	public String registerRedirect() {
		return "redirect:/user/register";
	}
	
	@GetMapping("/")
	public String home() {
		return "redirect:/food";
	}

  // @GetMapping("/error")
  // public String error() {

  // }

}
