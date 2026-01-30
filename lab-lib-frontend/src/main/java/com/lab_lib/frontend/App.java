package com.lab_lib.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.lab_lib.frontend.DI.AppModule;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inizializza Guice e usa la factory per i controller
        Injector injector = Guice.createInjector(new AppModule());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/LogRegMainPanel.fxml"));
        loader.setControllerFactory(injector::getInstance);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        primaryStage.setTitle("Tu App Librería");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
