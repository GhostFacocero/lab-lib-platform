// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import java.util.HashSet;
import java.util.Set;

import com.lab_lib.restapi.DTO.PersonalLibrary.PersonalLibraryDTO;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entità che rappresenta la libreria personale di un utente.
 *
 * <p>Contiene il nome della libreria, l'utente proprietario e l'insieme di libri
 * associati. Fornisce metodi per manipolare la collezione di libri e convertire
 * l'entità in DTO.
 */
@Entity
@Table(
    name = "personal_library",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ID"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalLibrary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "book_pl",
        joinColumns = @JoinColumn(name = "id_pl"),
        inverseJoinColumns = @JoinColumn(name = "id_book")
    )

    @Builder.Default

    private Set<Book> books = new HashSet<>();

    /**
     * Converte l'entità PersonalLibrary nel corrispondente DTO per la risposta API.
     *
     * @return {@link com.lab_lib.restapi.DTO.PersonalLibrary.PersonalLibraryDTO}
     */
    public PersonalLibraryDTO toDTO() {
        PersonalLibraryDTO personalLibrary = new PersonalLibraryDTO();
        personalLibrary.setId(this.id);
        personalLibrary.setName(this.name);
        personalLibrary.setUserNickname(this.user.getNickname());
        return personalLibrary;
    }

    /**
     * Aggiunge un libro alla libreria personale e aggiorna la relazione inversa.
     *
     * @param book libro da aggiungere
     */
    public void addBook(Book book) {
        books.add(book);
        book.getLibraries().add(this);
    }

    /**
     * Rimuove un libro dalla libreria personale e aggiorna la relazione inversa.
     *
     * @param book libro da rimuovere
     */
    public void removeBook(Book book) {
        books.remove(book);
        book.getLibraries().remove(this);
    }

    /**
     * Verifica se la libreria contiene il libro specificato.
     *
     * @param book libro da verificare
     * @return true se presente, false altrimenti
     */
    public boolean hasBook(Book book) {
        return books.contains(book);
    }

}