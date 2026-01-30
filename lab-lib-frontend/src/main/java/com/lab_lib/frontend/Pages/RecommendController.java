// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Pages;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Pages.Components.PaginatedTableView;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RecommendController {

    private final IRatingService ratingService;
    private final IBookService bookService;
    
    // Dati contestuali
    private Long sourceBookId; 
    private Runnable onCloseCallback; 

    // Stato
    private final List<Book> selectedBooks = new ArrayList<>();
    private final int MAX_SELECTION = 3;

    // Componenti FXML
    @FXML private Label selectionLabel;
    @FXML private HBox selectedBooksContainer;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button finishButton;
    @FXML private StackPane tableContainer;

    private PaginatedTableView<Book> table;

    @Inject
    public RecommendController(IRatingService ratingService, IBookService bookService) {
        this.ratingService = ratingService;
        this.bookService = bookService;
    }

    public void setContext(Long sourceBookId, Runnable onCloseCallback) {
        this.sourceBookId = sourceBookId;
        this.onCloseCallback = onCloseCallback;
        if (table != null) table.refresh();
    }

    @FXML
    public void initialize() {
        setupTable();
    }

    private void setupTable() {
        table = new PaginatedTableView<>(page -> {
            String query = searchField.getText();
            PaginatedResponse<Book> response;
            
            if (query == null || query.isBlank()) {
                 response = bookService.getBooks(page, 10);
            } else {
                 response = bookService.searchBooksByTitleOrAuthor(query, page, 10);
            }

            if (sourceBookId != null && response.getContent() != null) {
                try {
                    response.getContent().removeIf(b -> b.getId().equals(sourceBookId));
                } catch (UnsupportedOperationException e) {
                    System.err.println("Impossibile filtrare lista immutabile");
                }
            }
            return response;
        });

        // --- DEFINIZIONE COLONNE (Ordine modificato) ---

        // 1. PRIMA COLONNA: Azione (Aggiungi)
        TableColumn<Book, Book> actionCol = table.addColumn("Seleziona", b -> b);
        actionCol.setPrefWidth(120); // Larghezza fissa per i bottoni
        actionCol.setCellFactory(col -> new TableCell<Book, Book>() {
            private final Button btn = new Button("Aggiungi");
            {
                btn.setStyle("-fx-font-size: 12px; -fx-padding: 5 10;"); 
                btn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    addSelection(book);
                });
            }
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    boolean alreadySelected = selectedBooks.stream().anyMatch(b -> b.getId().equals(item.getId()));
                    boolean isSelf = (sourceBookId != null && sourceBookId.equals(item.getId()));
                    
                    if (alreadySelected) {
                        btn.setText("Presente");
                        btn.setDisable(true);
                        btn.setStyle("-fx-background-color: #444; -fx-text-fill: #aaa; -fx-font-size: 12px;");
                    } else {
                        btn.setText("Aggiungi");
                        btn.setDisable(isSelf || selectedBooks.size() >= MAX_SELECTION);
                        if (!btn.isDisabled()) btn.setStyle("-fx-font-size: 12px;"); 
                    }
                    setGraphic(btn);
                }
            }
        });
        
        // 2. SECONDA COLONNA: Titolo
        // Nota: Assicurati che "getTitle" sia il getter corretto nel tuo model Book
        TableColumn<Book, String> titleCol = table.addColumn("Titolo", Book::getTitle);
        titleCol.setPrefWidth(500); // Molto largo per riempire lo spazio

        // 3. NIENTE COLONNA AUTORI (Rimossa come richiesto)

        // Rimosso setColumnResizePolicy perché PaginatedTableView non lo supporta direttamente.
        // Abbiamo gestito la larghezza usando setPrefWidth sulle colonne qui sopra.

        tableContainer.getChildren().add(table);
    }

    @FXML
    private void doSearch() {
        if (table != null) table.refresh();
    }

    private void addSelection(Book book) {
        if (selectedBooks.size() >= MAX_SELECTION) return;
        if (selectedBooks.stream().anyMatch(b -> b.getId().equals(book.getId()))) return;
        
        selectedBooks.add(book);
        updateSelectionView();
        table.refresh();
    }

    private void removeSelection(Book book) {
        selectedBooks.remove(book);
        updateSelectionView();
        table.refresh();
    }

    private void updateSelectionView() {
        selectedBooksContainer.getChildren().clear();
        selectionLabel.setText("Selezionati: " + selectedBooks.size() + "/" + MAX_SELECTION);

        for (Book b : selectedBooks) {
            HBox tag = new HBox(8);
            tag.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 5 12; -fx-background-radius: 20; -fx-border-color: #3a3a3a; -fx-border-radius: 20;");
            tag.setAlignment(Pos.CENTER);

            Label lbl = new Label(limitText(b.getTitle(), 20));
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
            
            Button removeBtn = new Button("×");
            removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6b6b; -fx-font-weight: bold; -fx-padding: 0; -fx-font-size: 16px; -fx-cursor: hand;");
            removeBtn.setOnAction(e -> removeSelection(b));

            tag.getChildren().addAll(lbl, removeBtn);
            selectedBooksContainer.getChildren().add(tag);
        }
    }

    @FXML
    private void submitAndClose() {
        if (sourceBookId != null) {
            for (Book recommended : selectedBooks) {
                try {
                    ratingService.addRatingRecommendation(sourceBookId, recommended.getId());
                    System.out.println("Raccomandazione inviata: " + sourceBookId + " -> " + recommended.getId());
                } catch (Exception e) {
                    System.err.println("Errore invio raccomandazione: " + e.getMessage());
                }
            }
        }

        Stage currentStage = (Stage) finishButton.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }

        if (onCloseCallback != null) {
            try { onCloseCallback.run(); } catch (Exception ignored) {}
        }
    }

    private String limitText(String text, int len) {
        if (text == null) return "";
        return text.length() > len ? text.substring(0, len) + "..." : text;
    }
}