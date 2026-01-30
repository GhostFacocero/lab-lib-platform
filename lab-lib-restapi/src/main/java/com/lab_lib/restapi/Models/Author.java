// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entità che rappresenta un autore.
 *
 * <p>Contiene nome unico e la lista dei libri associati.
 */
@Entity
@Table(name = "author", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<Book> books = new HashSet<Book>();

    /**
     * Verifica se l'autore è associato al libro indicato.
     *
     * @param book libro da verificare
     * @return true se associato, false altrimenti
     */
    public boolean hasBook(Book book) {
        return books.contains(book);
    }

}
