package com.lab_lib.frontend.Interfaces;

import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;

public interface IBookService {

    public PaginatedResponse<Book> getBooks(
        int page,
        int size
    );

    public PaginatedResponse<Book> searchBooksByTitle(
        String title,
        int page,
        int size
    );

    public PaginatedResponse<Book> searchBooksByAuthor(
        String author,
        int page,
        int size
    );

    public PaginatedResponse<Book> searchBooksByTitleOrAuthor(
        String query,
        int page,
        int size
    );

}
