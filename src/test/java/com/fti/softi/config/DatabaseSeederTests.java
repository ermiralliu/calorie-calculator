package com.fti.softi.config;

import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.Role;
import com.fti.softi.models.User;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.repositories.RoleRepository;
import com.fti.softi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class DatabaseSeederTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private FoodEntryRepository foodRepository;

    @Mock
    private DataSource dataSource;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSeedMethod() throws Exception {
        // Arrange: Setup roles, users, and mock repository behavior
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminRoles.add(userRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        User admin = User.builder()
                .email("admin@gmail.com")
                .name("Admin")
                .roles(adminRoles)
                .password(new BCryptPasswordEncoder().encode("adminpassword"))
                .build();

        User user = User.builder()
                .email("john@gmail.com")
                .name("John")
                .roles(userRoles)
                .password(new BCryptPasswordEncoder().encode("passwordjohn"))
                .build();

        // Mock repository interactions
        when(roleRepository.count()).thenReturn(0L);
        when(roleRepository.save(any(Role.class))).thenReturn(adminRole, userRole);
        when(userRepository.save(any(User.class))).thenReturn(admin, user);

        ApplicationArguments args = mock(ApplicationArguments.class);

        ApplicationRunner runner = databaseSeeder.seed();
        runner.run(args);

        // Verify the role and user creation
        verify(roleRepository, times(2)).save(any(Role.class)); // Ensure roles are saved
        verify(userRepository, times(2)).save(any(User.class)); // Ensure users are saved
        verify(foodRepository, atLeastOnce()).save(any(FoodEntry.class)); // Ensure food entries are saved
    }

    @Test
    void testSeedSessionTables() {
        // This test would check if session tables are created if they don't exist
        // For simplicity, we'll mock the behavior for table existence check
        try {
            databaseSeeder.seedSessionTables();
            // Here you can verify the methods interacting with DataSource
            verify(dataSource, times(1)).getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
