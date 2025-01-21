package com.fti.softi.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
  private final Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonBackReference // Prevents infinite recursion
  private final User user;

  private String name;

  private String description;

  private Double price;

  private Integer calories;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private final LocalDateTime createdAt;

  public FoodEntry() { // for jpaRepository
    id = null;
    user = null;
    createdAt = null;
  }

  public String getDate() {
    return this.createdAt.toLocalDate().toString();
  }

  public String getTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return this.createdAt.format(formatter);
  }
}
