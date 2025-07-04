package com.lab_lib.restapi.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "author", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
