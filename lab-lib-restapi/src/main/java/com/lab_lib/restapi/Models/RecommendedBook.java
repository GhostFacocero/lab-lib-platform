// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommended_books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_book")
    private Book book;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_rbook")
    private Book recommendedBook;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
        name = "user_rb",
        joinColumns = @JoinColumn(name = "rb_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )

    @Builder.Default
    private Set<AppUser> users = new HashSet<>();

    public RecommendedBookDTO toDTO() {
        RecommendedBookDTO recommendedBook = new RecommendedBookDTO();
        recommendedBook.setTitle(this.recommendedBook.getTitle());
        List<String> userNicknames = this.users.stream().map(u -> u.getNickname()).toList();
        recommendedBook.setUserNicknames(userNicknames);
        return recommendedBook;
    }

    public void addUser(AppUser user) {
        users.add(user);
        user.getRecommendedBooks().add(this);
    }

    public boolean hasUser(AppUser user) {
        return users.contains(user);
    }

    public boolean removeUser(AppUser user) {
        if(!hasUser(user)) {
            return false;
        }
        users.remove(user);
        user.getRecommendedBooks().remove(this);
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecommendedBook)) return false;
        RecommendedBook other = (RecommendedBook) o;
        return Objects.equals(this.id, other.id);
    }


}