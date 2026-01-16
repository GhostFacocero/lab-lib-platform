package com.lab_lib.restapi.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;
import com.lab_lib.restapi.Exceptions.AuthenticationException;
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

}
