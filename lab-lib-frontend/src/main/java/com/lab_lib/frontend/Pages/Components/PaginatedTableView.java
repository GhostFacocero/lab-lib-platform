package com.lab_lib.frontend.Pages.Components;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

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

    private final Function<Integer, PaginatedData<T>> dataFetcher;

    public PaginatedTableView(Function<Integer, PaginatedData<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
        
        // Load FXML
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
    }

    private void initialize() {
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            loadPage(newVal.intValue());
        });
    }

    private void loadPage(int page) {
        try {
            PaginatedData<T> response = dataFetcher.apply(page);
            ObservableList<T> items = FXCollections.observableArrayList(response.getContent());
            tableView.setItems(items);
            
            pagination.setPageCount(response.getTotalPages());
            pagination.setCurrentPageIndex(response.getPageNumber());
        } catch (Exception e) {
            // Log the exception (replace with your preferred logging framework)
            System.err.println("Error loading page: " + e.getMessage());
            // Handle error
        }
    }

    public <R> void addColumn(String columnName, Function<T, R> property) {
        TableColumn<T, R> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(property.apply(cellData.getValue())));
        tableView.getColumns().add(column);
    }

    // Refresh the current page
    public void refresh() {
        loadPage(pagination.getCurrentPageIndex());
    }

    public static class PaginatedData<T> {
        private final List<T> content;
        private final int totalPages;
        private final int pageNumber;

        public PaginatedData(List<T> content, int totalPages, int pageNumber) {
            this.content = content;
            this.totalPages = totalPages;
            this.pageNumber = pageNumber;
        }

        public List<T> getContent() {
            return content;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public int getPageNumber() {
            return pageNumber;
        }
    }
}