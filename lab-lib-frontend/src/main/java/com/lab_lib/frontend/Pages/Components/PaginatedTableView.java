package com.lab_lib.frontend.Pages.Components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.function.Function;

public class PaginatedTableView<T> extends VBox {
    private final TableView<T> tableView;
    private final Pagination pagination;
    private final Function<Integer, PaginatedData<T>> dataFetcher;

    public PaginatedTableView(Function<Integer, PaginatedData<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
        this.tableView = new TableView<>();
        this.pagination = new Pagination();

        initialize();
        loadPage(0); // Load first page
    }

    private void initialize() {
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            loadPage(newVal.intValue());
        });

        this.getChildren().addAll(tableView, pagination);
    }

    private void loadPage(int page) {
        try {
            PaginatedData<T> response = dataFetcher.apply(page);
            ObservableList<T> items = FXCollections.observableArrayList(response.getContent());
            tableView.setItems(items);
            
            // Update pagination
            pagination.setPageCount(response.getTotalPages());
            pagination.setCurrentPageIndex(response.getPageNumber());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error (show alert, etc.)
        }
    }

    public <R> void addColumn(String columnName, Function<T, R> property) {
        TableColumn<T, R> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<R>(property.apply(cellData.getValue())));
        tableView.getColumns().add(column);
    }

    // Generic paginated data container
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