package com.lab_lib.frontend;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lab_lib.frontend.DI.AppModule;
import com.lab_lib.frontend.Pages.BookListPage;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize Guice injector
        Injector injector = Guice.createInjector(new AppModule());
        
        // Get the BookListPage instance with all dependencies injected
        BookListPage bookListPage = injector.getInstance(BookListPage.class);
        
        // Show the page
        try {
            bookListPage.show(primaryStage);
        } catch (IOException e) {
            // Log the exception (replace with your preferred logging framework)
            System.err.println("Error loading page: " + e.getMessage());
            // Handle error
        }
    }
}
