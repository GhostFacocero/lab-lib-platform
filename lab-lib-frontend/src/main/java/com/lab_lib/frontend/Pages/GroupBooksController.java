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
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
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
    private long libraryId;
    private String groupName;

    public void setContext(IPersonalLibraryService personalLibraryService, long libraryId, String groupName) {
        this.personalLibraryService = personalLibraryService;
        this.libraryId = libraryId;
        this.groupName = groupName;
        if (GroupTitleLabel != null) GroupTitleLabel.setText("Libri del gruppo: " + groupName);
        setupTable();
        loadBooks();
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
            // Apply app CSS to the context menu
            menu.getStyleClass().add("dark-menu");
            menu.setOnShown(ev -> {
                try {
                    var styles = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
                    if (styles != null) menu.getScene().getStylesheets().add(styles.toExternalForm());
                    var base = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
                    if (base != null) menu.getScene().getStylesheets().add(base.toExternalForm());
                    var buttons = getClass().getResource("/com/lab_lib/frontend/Css/Buttons.css");
                    if (buttons != null) menu.getScene().getStylesheets().add(buttons.toExternalForm());
                } catch (Exception ignore) {}
            });
            MenuItem removeItem = new MenuItem("Elimina dal gruppo");
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
            menu.getItems().add(removeItem);
            row.setContextMenu(menu);
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

    private void styleAlert(Alert alert) {
        try {
            var css1 = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (css1 != null) alert.getDialogPane().getStylesheets().add(css1.toExternalForm());
            var css2 = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (css2 != null) alert.getDialogPane().getStylesheets().add(css2.toExternalForm());
        } catch (Exception ignore) {}
    }
}
