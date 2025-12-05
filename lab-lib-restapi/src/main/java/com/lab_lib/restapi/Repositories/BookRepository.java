package com.lab_lib.restapi.Repositories;

import com.lab_lib.restapi.Models.Book;

import org.springframework.data.jpa.repository.JpaRepository;
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
    Page<Book> findByAuthorsNameContaining(String author, Pageable pageable);
    Page<Book> findByTitleAndAuthorsNameContaining(String title, String author, Pageable pageable);

}
