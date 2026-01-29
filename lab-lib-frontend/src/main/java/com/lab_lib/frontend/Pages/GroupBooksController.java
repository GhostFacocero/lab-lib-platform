package com.lab_lib.frontend.Pages;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;

import java.util.Collections;
import java.util.Optional;

public class GroupBooksController {

    @FXML private Label GroupTitleLabel;
    @FXML private TableView<Book> GroupBooksTable;
    @FXML private TableColumn<Book, String> ColTitle;
    @FXML private TableColumn<Book, String> ColAuthors;
    @FXML private Button BtnClose;

    private IPersonalLibraryService personalLibraryService;
    private IRatingService ratingService;
    private long libraryId;
    private String groupName;

    public void setContext(IPersonalLibraryService personalLibraryService, IRatingService ratingService, long libraryId, String groupName) {
        this.personalLibraryService = personalLibraryService;
        this.ratingService = ratingService;
        this.libraryId = libraryId;
        this.groupName = groupName;
        if (GroupTitleLabel != null) GroupTitleLabel.setText("Libri del gruppo: " + groupName);
        setupTable();
        loadBooks();
    }

    // Backward-compatible overload used by GroupListController
    public void setContext(IPersonalLibraryService personalLibraryService, long libraryId, String groupName) {
        setContext(personalLibraryService, null, libraryId, groupName);
    }

    @FXML
    public void initialize() {
        if (BtnClose != null) {
            BtnClose.setOnAction(e -> {
                Stage stage = (Stage) BtnClose.getScene().getWindow();
                stage.close();
            });
        }
    }

    private void setupTable() {
        if (ColTitle != null) {
            ColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        }
        if (ColAuthors != null) {
            ColAuthors.setCellValueFactory(cell -> {
                var list = Optional.ofNullable(cell.getValue().getAuthors()).orElse(Collections.emptyList());
                return new SimpleStringProperty(String.join(", ", list));
            });
        }
        GroupBooksTable.setRowFactory(tv -> {
            var row = new javafx.scene.control.TableRow<Book>();
            var menu = new ContextMenu();
            // Evita fondo negro: hace transparente la escena del ContextMenu y aplica estilo inline
            menu.setOnShown(ev -> {
                if (menu.getScene() != null) {
                    menu.getScene().setFill(Color.TRANSPARENT);
                    if (menu.getScene().getRoot() != null) {
                        menu.getScene().getRoot().setStyle("-fx-background-color: transparent;");
                    }
                }
            });
            menu.setStyle("-fx-background-color: #2E2E2E; -fx-background-radius: 6; -fx-border-color: #333333; -fx-border-radius: 6; -fx-padding: 4 0;");
            MenuItem infoItem = new MenuItem("Information");
            infoItem.setStyle("-fx-text-fill: #FFFFFF;");
            infoItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b != null) showBookInformation(b);
            });

            MenuItem reviewItem = new MenuItem("Valutare");
            reviewItem.setStyle("-fx-text-fill: #FFFFFF;");
            reviewItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b != null) openValutaForBook(b);
            });

            MenuItem removeItem = new MenuItem("Elimina dal gruppo");
            removeItem.setStyle("-fx-text-fill: #FFFFFF;");
            removeItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b == null) return;
                try {
                    personalLibraryService.removeBookFromLibrary(libraryId, b.getId());
                    GroupBooksTable.getItems().removeIf(x -> x.getId().equals(b.getId()));
                } catch (Exception ex) {
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Errore");
                    err.setHeaderText("Impossibile rimuovere il libro dal gruppo");
                    err.setContentText(ex.getMessage());
                    styleAlert(err);
                    err.showAndWait();
                }
            });
            menu.getItems().addAll(infoItem, reviewItem, removeItem);
            row.setContextMenu(menu);
            // Left-click default action: show Information
            row.setOnMouseClicked(ev -> {
                if (ev.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {
                    Book b = row.getItem();
                    if (b != null) showBookInformation(b);
                    ev.consume();
                }
            });
            return row;
        });
    }

    private void loadBooks() {
        try {
            PaginatedResponse<Book> page = personalLibraryService.getBooksInLibrary(libraryId, 0, 50);
            var books = Optional.ofNullable(page).map(PaginatedResponse::getContent).orElse(Collections.emptyList());
            GroupBooksTable.getItems().setAll(books);
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile caricare i libri del gruppo");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
        }
    }

    private void showBookInformation(Book b) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Informazioni libro");
        info.setHeaderText(b.getTitle() != null ? b.getTitle() : "Informazioni");
        String authors = Optional.ofNullable(b.getAuthors()).orElse(Collections.emptyList()).isEmpty()
                ? ""
                : String.join(", ", b.getAuthors());
        info.setContentText("Autori: " + authors + "\nID: " + b.getId());
        styleAlert(info);
        info.showAndWait();
    }

    private void openValutaForBook(Book b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/SchedaDiValutazioneLibro.fxml"));
            Parent root = loader.load();
            com.lab_lib.frontend.Pages.ValutaControllers ctrl = (com.lab_lib.frontend.Pages.ValutaControllers) loader.getController();
            java.util.function.LongConsumer noop = id -> {};
            ctrl.setContext(ratingService, personalLibraryService, b.getId(), b.getTitle(), noop);
            Stage stage = new Stage();
            stage.setTitle("Scheda di Valutazione del Libro");
            Scene scene = new Scene(root);
            // Optional: apply base CSS
            var base = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (base != null) scene.getStylesheets().add(base.toExternalForm());
            var buttons = getClass().getResource("/com/lab_lib/frontend/Css/Buttons.css");
            if (buttons != null) scene.getStylesheets().add(buttons.toExternalForm());
            stage.setScene(scene);
            stage.setWidth(900);
            stage.setHeight(620);
            stage.setResizable(true);
            stage.initOwner(GroupBooksTable.getScene().getWindow());
            stage.show();
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aprire la valutazione");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
        }
    }

    private void styleAlert(Alert alert) {
        try {
            var css1 = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (css1 != null) alert.getDialogPane().getStylesheets().add(css1.toExternalForm());
            var css2 = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (css2 != null) alert.getDialogPane().getStylesheets().add(css2.toExternalForm());
        } catch (Exception ignore) {}
    }
}
