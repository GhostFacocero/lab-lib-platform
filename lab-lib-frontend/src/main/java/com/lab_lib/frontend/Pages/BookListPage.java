package com.lab_lib.frontend.Pages;

import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.lab_lib.frontend.Pages.Components.PaginatedTableView;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Author;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BookListPage {
    private final IBookService bookService;

    @Inject
    public BookListPage(IBookService bookService) {
        this.bookService = bookService;
    }

    public void show(Stage primaryStage) {
        // Create table with custom data fetcher
        PaginatedTableView<Book> table = new PaginatedTableView<>(page -> {
            // Replace 'var' with the explicit type, e.g., Page<Book>
            PaginatedResponse<Book> response = bookService.getBooks(page, 20);
            return new PaginatedTableView.PaginatedData<>(
                response.getContent(),
                response.getTotalPages(),
                response.getNumber()
            );
        });
        
        // Configure columns
        table.addColumn("Title", Book::getTitle);
        table.addColumn("Author", book -> 
            book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.joining(", ")));
        table.addColumn("Price", Book::getPrice);

        BorderPane root = new BorderPane(table);
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Book Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
