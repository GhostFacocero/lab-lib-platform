package com.lab_lib.frontend.Models;

public class Publisher {
    private Integer id;
    private String name;

    public Publisher(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
