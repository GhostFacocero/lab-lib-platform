// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RecommendedBook;

@Repository
public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long> {

    boolean existsByBookAndRecommendedBook(Book book, Book recommendedBook);
    List<RecommendedBook> findAllByBook(Book book);
    RecommendedBook findByRecommendedBook(Book recommendedBook);
    RecommendedBook findByBookAndRecommendedBook(Book book, Book recommendedBook);
    int countByBookAndUsersContains(Book book, AppUser user);

}
