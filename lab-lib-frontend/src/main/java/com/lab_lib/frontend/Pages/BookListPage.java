// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Pages;

import java.io.IOException;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Pages.Components.PaginatedTableView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BookListPage {
    
    @FXML
    private BorderPane rootPane;

    private final IBookService bookService;
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
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initializeTable() {
        PaginatedTableView<Book> table = new PaginatedTableView<>(page -> bookService.getBooks(page, 20));

        // Show only the book title to match the simplified Book model
        table.addColumn("Title", Book::getTitle);

        rootPane.setCenter(table);
    }
}