package com.lab_lib.frontend.Pages.Components;

import java.io.IOException;
import java.util.function.Function;

import com.lab_lib.frontend.Models.PaginatedResponse;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PaginatedTableView<T> extends VBox {
    @FXML
    private TableView<T> tableView;
    @FXML
    private Pagination pagination;

    private final SimpleObjectProperty<ObservableList<T>> contentProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private final SimpleIntegerProperty totalPagesProperty = new SimpleIntegerProperty(1);
    private final SimpleIntegerProperty currentPageIndexProperty = new SimpleIntegerProperty(0);

    private final Function<Integer, PaginatedResponse<T>> dataFetcher;

    public PaginatedTableView(Function<Integer, PaginatedResponse<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/lab_lib/frontend/Pages/Components/paginated-table.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
            initialize();
            loadPage(0);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load table FXML", e);
        }

        tableView.itemsProperty().bind(contentProperty);
        pagination.pageCountProperty().bind(totalPagesProperty);
        pagination.currentPageIndexProperty().bindBidirectional(currentPageIndexProperty);

        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            loadPage(newVal.intValue());
        });
    }

    private void initialize() {
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            loadPage(newVal.intValue());
        });
    }

    private void loadPage(int page) {
        try {
            PaginatedResponse<T> response = dataFetcher.apply(page);

            // update reactive properties
            contentProperty.set(FXCollections.observableArrayList(response.getContent()));
            totalPagesProperty.set(response.getTotalPages());
            currentPageIndexProperty.set(response.getNumber());
        } catch (Exception e) {
            System.err.println("Error loading page: " + e.getMessage());
        }
    }

    public <R> TableColumn<T, R> addColumn(String columnName, Function<T, R> property) {
        TableColumn<T, R> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(property.apply(cellData.getValue())));
        tableView.getColumns().add(column);
        return column;
    }

    // Refresh the current page
    public void refresh() {
        loadPage(currentPageIndexProperty.get());
    }
}