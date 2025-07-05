package com.lab_lib.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Utils.HttpUtil;

public class BookService implements IBookService {
    private final HttpUtil httpUtil;

    @Inject 
    public BookService(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    @Override
    public PaginatedResponse<Book> getBooks(int page, int size) {
        String endpoint = "/books?page=" + page + "&size=" + size;
        return httpUtil.get(endpoint, new TypeReference<PaginatedResponse<Book>>() {});
    }


}
