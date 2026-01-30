package com.lab_lib.frontend.Pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller per la gestione del pannello di valutazione di un libro.
 * Consente di valutare diverse categorie con stelle e commenti testuali.
 */
public class ValutaControllers {
    // 1. INIEZIONE DIRETTA DEI SERVIZI E DELL'INJECTOR
    @Inject
    private com.lab_lib.frontend.Interfaces.IRatingService ratingService;
    
    @Inject
    private com.lab_lib.frontend.Interfaces.IPersonalLibraryService personalLibraryService;
    
    @Inject
    private Injector injector; // Guice si inietta da solo!

    private Long bookId;

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
    @FXML private Button AddToLibraryButton;

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
        // Configurable mapping from UI labels to backend rating names
        initCategoryMap();
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

        if (AddToLibraryButton != null) {
            AddToLibraryButton.setOnAction(e -> handleAddToLibrary());
        }
    }

    private java.util.function.LongConsumer onAddToLibrary;

    public void setContext(Long bookId,
                           String bookTitle,
                           java.util.function.LongConsumer onAddToLibrary) {
        // NON passiamo più i servizi qui, ce li ha già dati Guice!
        this.bookId = bookId;
        this.onAddToLibrary = onAddToLibrary;
        if (ValutaPanelNameLibro != null && bookTitle != null) {
            ValutaPanelNameLibro.setText(bookTitle);
        }
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
            // Rimosso submitIfAny
            ValutaPaneStile.setVisible(false);
            ValutaPaneContenuto.setVisible(true);
        });

        ValutaNextContenutoButtom1.setOnAction(e -> {
            // Rimosso submitIfAny
            ValutaPaneContenuto.setVisible(false);
            ValutaPaneGradevolezza.setVisible(true);
        });

        ValutaNextGradevolezzaButtom.setOnAction(e -> {
            // Rimosso submitIfAny
            ValutaPaneGradevolezza.setVisible(false);
            ValutaPaneOriginalita.setVisible(true);
        });

        ValutaNextOriginalitaButtom.setOnAction(e -> {
            // Rimosso submitIfAny
            ValutaPaneOriginalita.setVisible(false);
            ValutaPaneEdizione.setVisible(true);
        });

        // L'invio avviene solo qui
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

    private static class ReviewData {
        String category;
        int rating;
        TextArea textArea;

        public ReviewData(String category, int rating, TextArea textArea) {
            this.category = category;
            this.rating = rating;
            this.textArea = textArea;
        }
    }

    private void submitAndClose() {
        try {
            if (ratingService != null && bookId != null) {
                
                // 1. Creiamo una lista con tutti i dati raccolti
                List<ReviewData> reviewsToSend = new ArrayList<>();
                reviewsToSend.add(new ReviewData("Stile", ratingStile, ValutaTextStile));
                reviewsToSend.add(new ReviewData("Contenuto", ratingContenuto, ValutaTextContenuto));
                reviewsToSend.add(new ReviewData("Gradevolezza", ratingGradevolezza, ValutaTextGradevolezza));
                reviewsToSend.add(new ReviewData("Originalita", ratingOriginalita, ValutaTextOriginalita));
                reviewsToSend.add(new ReviewData("Edizione", ratingEdizione, ValutaTextEdizione));

                // 2. Ciclo FOR per inviare tutto
                System.out.println("[ValutaControllers] Inizio invio batch delle recensioni...");
                for (ReviewData review : reviewsToSend) {
                    // submitIfAny controlla da solo se c'è un voto o un commento valido
                    submitIfAny(review.category, review.rating, safeTxt(review.textArea));
                }
            }
            
            // 3. Logica di passaggio alla pagina Recommend (uguale a prima)
            if (injector != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/Recommend.fxml"));
                loader.setControllerFactory(injector::getInstance);
                Parent root = loader.load();
                
                RecommendController recommendPage = loader.getController();
                recommendPage.setContext(this.bookId, this::closeWindow);
                
                Stage currentStage = (Stage) ValutaNextSendButtom1.getScene().getWindow();
                Scene scene = new Scene(root);
                currentStage.setScene(scene);
                currentStage.centerOnScreen();
            } else {
                closeWindow();
            }

        } catch (Exception ex) {
            System.err.println("[ValutaControllers] submitAndClose FAILED -> " + ex.getMessage());
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Errore");
            a.setHeaderText("Impossibile inviare le valutazioni");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }


     //non so nemmeno come ho fatto a funzionare questo ma lo ho fatto   
     
    private void handleAddToLibrary() {
        try {
            if (personalLibraryService == null || bookId == null) {
                throw new IllegalStateException("Servizio libreria o libro non impostato");
            }
            if (onAddToLibrary != null) {
                onAddToLibrary.accept(bookId); // Optimistic UI stamp
                System.out.println("[PersonalLibrary] UI stamped from review for book id=" + bookId);
            }
            var libs = personalLibraryService.getPersonalLibraries();
            Long libId;
            String libName;
            if (libs != null && !libs.isEmpty()) {
                libId = libs.get(0).getId();
                libName = libs.get(0).getName();
            } else {
                var def = personalLibraryService.getDefaultPersonalLibrary();
                if (def == null) {
                    def = personalLibraryService.createDefaultPersonalLibrary();
                }
                if (def == null) {
                    System.out.println("[PersonalLibrary] No backend default; kept UI-only add from review.");
                    return;
                }
                libId = def.getId();
                libName = def.getName();
            }
            personalLibraryService.addBookToLibrary(libId, bookId);
            Alert ok = new Alert(AlertType.INFORMATION);
            ok.setTitle("Aggiunto");
            ok.setHeaderText("Libro aggiunto alla libreria");
            ok.setContentText("Aggiunto a '" + libName + "'.");
            ok.showAndWait();
        } catch (Exception ex) {
            Alert err = new Alert(AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aggiungere il libro alla libreria");
            err.setContentText(ex.getMessage());
            err.showAndWait();
        }
    }

    private void submitIfAny(String name, int stars, String review) {
        if ((stars >= 1 && stars <= 5) || (review != null && !review.isBlank())) {
            String backendName = mapCategoryName(name);
            if (backendName == null || backendName.isBlank()) {
                return; // category disabled/not mapped -> skip
            }
            int eval = stars >= 1 ? stars : 1; // backend requires 1..5; default to 1 if only comment
            try {
                String trimmed = review != null ? review.trim() : null;
                ratingService.addRatingToBook(bookId, backendName, eval, trimmed);
                System.out.println("[Ratings] POST /ratings/book/" + bookId + " OK | name='" + backendName + "' eval=" + eval + " reviewLen=" + (trimmed != null ? trimmed.length() : 0));
            } catch (Exception ex) {
                System.err.println("[Ratings] POST /ratings/book/" + bookId + " FAILED | name='" + backendName + "' eval=" + eval + " -> " + ex.getMessage());
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Review Error");
                a.setHeaderText("Failed to submit '" + name + "'");
                a.setContentText(ex.getMessage());
                a.showAndWait();
            }
        }
    }

    private final Map<String, String> categoryMap = new HashMap<>();
    private void initCategoryMap() {
        Dotenv dotenv = null;
        try { dotenv = Dotenv.load(); } catch (Exception ignore) {}
        // Defaults aligned to DB English labels shown (case-sensitive)
        categoryMap.put("Stile", getenvOr(dotenv, "RATING_STILE", "Style"));
        categoryMap.put("Contenuto", getenvOr(dotenv, "RATING_CONTENUTO", "Content"));
        categoryMap.put("Gradevolezza", getenvOr(dotenv, "RATING_GRADEVOLEZZA", "Pleasantness"));
        categoryMap.put("Originalita", getenvOr(dotenv, "RATING_ORIGINALITA", "Originality"));
        categoryMap.put("Edizione", getenvOr(dotenv, "RATING_EDIZIONE", "Edition"));
    }

    private String getenvOr(Dotenv dotenv, String key, String def) {
        if (dotenv != null) {
            String v = dotenv.get(key);
            if (v != null && !v.isBlank()) return v;
        }
        String env = System.getenv(key);
        return (env != null && !env.isBlank()) ? env : def;
    }

    private String mapCategoryName(String uiName) {
        if (uiName == null) return null;
        return categoryMap.getOrDefault(uiName, uiName);
    }

    private String safeTxt(TextArea ta) {
        return ta != null ? ta.getText() : null;
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
