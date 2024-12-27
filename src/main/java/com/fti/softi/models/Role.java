package com.fti.softi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Role implements Serializable{

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private transient Set<User> users; // A role can be assigned to many users

    public Role() { // Default constructor for JPA
        id = null;
        name = null;
        users = null;
    }

    public Role(String name) { // Parameterized constructor
        this.id = null;
        this.name = name;
        this.users = null;
    }
}
