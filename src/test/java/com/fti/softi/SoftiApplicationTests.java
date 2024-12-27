//package com.fti.softi;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.fti.softi.models.User;
//import com.fti.softi.models.FoodEntry;
//import com.fti.softi.repositories.FoodEntryRepository;
//import com.fti.softi.repositories.UserRepository;
//import com.fti.softi.services.CurrentUserServiceImpl;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class SoftiApplicationTests {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private FoodEntryRepository foodEntryRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//
//
//	@Test
//	void contextLoads() {
//	}
//
//	// CurrentUserServiceImplTests
//	@Test
//	void testGetCurrentUser() {
//		User mockUser = new User();
//		mockUser.setUsername("testuser");
//		when(userRepository.findByUsername("testuser")).thenReturn(mockUser);
//
//		CurrentUserServiceImpl currentUserService = new CurrentUserServiceImpl(userRepository);
//		User result = currentUserService.getCurrentUser("testuser");
//		assertEquals("testuser", result.getUsername());
//	}
//
//	// FoodEntryControllerTests
//	@Test
//	void testAddFoodEntry() throws Exception {
//		String newFoodEntryJson = """
//            {
//                "name": "Apple",
//                "calories": 95,
//                "date": "2024-12-27"
//            }
//        """;
//
//		mockMvc.perform(post("/food-entries")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(newFoodEntryJson))
//				.andExpect(status().isCreated());
//	}
//
//	// FoodEntryRepositoryTests
//	@Test
//	void testFindAll() {
//		FoodEntry foodEntry = new FoodEntry();
//		foodEntry.setName("Banana");
//		foodEntry.setCalories(105);
//		foodEntryRepository.save(foodEntry);
//
//		List<FoodEntry> foodEntries = foodEntryRepository.findAll();
//		assertFalse(foodEntries.isEmpty());
//		assertEquals("Banana", foodEntries.get(0).getName());
//	}
//
//	// SecurityConfigTests
//	@Test
//	@WithMockUser(username = "admin", roles = {"ADMIN"})
//	void testAdminAccess() throws Exception {
//		mockMvc.perform(get("/admin"))
//				.andExpect(status().isOk());
//	}
//
//	@Test
//	void testUnauthorizedAccess() throws Exception {
//		mockMvc.perform(get("/admin"))
//				.andExpect(status().isUnauthorized());
//	}
//
//}
