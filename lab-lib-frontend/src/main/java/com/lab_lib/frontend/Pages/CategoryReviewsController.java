// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Pages;

import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Models.RatingDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryReviewsController {

    @FXML private Label CategoryTitle;
    @FXML private TableView<RatingDTO> ReviewsTable;
    @FXML private TableColumn<RatingDTO, String> UserColumn;
    @FXML private TableColumn<RatingDTO, Integer> StarsColumn;
    @FXML private TableColumn<RatingDTO, String> CommentColumn;
    @FXML private Button CloseButton;

    private IRatingService ratingService;
    private Long bookId;
    private String ratingName;

    public void setContext(IRatingService ratingService, Long bookId, String ratingName, String bookTitle) {
        this.ratingService = ratingService;
        this.bookId = bookId;
        this.ratingName = ratingName;
        if (CategoryTitle != null) {
            CategoryTitle.setText("Reviews: " + ratingName + " — " + (bookTitle != null ? bookTitle : ""));
        }
        loadData();
    }

    @FXML
    public void initialize() {
        if (UserColumn != null) {
            UserColumn.setCellValueFactory(new PropertyValueFactory<>("userNickname"));
        }
        if (CommentColumn != null) {
            CommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
            CommentColumn.setCellFactory(commentWrappingCellFactory());
        }
        if (StarsColumn != null) {
            StarsColumn.setCellValueFactory(new PropertyValueFactory<>("evaluation"));
            StarsColumn.setCellFactory(starCellFactory());
        }
    }

    private Callback<TableColumn<RatingDTO, Integer>, TableCell<RatingDTO, Integer>> starCellFactory() {
        return col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                int rating = Math.max(0, Math.min(5, value));
                HBox box = new HBox(2);
                for (int i = 1; i <= 5; i++) {
                    ImageView iv = new ImageView(getStarImage(i <= rating));
                    iv.setFitWidth(14);
                    iv.setFitHeight(14);
                    box.getChildren().add(iv);
                }
                setGraphic(box);
                setText(null);
            }
        };
    }

    private Image getStarImage(boolean filled) {
        String path = filled ? "/Img/StarColor.png" : "/Img/StarNoColor.png";
        return new Image(getClass().getResourceAsStream(path));
    }

    private Callback<TableColumn<RatingDTO, String>, TableCell<RatingDTO, String>> commentWrappingCellFactory() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.setWrapText(true);
                label.setStyle("-fx-padding: 4 8 4 8;");
                label.maxWidthProperty().bind(col.widthProperty().subtract(16));
            }
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null || value.isBlank()) {
                    setGraphic(null);
                    setText(null);
                } else {
                    label.setText(value);
                    setGraphic(label);
                    setText(null);
                }
            }
        };
    }

    private void loadData() {
        try {
            List<RatingDTO> all = ratingService.getRatingsByBookId(bookId);
            List<RatingDTO> filtered = all.stream()
                .filter(r -> ratingName.equals(r.getRatingName()))
                .collect(Collectors.toList());
            ReviewsTable.getItems().setAll(filtered);
        } catch (Exception ex) {
            System.err.println("Failed to fetch reviews for book " + bookId + " rating '" + ratingName + "': " + ex.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
}
