package com.fti.softi.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class FoodEntity{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(unique = true)
   private String name;

   private String description;

   private Double price;

   private Integer calories;

   @Temporal(TemporalType.TIMESTAMP)
   private LocalDateTime createdAt;

   public FoodEntity() {

      id = null;
      name = null;
      description = null;
      price = null;
      calories = null;
      createdAt = null;
   }

   public FoodEntity(String name, String description, Double price, Integer calories, LocalDateTime createdAt) {

      this.id = null;
      this.name = name;
      this.description = description;
      this.price = price;
      this.calories = calories;
      this.createdAt = createdAt;
   }
}
