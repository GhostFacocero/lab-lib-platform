package com.lab_lib.frontend.Models;

public class Author {
    private Integer id;
    private String name;

    public Author(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author() {
        // Default constructor for deserialization
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
