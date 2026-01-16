package com.lab_lib.restapi.Models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JoinColumn(name = "id_book")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "id_rbook")
    private Book recommendedBook;

    @ManyToMany
    @JoinTable(
        name = "user_rb",
        joinColumns = @JoinColumn(name = "rb_id", referencedColumnName = "id_rbook"),
        inverseJoinColumns = @JoinColumn(name = "id_user", referencedColumnName = "user_id")
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

}