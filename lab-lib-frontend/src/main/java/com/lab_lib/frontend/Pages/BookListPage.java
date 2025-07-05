package com.lab_lib.frontend.Pages;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Author;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.Category;
import com.lab_lib.frontend.Pages.Components.PaginatedTableView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
            "/com/lab_lib/frontend/Css/styles.css").toExternalForm());
        
        initializeTable();
        primaryStage.setTitle("Book Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeTable() {
        PaginatedTableView<Book> table = new PaginatedTableView<>(page -> bookService.getBooks(page, 20));
        
        table.addColumn("Title", Book::getTitle);
        table.addColumn("Description", Book::getDescription).setPrefWidth(400);
        table.addColumn("Authors", book -> 
            book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.joining(", ")));
        table.addColumn("Categories", book -> 
            book.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.joining(", ")));
        table.addColumn("Price", Book::getPrice);
        table.addColumn("P. Month", Book::getPublishMonth);
        table.addColumn("P. Year", Book::getPublishYear);

        rootPane.setCenter(table);
    }
}