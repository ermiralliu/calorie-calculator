package com.fti.softi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class MainController {

	public MainController() {}

  @GetMapping("/register")
	public String getInsertView() {
		return "register";
	}
	
	@GetMapping("/login")
	public String loginView(
  @RequestParam(value = "error", required = false) String error,
  Model model) {
    if(error != null){
      model.addAttribute("error", "Unsuccessful Login");
      return "login";
    }
		return "login";
	}
	
	@GetMapping("/")
	public String home() {
		return "redirect:/food";
	}

}
