package com.lab_lib.frontend.Pages;

import java.io.IOException;

import com.lab_lib.frontend.Services.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainBookStoreControllers {

    @FXML
    private AnchorPane ArchorPaneLibreriaMain;

    @FXML
    private AnchorPane ArchorPaneLibriMain;

    @FXML
    private Button Btm_Gruppi;

    @FXML
    private ListView<?> ListaGruppi;

    @FXML
    private Button Btm_libreriaMain;

    @FXML
    private VBox GruppiPanel;
    
    @FXML
    private TableView<Book> tableLibriLibreria;

    @FXML
    private Button Btm_libriMain;

    @FXML
    private HBox HBoxContenutoGeneralReviesStarts;

    @FXML
    private HBox HBoxEdizioneGeneralReviesStarts;

    @FXML
    private HBox HBoxGradevolezzaGeneralReviesStarts;

    @FXML
    private HBox HBoxOriginalitaGeneralReviesStarts;

    @FXML
    private HBox HBoxStileGeneralReviesStarts;

    @FXML
    private HBox HBoxVotoFinaleGeneralReviesStarts;



    @FXML
    private StackPane StackPaneForLibreriaLogatta;

    @FXML
    private VBox VboxMenuOptions;

    @FXML
    private VBox hamburgerButton;

    @FXML
    private Label menuLabel;

    @FXML
    private HBox rootLayout;

    @FXML
    private VBox sideBar;

    @FXML
    void ToLibrerii(ActionEvent event) {}

    @FXML
    void makeClickable(MouseEvent event) {}

    @FXML
    void makeDefault(DragEvent event) {}

    @FXML
    void toLibriLogin(ActionEvent event) {}

    @FXML
    void toggleSidebar(ActionEvent event) {}
    
    @FXML
    private VBox ItemsMenu;
    @FXML
    private HBox PanelDataLibroGeneralReviews;

    private final int MAX_STARS = 5;
    private boolean isMenuCollapsed = false;
    private ObservableList<Book> libriData = FXCollections.observableArrayList();
    
    @FXML
    private Button LogOut;
    
    @FXML
    public void initialize() {

        // Imposta visibilità iniziali: mostra i libri, nasconde libreria e dettagli libro
        ArchorPaneLibriMain.setVisible(true);
        ArchorPaneLibreriaMain.setVisible(false);
        PanelDataLibroGeneralReviews.setVisible(false);


        // Listener per selezione libro in tabella
        tableLibriLibreria.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Quando un libro viene selezionato, mostra il pannello dei dettagli e nasconde libreria
                ArchorPaneLibreriaMain.setVisible(false);
                PanelDataLibroGeneralReviews.setVisible(true);
                GruppiPanel.setVisible(false);
                // Qui puoi aggiungere codice per aggiornare il pannello con i dati del libro selezionato
            }
        });

        // Bottoni per passare tra schermate libri e libreria
        Btm_Gruppi.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(true);
        });

          Btm_libriMain.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(true);
            ArchorPaneLibreriaMain.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            tableLibriLibreria.getSelectionModel().clearSelection();
        });


        Btm_libreriaMain.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(true);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            tableLibriLibreria.getSelectionModel().clearSelection();
        });

        // Bottone logout apre il pannello login/registrazione e chiude questa finestra
        LogOut.setOnAction(e -> openLogRegMainPanelAndCloseCurrent());

        // Toggle menu dimensioni con clic sull'icona hamburger
        hamburgerButton.setOnMouseClicked(e -> toggleMenuSize());

        // Imposta stelle valutazioni a zero all'avvio
        setStarRating(HBoxContenutoGeneralReviesStarts, 0);
        setStarRating(HBoxEdizioneGeneralReviesStarts, 0);
        setStarRating(HBoxGradevolezzaGeneralReviesStarts, 0);
        setStarRating(HBoxStileGeneralReviesStarts, 0);
        setStarRating(HBoxVotoFinaleGeneralReviesStarts, 0);
        setStarRating(HBoxOriginalitaGeneralReviesStarts, 0);
    }

    /**
     * Metodo privato per impostare il rating a stelle in un contenitore HBox.
     * 
     * @param targetBox HBox in cui inserire le immagini delle stelle
     * @param rating valore da 0 a MAX_STARS delle stelle piene
     */
    private void setStarRating(HBox targetBox, int rating) {
        targetBox.getChildren().clear();

        for (int i = 1; i <= MAX_STARS; i++) {
            ImageView starImageView = new ImageView();
            starImageView.setFitWidth(18);
            starImageView.setFitHeight(18);

            if (i <= rating) {
                starImageView.setImage(getStarImage(true));
            } else {
                starImageView.setImage(getStarImage(false));
            }

            targetBox.getChildren().add(starImageView);
        }
    }

    /**
     * Restituisce l'immagine della stella piena o vuota.
     * 
     * @param filled true per stella piena, false per vuota
     * @return immagine della stella
     */
    private Image getStarImage(boolean filled) {
        String path = filled ? "/Img/StarColor.png" : "/Img/StarNoColor.png";
        return new Image(getClass().getResourceAsStream(path));
    }

    /**
     * Cambia la dimensione del menu (collassa o espande).
     */
    private void toggleMenuSize() {
        if (isMenuCollapsed) {
            VboxMenuOptions.setPrefWidth(142);
            VboxMenuOptions.setMinWidth(142);
            VboxMenuOptions.setMaxWidth(142);
            ItemsMenu.setVisible(true);
            menuLabel.setVisible(true);
        } else {
            VboxMenuOptions.setPrefWidth(65);
            VboxMenuOptions.setMinWidth(65);
            VboxMenuOptions.setMaxWidth(65);
            ItemsMenu.setVisible(false);
            menuLabel.setVisible(false);
        }
        isMenuCollapsed = !isMenuCollapsed;
    }

    /**
     * Apre il pannello login/registrazione e chiude la finestra corrente.
     */
    private void openLogRegMainPanelAndCloseCurrent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/LogRegMainPanel.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Login/Register Panel");
            newStage.show();

            // Chiude la finestra corrente
            Stage currentStage = (Stage) Btm_libriMain.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Impossibile aprire LogRegMainPanel.fxml");
        }
    }
    

    /**
     * Metodo pubblico per aggiungere un libro dinamicamente alla lista e aggiornarla.
     * 
     * @param titolo titolo del libro
     * @param autore autore del libro
     */
    public void addLibro(String titolo, String autore) {
        libriData.add(new Book(titolo, autore));
    }

    /**
     * Gestisce l'apertura della finestra di valutazione del libro.
     * 
     * @param event evento del click sul bottone
     */
    @FXML
    private void handleOpenReviewWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/SchedaDiValutazioneLibro.fxml"));
            Parent root = loader.load();

            Stage reviewStage = new Stage();
            reviewStage.setTitle("Scheda di Valutazione del Libro");
            reviewStage.setScene(new Scene(root));
            reviewStage.initModality(Modality.WINDOW_MODAL); // blocca l'interazione con la finestra principale
            reviewStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // imposta la finestra principale come owner
            reviewStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
