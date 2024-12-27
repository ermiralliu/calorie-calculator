package com.fti.softi.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

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
  private final DataSource dataSource;

  @PostConstruct // waits until dependency injection is done
  @Bean
  public ApplicationRunner seed() {
    return args -> {
      seedSessionTables();
      if (roleRepository.count() != 0)
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

      for (int i = 0; i < 10; i++) { // adding 20 users
        User currentUser = User.builder()
            .email("user" + i + "@gmail.com")
            .name("User " + i)
            .roles(userRoles)
            .password(hasher.encode("password" + i))
            .build();
        User registeredUser = userRepository.save(currentUser);
        for (int j = 0; j < 30; j++) {
          LocalDateTime time = LocalDateTime.now().withDayOfMonth((j % 29) + 1); // around this month
          FoodEntry food = FoodEntry.builder()
              .user(registeredUser)
              .name("Food " + j + " of User " + i)
              .description("Mock " + i + " * " + j)
              .calories((int) (Math.random() * 500))
              .price(roundToTwoDecimalPlaces(Math.random() * 100))
              .createdAt(time)
              .build();
          foodRepository.save(food);
        }
      }
    };
  }

  public void seedSessionTables() {
    try (Connection conn = dataSource.getConnection()) {
      if (!tableExists(conn, "SPRING_SESSION")) { 
        createTables(conn); 
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createTables(Connection conn) throws SQLException {
    String sql = """
        CREATE TABLE SPRING_SESSION (
            PRIMARY_ID CHAR(36) NOT NULL,
            SESSION_ID CHAR(36) NOT NULL,
            CREATION_TIME BIGINT NOT NULL,
            LAST_ACCESS_TIME BIGINT NOT NULL,
            MAX_INACTIVE_INTERVAL INT NOT NULL,
            EXPIRY_TIME BIGINT NOT NULL,
            PRINCIPAL_NAME VARCHAR(100),
            CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
        );
        CREATE TABLE SPRING_SESSION_ATTRIBUTES (
            SESSION_PRIMARY_ID CHAR(36) NOT NULL,
            ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
            ATTRIBUTE_BYTES BLOB NOT NULL,
            CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
            CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
        );
        """;
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    }
  }

  private boolean tableExists(Connection conn, String tableName) throws SQLException {
    boolean exists = false;
    try (Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt
          .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';");
      if (rs.next()) {
        exists = true;
      }
    }
    return exists;
  }

  public double roundToTwoDecimalPlaces(double value) {
    BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}