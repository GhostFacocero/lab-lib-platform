// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Pages;

import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Models.PersonalLibrary;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import java.util.Collections;
import java.util.Optional;

public class GroupListController {
    @FXML private TableView<PersonalLibrary> groupsTable;
    @FXML private TableColumn<PersonalLibrary, String> nameColumn;
    @FXML private TableColumn<PersonalLibrary, String> ownerColumn;
    @FXML private Button openButton;
    @FXML private Button closeButton;

    private IPersonalLibraryService personalLibraryService;

    public void setContext(IPersonalLibraryService pls) {
        this.personalLibraryService = pls;
        setupTable();
        loadGroups();
    }

    @FXML
    public void initialize() {
        if (openButton != null) {
            openButton.setOnAction(e -> openSelectedGroup());
        }
        if (closeButton != null) {
            closeButton.setOnAction(e -> closeWindow());
        }
    }

    private void setupTable() {
        if (nameColumn != null) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        }
        if (ownerColumn != null) {
            ownerColumn.setCellValueFactory(cell -> new SimpleStringProperty(Optional.ofNullable(cell.getValue().getUserNickname()).orElse("-")));
        }

        groupsTable.setRowFactory(tv -> {
            TableRow<PersonalLibrary> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            // Ensure context menu uses app styles
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
            MenuItem openItem = new MenuItem("Apri");
            openItem.setOnAction(e -> {
                PersonalLibrary lib = row.getItem();
                if (lib != null) openGroupBooks(lib);
            });
            MenuItem delItem = new MenuItem("Elimina");
            delItem.setOnAction(e -> deleteGroup(row.getItem()));
            menu.getItems().addAll(openItem, delItem);
            row.setContextMenu(menu);

            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) return;
                if (e.getClickCount() == 2 && e.isPrimaryButtonDown()) {
                    PersonalLibrary lib = row.getItem();
                    if (lib != null) openGroupBooks(lib);
                }
            });
            return row;
        });
    }

    private void loadGroups() {
        try {
            var libs = personalLibraryService.getPersonalLibraries();
            groupsTable.getItems().setAll(Optional.ofNullable(libs).orElse(Collections.emptyList()));
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Caricamento gruppi fallito");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[Groups] Failed to load in GroupListController: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void openSelectedGroup() {
        PersonalLibrary sel = groupsTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert warn = new Alert(Alert.AlertType.WARNING);
            warn.setTitle("Nessuna selezione");
            warn.setHeaderText("Seleziona un gruppo dalla tabella");
            styleAlert(warn);
            warn.showAndWait();
            return;
        }
        openGroupBooks(sel);
    }

    private void openGroupBooks(PersonalLibrary lib) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/GroupBooks.fxml"));
            javafx.scene.Parent root = loader.load();
            GroupBooksController ctrl = (GroupBooksController) loader.getController();
            ctrl.setContext(personalLibraryService, lib.getId(), lib.getName());
            javafx.stage.Stage dlg = new javafx.stage.Stage();
            dlg.setTitle("Libri del gruppo: " + lib.getName());
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            var css = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            dlg.setScene(scene);
            dlg.setResizable(false);
            dlg.initModality(javafx.stage.Modality.NONE);
            dlg.show();
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aprire i libri del gruppo");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[Groups] Failed to open GroupBooks from GroupListController: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteGroup(PersonalLibrary lib) {
        if (lib == null) return;
        try {
            personalLibraryService.deletePersonalLibrary(lib.getId());
            groupsTable.getItems().remove(lib);
            System.out.println("[Groups] Deleted group '" + lib.getName() + "' (id=" + lib.getId() + ") from GroupListController");
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Eliminazione gruppo fallita");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[Groups] Failed to delete group in GroupListController: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void closeWindow() {
        try {
            ((javafx.stage.Stage) groupsTable.getScene().getWindow()).close();
        } catch (Exception ignore) {}
    }

    private void styleAlert(Alert a) {
        try {
            var css = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (css != null) a.getDialogPane().getStylesheets().add(css.toExternalForm());
            a.getDialogPane().getStyleClass().add("dialog-pane");
        } catch (Exception ignore) {}
    }
}