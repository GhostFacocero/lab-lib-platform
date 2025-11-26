package com.lab_lib.frontend.Pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

/**
 * Controller per la gestione del pannello di valutazione di un libro.
 * Consente di valutare diverse categorie con stelle e commenti testuali.
 */
public class ValutaControllers {

    // Contenitori HBox per le stelle di ogni categoria
    @FXML private HBox HBOXStartsEdizioneOriginalita1;
    @FXML private HBox HBOXStartsValutaGradevolezza;
    @FXML private HBox HBOXStartsValutaOriginalita;
    @FXML private HBox HBOXStartsValutaPaneContenuto1;
    @FXML private HBox HBOXStartsValutaPaneStile;

    // Bottoni per navigare tra i pannelli di valutazione
    @FXML private Button ValutaNextContenutoButtom1;
    @FXML private Button ValutaNextGradevolezzaButtom;
    @FXML private Button ValutaNextOriginalitaButtom;
    @FXML private Button ValutaNextSendButtom1;
    @FXML private Button ValutaNextStileButtom;

    // Bottone per tornare indietro
    @FXML private Label BackButtom;

    // Contenitori VBox per i diversi pannelli di valutazione
    @FXML private VBox ValutaPaneContenuto;
    @FXML private VBox ValutaPaneEdizione;
    @FXML private VBox ValutaPaneGradevolezza;
    @FXML private VBox ValutaPaneOriginalita;
    @FXML private VBox ValutaPaneStile;

    // Label per visualizzare il nome del libro
    @FXML private Label ValutaPanelNameLibro;

    // TextArea per commenti nelle varie categorie
    @FXML private TextArea ValutaTextStile;
    @FXML private TextArea ValutaTextContenuto;
    @FXML private TextArea ValutaTextGradevolezza;
    @FXML private TextArea ValutaTextOriginalita;
    @FXML private TextArea ValutaTextEdizione;

    private final int MAX_STARS = 5;

    // Array di ImageView per le stelle di ogni categoria
    private ImageView[] starsStile = new ImageView[MAX_STARS];
    private ImageView[] starsContenuto = new ImageView[MAX_STARS];
    private ImageView[] starsGradevolezza = new ImageView[MAX_STARS];
    private ImageView[] starsOriginalita = new ImageView[MAX_STARS];
    private ImageView[] starsEdizione = new ImageView[MAX_STARS];

    // Valutazioni correnti (numero stelle) per ogni categoria
    private int ratingStile = 0;
    private int ratingContenuto = 0;
    private int ratingGradevolezza = 0;
    private int ratingOriginalita = 0;
    private int ratingEdizione = 0;

    /**
     * Metodo chiamato automaticamente dopo il caricamento del FXML.
     * Imposta placeholder, limita la lunghezza testo e inizializza le stelle e pulsanti.
     */
    @FXML
    public void initialize() {
        // Placeholder per i campi testo (max 256 caratteri)
        ValutaTextStile.setPromptText("Scrivi qui (max 256 caratteri)");
        ValutaTextContenuto.setPromptText("Scrivi qui (max 256 caratteri)");
        ValutaTextGradevolezza.setPromptText("Scrivi qui (max 256 caratteri)");
        ValutaTextOriginalita.setPromptText("Scrivi qui (max 256 caratteri)");
        ValutaTextEdizione.setPromptText("Scrivi qui (max 256 caratteri)");

        // Limita la lunghezza del testo in ogni TextArea
        limitTextFieldLength(ValutaTextStile, 256);
        limitTextFieldLength(ValutaTextContenuto, 256);
        limitTextFieldLength(ValutaTextGradevolezza, 256);
        limitTextFieldLength(ValutaTextOriginalita, 256);
        limitTextFieldLength(ValutaTextEdizione, 256);

        // Mostra inizialmente solo il pannello Stile
        ValutaPaneStile.setVisible(true);
        ValutaPaneContenuto.setVisible(false);
        ValutaPaneGradevolezza.setVisible(false);
        ValutaPaneOriginalita.setVisible(false);
        ValutaPaneEdizione.setVisible(false);

        // Inizializza le stelle in ogni pannello con gli handler per hover e click
        initStars(HBOXStartsValutaPaneStile, starsStile,
                  () -> updateStarsDisplay(starsStile, ratingStile),
                  (newRating) -> ratingStile = newRating);

        initStars(HBOXStartsValutaPaneContenuto1, starsContenuto,
                  () -> updateStarsDisplay(starsContenuto, ratingContenuto),
                  (newRating) -> ratingContenuto = newRating);

        initStars(HBOXStartsValutaGradevolezza, starsGradevolezza,
                  () -> updateStarsDisplay(starsGradevolezza, ratingGradevolezza),
                  (newRating) -> ratingGradevolezza = newRating);

        initStars(HBOXStartsValutaOriginalita, starsOriginalita,
                  () -> updateStarsDisplay(starsOriginalita, ratingOriginalita),
                  (newRating) -> ratingOriginalita = newRating);

        initStars(HBOXStartsEdizioneOriginalita1, starsEdizione,
                  () -> updateStarsDisplay(starsEdizione, ratingEdizione),
                  (newRating) -> ratingEdizione = newRating);

        // Setup dei bottoni di navigazione tra i pannelli
        setupButtons();

        // Bottone Indietro per tornare al pannello precedente o chiudere la finestra
        BackButtom.setOnMouseClicked(e -> onBackButton());
    }

