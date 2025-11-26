package com.lab_lib.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el FXML desde resources
        Parent root = FXMLLoader.load(
            getClass().getResource("/com/lab_lib/frontend/Pages/LogRegMainPanel.fxml")
        );

        Scene scene = new Scene(root);

        primaryStage.setTitle("Tu App Librería");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
