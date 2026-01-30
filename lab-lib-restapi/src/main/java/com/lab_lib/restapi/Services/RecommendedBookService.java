// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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

        long count = recommendedBookRepository.countByBookAndUsersContains(book, user);
        if(count >= 3) {
            throw new IllegalArgumentException("Cannot add more than three recommendations per book");
        }
        RecommendedBook rb;
        System.out.println(recommendedBookRepository.existsByBookAndRecommendedBook(book, recommendedBook));
        if(!recommendedBookRepository.existsByBookAndRecommendedBook(book, recommendedBook)) {
            rb = new RecommendedBook();
            rb.setBook(book);
            rb.setRecommendedBook(recommendedBook);
        } else {
            rb = recommendedBookRepository.findByBookAndRecommendedBook(book, recommendedBook);
        }
        if(rb.hasUser(user)) {
            throw new IllegalArgumentException("Cannot recommend the same book multiple times");
        }
        rb.addUser(user);
        System.out.println(rb.hasUser(user));
        RecommendedBook saved = recommendedBookRepository.save(rb);
        return saved.toDTO();

    }


    @Transactional
    public void removeRecommendedBook(Book book, Book recommendedBook, AppUser user) {

        RecommendedBook recommendation = recommendedBookRepository.findByBookAndRecommendedBook(book, recommendedBook);
        recommendation.removeUser(user);

    }

}