    /**
     * Inizializza le stelle in un contenitore HBox, impostando eventi hover e click.
     * @param container HBox dove inserire le stelle
     * @param starsArray array dove salvare le ImageView delle stelle
     * @param updateDisplay funzione per aggiornare la visualizzazione delle stelle
     * @param setRating funzione per salvare il rating selezionato
     */
    private void initStars(HBox container, ImageView[] starsArray, Runnable updateDisplay, java.util.function.IntConsumer setRating) {
        container.getChildren().clear();
        container.setSpacing(15);
        container.setAlignment(Pos.TOP_CENTER);
        for (int i = 0; i < MAX_STARS; i++) {
            int index = i;
            ImageView star = new ImageView(getStarImage(false));
            star.setFitWidth(40);
            star.setFitHeight(40);
            star.setTranslateY(20);
            star.setStyle("-fx-cursor: hand;");

            star.setOnMouseEntered(e -> updateStarsDisplay(starsArray, index + 1));
            star.setOnMouseExited(e -> updateDisplay.run());
            star.setOnMouseClicked(e -> {
                setRating.accept(index + 1);
                updateDisplay.run();
            });

            starsArray[i] = star;
            container.getChildren().add(star);
        }
    }

    /**
     * Aggiorna la visualizzazione delle stelle (piene o vuote) in base al rating.
     */
    private void updateStarsDisplay(ImageView[] starsArray, int activeStars) {
        for (int i = 0; i < MAX_STARS; i++) {
            boolean filled = i < activeStars;
            starsArray[i].setImage(getStarImage(filled));
        }
    }

    /**
     * Configura i bottoni per la navigazione tra i pannelli di valutazione.
     */
    private void setupButtons() {
        ValutaNextStileButtom.setOnAction(e -> {
            ValutaPaneStile.setVisible(false);
            ValutaPaneContenuto.setVisible(true);
        });

        ValutaNextContenutoButtom1.setOnAction(e -> {
            ValutaPaneContenuto.setVisible(false);
            ValutaPaneGradevolezza.setVisible(true);
        });

        ValutaNextGradevolezzaButtom.setOnAction(e -> {
            ValutaPaneGradevolezza.setVisible(false);
            ValutaPaneOriginalita.setVisible(true);
        });

        ValutaNextOriginalitaButtom.setOnAction(e -> {
            ValutaPaneOriginalita.setVisible(false);
            ValutaPaneEdizione.setVisible(true);
        });

        ValutaNextSendButtom1.setOnAction(e -> submitAndClose());
    }

    /**
     * Gestisce il bottone Indietro per tornare al pannello precedente o chiudere la finestra.
     */
    private void onBackButton() {
        if (ValutaPaneEdizione.isVisible()) {
            ValutaPaneEdizione.setVisible(false);
            ValutaPaneOriginalita.setVisible(true);
        } else if (ValutaPaneOriginalita.isVisible()) {
            ValutaPaneOriginalita.setVisible(false);
            ValutaPaneGradevolezza.setVisible(true);
        } else if (ValutaPaneGradevolezza.isVisible()) {
            ValutaPaneGradevolezza.setVisible(false);
            ValutaPaneContenuto.setVisible(true);
        } else if (ValutaPaneContenuto.isVisible()) {
            ValutaPaneContenuto.setVisible(false);
            ValutaPaneStile.setVisible(true);
        } else if (ValutaPaneStile.isVisible()) {
            closeWindow();
        }
    }

    /**
     * Stampa i dati di valutazione e chiude la finestra.
     */
    private void submitAndClose() {
        System.out.println("Valutazione finale:");
        System.out.println("- Stile: " + ValutaTextStile.getText() + " | Stelle: " + ratingStile);
        System.out.println("- Contenuto: " + ValutaTextContenuto.getText() + " | Stelle: " + ratingContenuto);
        System.out.println("- Gradevolezza: " + ValutaTextGradevolezza.getText() + " | Stelle: " + ratingGradevolezza);
        System.out.println("- Originalità: " + ValutaTextOriginalita.getText() + " | Stelle: " + ratingOriginalita);
        System.out.println("- Edizione: " + ValutaTextEdizione.getText() + " | Stelle: " + ratingEdizione);

        closeWindow();
    }

    /**
     * Chiude la finestra corrente.
     */
    private void closeWindow() {
        Stage stage = (Stage) ValutaNextSendButtom1.getScene().getWindow();
        stage.close();
    }

    /**
     * Restituisce l'immagine della stella piena o vuota.
     * @param filled true se la stella è piena, false se vuota
     */
    private Image getStarImage(boolean filled) {
        String path = filled ? "/Img/StarColor.png" : "/Img/StarNoColor.png";
        return new Image(getClass().getResourceAsStream(path));
    }

    /**
     * Limita la lunghezza massima di testo in una TextArea.
     * @param TTextArea TextArea da limitare
     * @param maxLength lunghezza massima permessa
     */
    private void limitTextFieldLength(TextArea TTextArea, int maxLength) {
        TTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > maxLength) {
                TTextArea.setText(oldValue);
            }
        });
    }
}
