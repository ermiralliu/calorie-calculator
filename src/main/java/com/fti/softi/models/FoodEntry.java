package com.fti.softi.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class FoodEntry {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   //@Column(unique = true)
   private String name;

   private String description;

   private Double price;

   private Integer calories;

   //@Temporal(TemporalType.TIMESTAMP)
   private LocalDateTime createdAt;

   public FoodEntry() {
      id = null;
      user = null;
      name = null;
      description = null;
      price = null;
      calories = null;
      createdAt = null;
   }

   public FoodEntry(String name, User user, String description, Double price, Integer calories) {
      this.id = null;	// cause autogen
      this.user = user;
      this.name = name;
      this.description = description;
      this.price = price;
      this.calories = calories;
      this.createdAt = null; // cause autogen
   }
   public String getCreatedAtToString() {
  	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
  	 return this.createdAt.format(formatter);
   }
}
