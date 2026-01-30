// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Utils.HttpUtil;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @Override
    public PaginatedResponse<Book> searchBooksByTitle(String title, int page, int size) {
        String q = title == null ? "" : URLEncoder.encode(title, StandardCharsets.UTF_8);
        String endpoint = "/books/search?title=" + q + "&startsWith=true&page=" + page + "&size=" + size;
        return httpUtil.get(endpoint, new TypeReference<PaginatedResponse<Book>>() {});
    }

    @Override
    public PaginatedResponse<Book> searchBooksByAuthor(String author, int page, int size) {
        String q = author == null ? "" : URLEncoder.encode(author, StandardCharsets.UTF_8);
        String endpoint = "/books/search?author=" + q + "&startsWith=true&page=" + page + "&size=" + size;
        return httpUtil.get(endpoint, new TypeReference<PaginatedResponse<Book>>() {});
    }

    @Override
    public PaginatedResponse<Book> searchBooksByTitleOrAuthor(String query, int page, int size) {
        String q = query == null ? "" : URLEncoder.encode(query, StandardCharsets.UTF_8);
        String endpoint = "/books/search?title=" + q + "&author=" + q + "&startsWith=true&page=" + page + "&size=" + size;
        return httpUtil.get(endpoint, new TypeReference<PaginatedResponse<Book>>() {});
    }

}
