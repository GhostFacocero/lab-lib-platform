// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RatingName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"publisher"})

    Page<Book> findAll(Pageable pageable);
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByTitleStartingWithIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorsNameContaining(String author, Pageable pageable);
    Page<Book> findByTitleAndAuthorsNameContaining(String title, String author, Pageable pageable);
    Page<Book> findByAuthorsNameStartingWithIgnoreCase(String author, Pageable pageable);
    Page<Book> findDistinctByTitleStartingWithIgnoreCaseOrAuthorsNameStartingWithIgnoreCase(String title, String author, Pageable pageable);
    Page<Book> findByLibrariesId(Long libId, Pageable pageable);
    Page<Book> findByLibrariesIdAndTitleContaining(Long libId, String title, Pageable pageable);
    Page<Book> findByLibrariesIdAndAuthorsNameContaining(Long libId, String author, Pageable pageable);
    Page<Book> findByLibrariesIdAndTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(Long libId, String title, String author, Pageable pageable);
    Page<Book> findByRatingsNameAndRatingsEvaluation(RatingName name, Integer evaluation, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY LENGTH(b.title) ASC")
    Page<Book> findByTitleContainingSortedByLength(@Param("title") String title, Pageable pageable);
}
