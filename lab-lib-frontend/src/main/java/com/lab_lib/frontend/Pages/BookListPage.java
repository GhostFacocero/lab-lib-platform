package com.lab_lib.frontend.Pages;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Author;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Pages.Components.PaginatedTableView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;

public class BookListPage {
    private final IBookService bookService;
    
    @FXML
    private BorderPane rootPane;

    @Inject
    public BookListPage(IBookService bookService) {
        this.bookService = bookService;
    }

    public void show(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/lab_lib/frontend/Pages/book-list.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource(
            "/com/lab_lib/frontend/styles/book-list.css").toExternalForm());
        
        initializeTable();
        primaryStage.setTitle("Book Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeTable() {
        PaginatedTableView<Book> table = new PaginatedTableView<>(page -> {
            PaginatedResponse<Book> response = bookService.getBooks(page, 20);
            
            for (Book book : response.getContent()) {
                System.out.println("Book: " + book.getTitle());
            }

            return new PaginatedTableView.PaginatedData<>(
                response.getContent(),
                response.getTotalPages(),
                response.getNumber()
            );
        });
        
        table.addColumn("Title", Book::getTitle);
        table.addColumn("Author", book -> 
            book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.joining(", ")));
        table.addColumn("Price", Book::getPrice);
        
        rootPane.setCenter(table);
    }
}