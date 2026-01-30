// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lab_lib.restapi.DTO.AppUser.AppUserDTO;

/**
 * Entità che rappresenta un utente dell'applicazione.
 *
 * <p>Include informazioni di profilo e relazioni con raccomandazioni.
 */
@Entity
@Table(
    name = "app_user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}),
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"cf"}),
        @UniqueConstraint(columnNames = {"token"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, updatable = false, insertable = false)
    private UUID token;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(length = 16, unique = true)
    private String cf;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(mappedBy = "users")
    @Builder.Default
    @JsonBackReference
    private Set<RecommendedBook> recommendedBooks = new HashSet<>();

    /**
     * Converte l'utente in un DTO pubblico che espone informazioni non sensibili.
     *
     * @return {@link com.lab_lib.restapi.DTO.AppUser.AppUserDTO}
     */
    public AppUserDTO toDTO() {
        return new AppUserDTO(this);
    }

    /**
     * Aggiunge una raccomandazione dell'utente e aggiorna la relazione inversa.
     *
     * @param book raccomandazione da aggiungere
     */
    public void addRecommendedBook(RecommendedBook book) {
        recommendedBooks.add(book);
        book.getUsers().add(this);
    }

    /**
     * Verifica se l'utente ha già raccomandato il libro specificato.
     *
     * @param book raccomandazione da verificare
     * @return true se presente, false altrimenti
     */
    public boolean hasRecommendedBook(RecommendedBook book) {
        return recommendedBooks.contains(book);
    }

    /**
     * Rimuove la raccomandazione dall'utente e aggiorna la relazione inversa.
     *
     * @param book raccomandazione da rimuovere
     * @return true se rimossa, false se non presente
     */
    public boolean removeRecommendedBook(RecommendedBook book) {
        if(!hasRecommendedBook(book)) {
            return false;
        }
        recommendedBooks.remove(book);
        book.getUsers().remove(this);
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser other = (AppUser) o;
        return Objects.equals(this.id, other.id);
    }


}
