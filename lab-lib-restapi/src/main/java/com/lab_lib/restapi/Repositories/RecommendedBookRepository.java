package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RecommendedBook;

@Repository
public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long> {

    List<RecommendedBook> findAllByBook(Book book);
    RecommendedBook findByRecommendedBook(Book recommendedBook);
    RecommendedBook deleteByBookAndUserId(Book book);
    RecommendedBook deleteByRecommendedBookAndUserId(Book recommendedBook);
    
}
