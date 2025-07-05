package com.lab_lib.frontend.Interfaces;

import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;

public interface IBookService {

    public PaginatedResponse<Book> getBooks(
        int page,
        int size
    );

}
