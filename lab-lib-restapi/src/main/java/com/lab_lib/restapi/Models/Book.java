// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lab_lib.restapi.DTO.Book.LibraryBookDTO;

/**
 * Entità che rappresenta un libro con metadati come titolo, descrizione,
 * prezzo, autori, categorie e valutazioni.
 *
 * <p>Fornisce metodi helper per convertire l'entità in DTO e per gestire le
 * relazioni verso librerie personali e autori.
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    @Column(name = "publish_month")
    private Integer publishMonth;

    @Column(name = "publish_year")
    private Integer publishYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @BatchSize(size = 50)
    private Set<Author> authors = new HashSet<Author>();

    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @BatchSize(size = 50)
    private Set<Category> categories;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<PersonalLibrary> libraries = new HashSet<>();

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private Set<Rating> ratings = new HashSet<>();  

    /**
     * Crea un DTO semplificato contenente le informazioni minime utili per
     * rappresentare il libro all'interno di una libreria (id e titolo).
     *
     * @return {@link LibraryBookDTO} con id e titolo
     */
    public LibraryBookDTO toLibraryDTO() {
        return new LibraryBookDTO(this.id, this.title);
    }

    /**
     * Aggiunge una libreria personale alla collezione delle librerie che contengono questo libro
     * e aggiorna la relazione inversa.
     *
     * @param personalLibrary libreria da aggiungere
     */
    public void addPersonalLibrary(PersonalLibrary personalLibrary) {
        libraries.add(personalLibrary);
        personalLibrary.getBooks().add(this);
    }

    /**
     * Rimuove la libreria personale dalla lista e aggiorna la relazione inversa.
     *
     * @param personalLibrary libreria da rimuovere
     */
    public void removePersonalLibrary(PersonalLibrary personalLibrary) {
        libraries.remove(personalLibrary);
        personalLibrary.getBooks().remove(this);
    }

    /**
     * Verifica se il libro è presente nella libreria personale indicata.
     *
     * @param personalLibrary libreria da verificare
     * @return true se presente, false altrimenti
     */
    public boolean hasPersonalLibrary(PersonalLibrary personalLibrary) {
        return libraries.contains(personalLibrary);
    }

    /**
     * Verifica se il libro è associato all'autore specificato.
     *
     * @param author autore da verificare
     * @return true se presente, false altrimenti
     */
    public boolean hasAuthor(Author author) {
        return authors.contains(author);
    }

}
