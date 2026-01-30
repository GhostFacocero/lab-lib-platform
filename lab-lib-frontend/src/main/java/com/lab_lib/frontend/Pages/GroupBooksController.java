package com.lab_lib.frontend.Pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Pages.ValutaControllers;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupBooksController {

    @FXML private Label GroupTitleLabel;
    @FXML private TableView<Book> GroupBooksTable;
    @FXML private TableColumn<Book, String> ColTitle;
    @FXML private TableColumn<Book, String> ColAuthors;
    @FXML private Button BtnClose;

    private IPersonalLibraryService personalLibraryService;
    private long libraryId;
    private String groupName;
    private java.util.function.Consumer<Book> infoHandler;

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
        // Ensure only Title and Authors columns are present
        if (GroupBooksTable != null && ColTitle != null && ColAuthors != null) {
            GroupBooksTable.getColumns().setAll(ColTitle, ColAuthors);
            GroupBooksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
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

            MenuItem infoItem = new MenuItem("Information");
            infoItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b == null) return;
                if (infoHandler != null) {
                    infoHandler.accept(b);
                } else {
                    showBookInfo(b);
                }
            });

            MenuItem reviewItem = new MenuItem("Valutare");
            reviewItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b == null) return;
                openReviewWindowFor(b);
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

            menu.getItems().addAll(infoItem, reviewItem, removeItem);
            menu.getStyleClass().add("dark-menu");
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

    public void setInfoHandler(java.util.function.Consumer<Book> infoHandler) {
        this.infoHandler = infoHandler;
    }

    private void showBookInfo(Book b) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Informazioni libro");
        info.setHeaderText(b.getTitle() != null ? b.getTitle() : "-");
        String authors = Optional.ofNullable(b.getAuthors()).orElse(Collections.emptyList()).stream().collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder();
        sb.append("Autori: ").append(authors.isEmpty() ? "-" : authors).append("\n");
        if (b.getPublisher() != null) sb.append("Publisher: ").append(b.getPublisher()).append("\n");
        if (b.getPublishYear() != null) sb.append("Anno: ").append(b.getPublishYear()).append("\n");
        if (b.getDescription() != null) sb.append("Descrizione: ").append(b.getDescription());
        info.setContentText(sb.toString());
        styleAlert(info);
        info.showAndWait();
    }

    private void openReviewWindowFor(Book b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/SchedaDiValutazioneLibro.fxml"));
            Parent root = loader.load();
            var ctrl = (ValutaControllers) loader.getController();
            ctrl.setContext(b.getId(), b.getTitle() != null ? b.getTitle() : "-", id -> {
                try {
                    personalLibraryService.addBookToLibrary(libraryId, id);
                    boolean exists = GroupBooksTable.getItems().stream().anyMatch(x -> x.getId().equals(id));
                    if (!exists) {
                        // If not present, add a minimal Book entry with id and title
                        GroupBooksTable.getItems().add(b);
                    }
                } catch (Exception ex) {
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Errore");
                    err.setHeaderText("Impossibile aggiungere il libro al gruppo");
                    err.setContentText(ex.getMessage());
                    styleAlert(err);
                    err.showAndWait();
                }
            });

            Stage reviewStage = new Stage();
            reviewStage.setTitle("Scheda di Valutazione del Libro");
            reviewStage.setScene(new Scene(root));
            reviewStage.setResizable(false);
            reviewStage.initModality(Modality.WINDOW_MODAL);
            // owner: try GroupBooksTable scene window if available
            if (GroupBooksTable != null && GroupBooksTable.getScene() != null) {
                reviewStage.initOwner(GroupBooksTable.getScene().getWindow());
            }
            reviewStage.show();

        } catch (Exception e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aprire la scheda di valutazione");
            err.setContentText(e.getMessage());
            styleAlert(err);
            err.showAndWait();
        }
    }
}
