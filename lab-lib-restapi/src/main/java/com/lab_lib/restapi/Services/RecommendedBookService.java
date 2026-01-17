package com.lab_lib.restapi.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RecommendedBook;
import com.lab_lib.restapi.Repositories.RecommendedBookRepository;

@Service
public class RecommendedBookService {
    
    private final RecommendedBookRepository recommendedBookRepository;

    public RecommendedBookService(RecommendedBookRepository recommendedBookRepository) {
        this.recommendedBookRepository = recommendedBookRepository;
    }

    @Transactional
    public List<RecommendedBookDTO> getRecommendedBooks(Book book) {

        List<RecommendedBook> recommendedBooks = recommendedBookRepository.findAllByBook(book);
        List<RecommendedBookDTO> booksDTO = recommendedBooks.stream().map(rb -> rb.toDTO()).toList();
        return booksDTO;

    }


    @Transactional
    public RecommendedBookDTO addRecommendedBook(Book book, Book recommendedBook, AppUser user) {

        List<RecommendedBook> recommendedBooks = recommendedBookRepository.findAllByBook(book);
        List<RecommendedBook> filteredRecommendations = recommendedBooks
        .stream()
        .filter(rb -> rb.getUsers().contains(user)).toList();
        if(filteredRecommendations.size() == 3) {
            throw new IllegalArgumentException("Cannot add more than three recommendation per book");
        }
        RecommendedBook rb = new RecommendedBook();
        rb.setBook(book);
        rb.setRecommendedBook(recommendedBook);
        rb.addUser(user);
        RecommendedBook saved = recommendedBookRepository.save(rb);
        return saved.toDTO();

    }


    @Transactional
    public void removeRecommendedBook(Book book, Book recommendedBook, AppUser user) {

        RecommendedBook recommendation = recommendedBookRepository.findByBookAndRecommendedBook(book, recommendedBook);
        recommendation.removeUser(user);

    }

}
