package com.fti.softi.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@PostConstruct	// waits until dependency injection is done
	@Bean
	public ApplicationRunner seed() {
		return args->{
			if(roleRepository.count() != 0)
				return;
			roleRepository.deleteAll();
			userRepository.deleteAll();
			foodRepository.deleteAll();
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole = roleRepository.save(new Role("USER"));
			
			Set<Role> adminRoles = new HashSet<Role>();
			adminRoles.add(adminRole);
			adminRoles.add(userRole);

			var hasher = new BCryptPasswordEncoder();
			User admin = User.builder()
					.email("admin@gmail.com")
					.name("Admin")
					.roles(adminRoles)
					.password(hasher.encode("adminpassword"))
					.build();
			userRepository.save(admin);
			
			Set<Role> userRoles = new HashSet<Role>();
			userRoles.add(userRole);
			
			for(int i = 0; i<10; i++) { //adding 20 users
				User currentUser = User.builder()
						.email( "user"+ i +"@gmail.com")
						.name("User "+i)
						.roles(userRoles)
						.password(hasher.encode("password"+i))
						.build();
				User registeredUser = userRepository.save(currentUser);
				for(int j=0; j<30; j++) {
					LocalDateTime time = LocalDateTime.now().withDayOfMonth((j%29) +1);	// around this month
					FoodEntry food = FoodEntry.builder()
							.user(registeredUser)
							.name("Food "+j+" of User "+i)
							.description("Mock " + i + " * "+j )	
							.calories( (int) (Math.random()*500))
							.price(roundToTwoDecimalPlaces(Math.random()*100))
							.createdAt(time)
							.build();
					foodRepository.save(food);
				}
			}
		};
	}
	
	public double roundToTwoDecimalPlaces(double value) { 
		BigDecimal bd = new BigDecimal(Double.toString(value)); 
		bd = bd.setScale(2, RoundingMode.HALF_UP); 
		return bd.doubleValue(); 
	}

	//String[] options = new String[]  {"Pasta" , "Risotto" , ... }
}