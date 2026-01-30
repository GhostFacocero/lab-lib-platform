// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
