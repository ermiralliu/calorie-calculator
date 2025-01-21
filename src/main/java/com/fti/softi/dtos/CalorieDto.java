package com.fti.softi.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Builder
public class CalorieDto{
  private String name;
  private String caloriesPerDay; // string representation

  public CalorieDto(){}

  public CalorieDto(String name, String caloriesPerDay) { // Matching constructor
    this.name = name;
    this.caloriesPerDay = caloriesPerDay;
  }
}
