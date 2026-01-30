// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Pages;

import java.util.List;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IRatingService; // Assicurati che il package sia corretto
import com.lab_lib.frontend.Models.RecommendedBookDTO;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class RecommendedBooksController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<RecommendedBookDTO> recommendationsTable;
    @FXML private TableColumn<RecommendedBookDTO, String> colTitle;
    @FXML private TableColumn<RecommendedBookDTO, Integer> colCount;
    @FXML private TableColumn<RecommendedBookDTO, String> colNicknames;
    @FXML private Button btnClose;

    private final IRatingService ratingService;

    @Inject
    public RecommendedBooksController(IRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @FXML
    public void initialize() {
        // Setup Colonne
        
        // 1. Titolo
        colTitle.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitle()));

        // 2. Count (Dimensione della lista userNicknames)
        colCount.setCellValueFactory(cellData -> {
            List<String> nickNames = cellData.getValue().getUserNicknames();
            int count = (nickNames != null) ? nickNames.size() : 0;
            return new SimpleIntegerProperty(count).asObject();
        });

        // 3. Nicknames (Join con virgola)
        colNicknames.setCellValueFactory(cellData -> {
            List<String> nickNames = cellData.getValue().getUserNicknames();
            if (nickNames == null || nickNames.isEmpty()) return new SimpleStringProperty("-");
            return new SimpleStringProperty(String.join(", ", nickNames));
        });
    }

    public void setContext(long bookId, String sourceBookTitle) {
        if (subtitleLabel != null) {
            subtitleLabel.setText("Utenti che hanno letto '" + sourceBookTitle + "' consigliano:");
        }

        loadData(bookId);
    }

    private void loadData(long bookId) {
        try {
            List<RecommendedBookDTO> recommendations = ratingService.getRecommendedBooks(bookId);
            if (recommendations != null) {
                recommendationsTable.getItems().setAll(recommendations);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare i consigli");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}