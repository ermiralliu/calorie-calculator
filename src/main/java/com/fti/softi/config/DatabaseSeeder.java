package com.fti.softi.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

      var hasher = new BCryptPasswordEncoder();
      User admin = User.builder()
          .email("admin@example.com")
          .name("Admin")
          .roles(Set.of(adminRole, userRole))
          .password(hasher.encode("adminpassword"))
          .build();
      userRepository.save(admin);

      Set<Role> userRoles = Set.of(userRole);

      Food[] foodOptions = foodOptions();
      Food[] highCalorieFoods = highCalorieFoods();
      String[] names = { "Ermir", "Holta", "Orkida", "Selma"};

      for (String currentName : names) { // adding a user for each name
        User currentUser = User.builder()
          .email(currentName.toLowerCase() + "@example.com")
          .name(currentName)
          .roles(userRoles)
          .password(hasher.encode("password" + currentName))
          .build();
        User registeredUser = userRepository.save(currentUser);
        for (int j = 1; j <= LocalDate.now().getDayOfMonth(); j++) {
          LocalDateTime time = LocalDateTime.now().withDayOfMonth(j); // around this month
          for(int k= 6; k<=22; k+= (Math.random()*3 +2)){ // hour of day, incrementing with intervals from 2 to 4
            Food currentFood = (int) (Math.random()*6) == 0 
              ? foodOptions[(int) (Math.random() * foodOptions.length)] // making this rarer than the high calorie ones
              : highCalorieFoods[(int) (Math.random() * highCalorieFoods.length)];
            time = time.withHour(k);
            FoodEntry food = FoodEntry.builder()
                .user(registeredUser)
                .name(currentFood.foodName)
                .description(currentFood.description)
                .calories(currentFood.calories)
                .price(currentFood.price)
                .createdAt(time)
                .build();
            foodRepository.save(food);
            if(time.isAfter(LocalDateTime.now()))
              break;
          }
        }
      }
    };
  }

  public void seedSessionTables() {
    try (Connection conn = dataSource.getConnection()) {
      if (!tableExists(conn, "SPRING_SESSION")) {
        createTable(conn, SPRING_SESSION_STATEMENT());
      }
      if (!tableExists(conn, "SPRING_SESSION_ATTRIBUTES")) {
        createTable(conn, SPRING_SESSION_ATTRIBUTES_STATEMENT());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private String SPRING_SESSION_STATEMENT() {
    return """
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
    """;
  }

  private String SPRING_SESSION_ATTRIBUTES_STATEMENT() {
    return """
        CREATE TABLE SPRING_SESSION_ATTRIBUTES (
        SESSION_PRIMARY_ID CHAR(36) NOT NULL,
        ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
        ATTRIBUTE_BYTES BLOB NOT NULL,
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
      );
    """;
  }

  private class Food {
    String foodName;
    String description;
    int calories;
    double price;

    Food(String foodName, String description, int calories, double price) {
      this.foodName = foodName;
      this.description = description;
      this.calories = calories;
      this.price = price;
    }
  }

  private Food[] foodOptions() {
    Food[] foods = {
        new Food("Apple", "Crunchy fruit", 95, 0.5),
        new Food("Banana", "Yellow fruit", 105, 0.3),
        new Food("Orange", "Citrus fruit", 62, 0.4),
        new Food("Bacon", "Crispy bacon strips", 150, 2.00),
        new Food("Grapes", "Small berries", 62, 2.5),
        new Food("Watermelon", "Juicy melon", 86, 0.2),
        new Food("Pineapple", "Tropical fruit", 50, 1.0),
        new Food("Mango", "Sweet fruit", 201, 1.5),
        new Food("Blueberry", "Tiny berry", 84, 3.0),
        new Food("Raspberry", "Tart berry", 65, 3.0),
        new Food("Blackberry", "Dark berry", 62, 3.0),
        new Food("Peach", "Fuzzy fruit", 59, 1.0),
        new Food("Plum", "Stone fruit", 46, 0.8),
        new Food("Kiwi", "Fuzzy green", 42, 0.6),
        new Food("Pomegranate", "Seed-filled", 234, 2.0),
        new Food("Cherry", "Sweet/tart", 50, 4.0),
        new Food("Cantaloupe", "Orange melon", 60, 0.6),
        new Food("Honeydew", "Sweet melon", 61, 0.6),
        new Food("Coconut", "Tropical nut", 354, 3.0),
        new Food("Grapefruit", "Tangy citrus", 52, 1.0),
        new Food("Papaya", "Tropical orange", 55, 1.2),
        new Food("Guava", "Tropical pink", 37, 1.5),
        new Food("Avocado", "Creamy fruit", 160, 1.5),
        new Food("Tomato", "Juicy red", 22, 1.5),
        new Food("Cucumber", "Refreshing veggie", 16, 1.0),
        new Food("Bell Pepper", "Crunchy veggie", 24, 1.0),
        new Food("Carrot", "Orange root", 25, 1.0),
        new Food("Broccoli", "Green florets", 55, 1.0),
        new Food("Cauliflower", "White florets", 25, 1.0),
        new Food("Spinach", "Leafy green", 23, 1.5),
        new Food("Lettuce", "Salad green", 5, 1.0),
        new Food("Kale", "Dark green", 33, 2.0),
        new Food("Swiss Chard", "Colorful stalks", 35, 1.5),
        new Food("Collard Greens", "Hearty leaves", 11, 1.5),
        new Food("Beetroot", "Purple root", 43, 1.5),
        new Food("Onion", "Pungent bulb", 40, 1.0),
        new Food("Garlic", "Flavorful bulb", 149, 1.0),
        new Food("Shallot", "Mild onion", 72, 1.0),
        new Food("Leek", "Mild stalk", 54, 1.0),
        new Food("Green Onion", "Mild allium", 32, 1.0),
        new Food("Pumpkin", "Orange gourd", 26, 1.0),
        new Food("Potato", "Starchy tuber", 77, 1.0),
        new Food("Sweet Potato", "Sweet tuber", 86, 1.0),
        new Food("Yam", "Sweet tuber", 118, 1.0),
        new Food("Corn", "Yellow kernels", 96, 1.0),
        new Food("Green Bean", "String bean", 31, 1.0),
        new Food("Peas", "Sweet pods", 81, 1.0),
        new Food("Chickpeas", "Nutty legume", 164, 1.0),
        new Food("Lentils", "Small legumes", 116, 1.0),
        new Food("Black Beans", "Nutritious beans", 132, 1.0),
        new Food("Kidney Beans", "Red beans", 125, 1.0),
        new Food("Pinto Beans", "Speckled beans", 145, 1.0),
        new Food("Soybeans", "Protein-packed", 147, 1.0),
        new Food("Edamame", "Young soybeans", 121, 1.0),
        new Food("Mushroom", "Fungi", 22, 1.0),
        new Food("Asparagus", "Green spears", 20, 1.0),
        new Food("Artichoke", "Edible thistle", 60, 1.0),
        new Food("Brussels Sprouts", "Small cabbages", 43, 1.0),
        new Food("Eggplant", "Purple veggie", 25, 1.0),
        new Food("Okra", "Green pods", 31, 1.0),
        new Food("Turnip", "White root", 28, 1.0),
        new Food("Parsnip", "Sweet root", 75, 1.0),
        new Food("Rutabaga", "Root vegetable", 37, 1.0),
        new Food("Daikon", "White radish", 61, 1.0),
        new Food("Fennel", "Anise-flavored", 31, 1.0),
        new Food("Jicama", "Crunchy root", 38, 1.0),
        new Food("Snow Peas", "Sweet pods", 42, 1.0),
        new Food("Sugar Snap Peas", "Crisp pods", 42, 1.0),
        new Food("Savoy Cabbage", "Crinkly leaves", 28, 1.0),
        new Food("Bitter Melon", "Bitter gourd", 21, 1.0),
        new Food("Bamboo Shoots", "Crunchy sprouts", 27, 1.0),
        new Food("Lotus Root", "Edible root", 74, 1.0),
        new Food("Celeriac", "Celery root", 42, 1.0),
        new Food("Kohlrabi", "Edible bulb", 27, 1.0),
        new Food("Taro", "Starchy root", 142, 1.0),
        new Food("Plantain", "Starchy banana", 215, 1.0),
        new Food("Cassava", "Starchy root", 160, 1.0),
        new Food("Arrowroot", "Starchy tuber", 65, 1.0),
        new Food("Salsify", "Root vegetable", 82, 1.0),
        new Food("Burdock Root", "Edible root", 72, 1.0),
        new Food("Scotch Bonnet", "Fiery chili", 40, 2.0),
        new Food("Poblano Pepper", "Mild chili", 48, 1.5),
        new Food("Hatch Chili", "New Mexico", 40, 1.5),
        new Food("Chipotle Pepper", "Smoked jalapeño", 29, 2.0),
        new Food("Paprika", "Mild spice", 50, 1.0),
        new Food("Cumin", "Earthy spice", 22, 1.0),
        new Food("Ginger", "Spicy root", 80, 1.5),
        new Food("Onion Powder", "Flavoring", 24, 1.0),
        new Food("Mustard Seed", "Spicy seeds", 66, 1.0),
        new Food("Caraway Seed", "Earthy seeds", 56, 1.0),
        new Food("Fenugreek", "Bitter seeds", 36, 1.5),
        new Food("Cardamom", "Aromatic pods", 311, 3.0),
        new Food("Nutmeg", "Warm spice", 32, 2.0),
        new Food("Vanilla", "Sweet bean", 288, 3.0),
        new Food("Star Anise", "Licorice flavor", 337, 2.5),
        new Food("Fennel Seed", "Anise flavor", 345, 1.5),
        new Food("Anise Seed", "Licorice flavor", 23, 1.5),
        new Food("Dill Seed", "Flavoring seeds", 43, 1.0),
        new Food("Celery Seed", "Flavoring seeds", 25, 1.0),
        new Food("Basil", "Sweet herb", 22, 1.5),
        new Food("Thyme", "Aromatic herb", 28, 1.5),
        new Food("Sage", "Earthy herb", 32, 1.0),
        new Food("Parsley", "Fresh herb", 22, 1.0),
        new Food("Marjoram", "Mild herb", 271, 1.0),
        new Food("Lemongrass", "Citrus stalk", 99, 1.0),
        new Food("Hyssop", "Bitter herb", 53, 1.0),
        new Food("Lovage", "Celery flavor", 42, 1.5),
        new Food("Summer Savory", "Mild herb", 30, 1.0),
        new Food("Winter Savory", "Strong herb", 30, 1.0),
        new Food("Dill", "Aromatic herb", 43, 1.0),
        new Food("Fat Hen", "Leafy green", 43, 2.0),
        new Food("Sea Buckthorn", "Tart berry", 82, 2.0),
        new Food("Gooseberry", "Tart berry", 66, 3.0),
        new Food("Cloudberry", "Arctic berry", 29, 4.0),
        new Food("Salmonberry", "Tart berry", 64, 5.0),
        new Food("Lingonberry", "Tart berry", 47, 2.0),
        new Food("Aronia Berry", "Tart berry", 60, 3.0),
        new Food("Granola Bar", "A chocolate chip granola bar", 150, 1.00),
        new Food("Muffin", "A blueberry muffin", 200, 1.50),
        new Food("Donut", "A glazed donut with sprinkles", 250, 1.00)
    };
    return foods;
  }

  private Food[] highCalorieFoods() {
    Food[] highCalorieFoods = {
        new Food("Burger", "A juicy beef burger with cheese, lettuce, and tomato", 500, 5.99),
        new Food("Pizza", "A slice of pepperoni pizza with extra cheese", 300, 2.50),
        new Food("Fried Chicken", "Crispy fried chicken drumsticks", 400, 3.99),
        new Food("Ice Cream", "A scoop of vanilla ice cream with chocolate syrup", 200, 1.50),
        new Food("Chocolate Cake", "A rich chocolate cake with frosting", 350, 4.00),
        new Food("Pasta", "Creamy Alfredo pasta with chicken", 600, 7.99),
        new Food("French Fries", "Crispy golden French fries", 300, 2.00),
        new Food("Milkshake", "A thick chocolate milkshake", 400, 3.50),
        new Food("Cheesecake", "A slice of New York-style cheesecake", 450, 4.50),
        new Food("Hot Dog", "A hot dog with ketchup and mustard", 250, 2.50),
        new Food("Nachos", "Nachos with cheese and jalapenos", 350, 3.00),
        new Food("Pancakes", "Fluffy pancakes with syrup", 300, 3.50),
        new Food("Sausage", "Grilled sausage links", 250, 3.00),
        new Food("Brownie", "A fudgy chocolate brownie", 300, 2.00)
    };
    return highCalorieFoods;
  }

  private void createTable(Connection conn, String sqlStatement) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sqlStatement);
    }
  }

  private boolean tableExists(Connection conn, String tableName) throws SQLException {
    boolean exists = false;
    String sqlStatement = "SELECT name FROM sqlite_master WHERE type='table' AND name= ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
      stmt.setString(1, tableName);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          exists = true;
        }
      }
    }
    return exists;
  }

  // public double roundToTwoDecimalPlaces(double value) {
  // BigDecimal bd = new BigDecimal(Double.toString(value));
  // bd = bd.setScale(2, RoundingMode.HALF_UP);
  // return bd.doubleValue();
  // }
}