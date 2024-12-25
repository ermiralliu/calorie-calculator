package com.fti.softi.config;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.Role;
import com.fti.softi.models.User;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DatabaseSeeder {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final FoodEntryRepository foodRepository;
	
	@PostConstruct
	public ApplicationRunner seed() {
		return args->{
			if(roleRepository.count() != 0)
				return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole = roleRepository.save(new Role("USER"));
			
			Set<Role> adminRoles = new HashSet<Role>();
			adminRoles.add(adminRole);
			adminRoles.add(userRole);
			User admin = User.builder()
					.email("admin@gmail.com")
					.name("Admin")
					.roles(adminRoles)
					.build();
			userRepository.save(admin);
			
			Set<Role> userRoles = new HashSet<Role>();
			userRoles.add(userRole);
			
			for(int i = 0; i<20; i++) { //adding 20 users
				User currentUser = User.builder()
						.email( "user"+ i +"@gmail.com")
						.name("User "+i)
						.roles(userRoles)
						.build();
				User registeredUser = userRepository.save(currentUser);
				for(int j=0; j<30; j++) {
					LocalDateTime time = LocalDateTime.now().withDayOfMonth(j);	// around this month
					FoodEntry food = FoodEntry.builder()
							.user(registeredUser)
							.name("Food "+j+"of User "+i)
							.calories( (int) Math.random()*500)
							.price((double) Math.random()*100)
							.createdAt(time)
							.build();
					foodRepository.save(food);
				}
			}
		};
		
	}

}
