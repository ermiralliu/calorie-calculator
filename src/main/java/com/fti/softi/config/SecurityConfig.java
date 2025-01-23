package com.fti.softi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.impl.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {
  @Autowired
  UserRepository userRepository;

  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl(userRepository);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        // .csrf(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/*.css", "/js/*.js").permitAll() // Public endpoints
            .requestMatchers("/register", "/user/register").not().authenticated()
            .requestMatchers("/login").not().authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/food/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated() // All other requests require authentication
        )
        .formLogin(login -> login
            .loginPage("/login") // Custom login page
            .defaultSuccessUrl("/food", true) // Redirect after successful login
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
  }
}
