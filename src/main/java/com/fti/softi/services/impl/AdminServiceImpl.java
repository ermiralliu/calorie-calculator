package com.fti.softi.services.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fti.softi.dtos.CalorieDto;
import com.fti.softi.models.FoodEntry;
import com.fti.softi.models.User;
import com.fti.softi.repositories.FoodEntryRepository;
import com.fti.softi.repositories.UserRepository;
import com.fti.softi.services.AdminService;
import com.fti.softi.utils.DateUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService  {
  private final FoodEntryRepository foodEntryRepository;
  private final UserRepository userRepository;

  @Override
  public boolean foodIsPresent(long id){
    return foodEntryRepository.findById(id).isPresent();
  }

  @Override
  public void deleteFoodEntryById(long id) {
    foodEntryRepository.deleteById(id);
  }

  @Override
  public void updateFoodEntry(  long foodId, String name, String description, 
    Double price, Integer calories, LocalDateTime createdAt ) {
    FoodEntry previous = foodEntryRepository.findById(foodId).orElse(null);
    if(previous == null){
      System.out.println("Food Entry of id "+ foodId+" was not found.");
      return;
    }
    if(name != null)
      previous.setName(name);
    if(description != null)
      previous.setDescription(description);
    if(price != null)
      previous.setPrice(price);
    if(calories != null)
      previous.setCalories(calories);
    if(createdAt != null)
      previous.setCreatedAt(createdAt);
    foodEntryRepository.save(previous);
  }
  
   @Override
  public LinkedHashMap<String, Integer> getWeeklyEntryComparison() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = now.minusDays(7);
    LocalDateTime previousWeekStart = startOfWeek.minusDays(7);

    int last7Days = ((int) foodEntryRepository.countByCreatedAtBetween(startOfWeek, now));
    int previousWeek = ((int) foodEntryRepository.countByCreatedAtBetween(previousWeekStart, startOfWeek));

    LinkedHashMap<String, Integer> weeklyEntryComparison = new LinkedHashMap<>();
    weeklyEntryComparison.put("Last 7 Days", last7Days);
    weeklyEntryComparison.put("Previous Week", previousWeek);

    return weeklyEntryComparison; // Return the map directly
  }

  @Override // Konverton Listen me Object array qe merret nga databaza ne Liste me
            // CalorieDto
            public List<CalorieDto> averageDailyCaloriesLast7Days() {
    return foodEntryRepository.findAverageCaloriesPerUser().stream()
        .map(item -> {
          String name = (String) item[0];
          String caloriesPerDay = String.format("%.2f", item[1]);
          return new CalorieDto(name, caloriesPerDay);
        })
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getUsersExceedingMonthlyExpenditureLimit(double monthlyLimit) {
    // LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1).withDayOfMonth(1);
    LocalDateTime monthStart = DateUtils.currentMonthStart().minusMonths(1);  // start of previous month
    LocalDateTime monthEnd = DateUtils.currentMonthStart();
    List<FoodEntry> lastMonthEntries = foodEntryRepository.findByCreatedAtBetween(monthStart, monthEnd);
    return lastMonthEntries.stream()
        .collect(Collectors.groupingBy(entry -> entry.getUser().getId()))
        .entrySet().stream()
        .filter(entry -> entry.getValue().stream()
            .mapToDouble(FoodEntry::getPrice).sum() > monthlyLimit)
        .map(entry -> entry.getValue().get(0).getUser().getEmail())
        .toList();
  }
  @Override
  public List<User> getAllUsers(){
    return userRepository.findAll();
  }

  @Override
  public List<FoodEntry> getAllForUser(long user_id){
    var foodEntries = foodEntryRepository.findByUserId(user_id);
    foodEntries.sort((one, two)-> -one.getCreatedAt().compareTo(two.getCreatedAt()) );
    return foodEntries;
  }
  @Override
  public FoodEntry getFood(long foodId){
    return foodEntryRepository.findById(foodId).orElse(null);
  }
}
