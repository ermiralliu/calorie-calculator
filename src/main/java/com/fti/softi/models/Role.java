package com.fti.softi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Role{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(unique = true)
  private final String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<User> users; // A role can be assigned to many users

  protected Role(){
    this(null, null, null);
  }

  public Role(String name) { // Parameterized constructor
    this.id = null;
    this.name = name;
    this.users = null;
  }
}
