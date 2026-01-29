package com.lab_lib.frontend.Pages;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lab_lib.frontend.Exceptions.ApiException;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Models.PersonalLibrary;
import com.lab_lib.frontend.Utils.UserSession;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class MainBookStoreControllers {

    @FXML
    private TableView<Book> libreriaPersonaleTabella;
    @FXML
    private TableColumn<Book, String> PersonaleTitleColumn;
    @FXML
    private TableColumn<Book, String> PersonaleAuthorsColumn;
    
    @FXML
    private AnchorPane ArchorPaneLibreriaMain;

    @FXML
    private AnchorPane ArchorPaneLibriMain;

    @FXML
    private Button Btm_Gruppi;

    @FXML
    private ListView<PersonalLibrary> ListaGruppi;
    @FXML
    private TextField GroupNameField;
    @FXML
    private Button GroupCreateButton;

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
    @FXML
    private Button Btm_userInfo;
    @FXML
    private AnchorPane UserInfoPane;
    @FXML
    private Label UserNicknameLabel;
    @FXML
    private Label UserNameLabel;
    @FXML
    private Label SessionStartedLabel;
    @FXML
    private Label SessionExpiresLabel;
    @FXML private Label BookTitleLabel;
    @FXML private Label BookAuthorsLabel;
    @FXML private Label BookYearLabel;
    @FXML private Label BookCategoriesLabel;
    @FXML private Label BookPublisherLabel;
    @FXML private Label BookPriceLabel;
    @FXML private Label BookDescriptionLabel;
    @FXML private Button DataLibroBotoneReviewIt;
    @FXML private Button btnViewRecommendations;

    private final int MAX_STARS = 5;
    private boolean isMenuCollapsed = false;
    private ObservableList<Book> libriData = FXCollections.observableArrayList();
    private ObservableList<Book> personalLibraryData = FXCollections.observableArrayList();
    private final ObservableList<PersonalLibrary> groupsData = FXCollections.observableArrayList();
    private Long currentGroupId = null;
    private final int pageSize = 10;
    // Server-side prefix search now handles paging; no extra client fetches needed
    private boolean suppressSelectionDuringPaging = false;
    private String currentQuery = null;

    private final Injector injector;
    
    @FXML
    private Button LogOut;
    
    private final UserSession userSession;
    private final IBookService bookService;
    private final IPersonalLibraryService personalLibraryService;
    private final IRatingService ratingService;
    private boolean viewOnlyMode;
    private Book currentDetailBook;

    @Inject // Guice userà questo costruttore automaticamente!
    public MainBookStoreControllers(UserSession userSession, 
                                    IBookService bookService, 
                                    IPersonalLibraryService personalLibraryService, 
                                    IRatingService ratingService, 
                                    Injector injector) { // TOLTO viewOnlyMode
        this.userSession = userSession;
        this.bookService = bookService;
        this.personalLibraryService = personalLibraryService;
        this.ratingService = ratingService;
        this.injector = injector;
        this.viewOnlyMode = false; // Default: modalità utente normale
    }

    private void showGroupBooks(long libId, String groupName) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/GroupBooks.fxml"));
            javafx.scene.Parent root = loader.load();
            GroupBooksController ctrl = (GroupBooksController) loader.getController();
            ctrl.setContext(personalLibraryService, ratingService, libId, groupName);
            javafx.stage.Stage dlg = new javafx.stage.Stage();
            dlg.setTitle("Libri del gruppo: " + groupName);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            // Add CSS (styles and base app CSS) if available
            var styles = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (styles != null) scene.getStylesheets().add(styles.toExternalForm());
            var base = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (base != null) scene.getStylesheets().add(base.toExternalForm());
            var buttons = getClass().getResource("/com/lab_lib/frontend/Css/Buttons.css");
            if (buttons != null) scene.getStylesheets().add(buttons.toExternalForm());
            dlg.setScene(scene);
            // Make window larger and resizable
            dlg.setWidth(960);
            dlg.setHeight(640);
            dlg.setMinWidth(800);
            dlg.setMinHeight(520);
            dlg.setResizable(true);
            dlg.initOwner(VboxMenuOptions.getScene().getWindow());
            dlg.initModality(javafx.stage.Modality.NONE);
            dlg.show();
        } catch (java.io.IOException ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aprire la finestra dei libri del gruppo");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[Groups] Failed to open group books window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @FXML
    public void initialize() {

        // Imposta visibilità iniziali: mostra i libri, nasconde libreria e dettagli libro
        ArchorPaneLibriMain.setVisible(true);
        ArchorPaneLibreriaMain.setVisible(false);
        PanelDataLibroGeneralReviews.setVisible(false);
        // View-only mode: hide non-essential buttons and focus on catalog
        if (viewOnlyMode) {
            if (Btm_userInfo != null) Btm_userInfo.setVisible(false);
            if (Btm_Gruppi != null) Btm_Gruppi.setVisible(false);
            if (Btm_libreriaMain != null) Btm_libreriaMain.setVisible(false);
            // Keep LogOut visible to exit user-only mode
            if (LogOut != null) LogOut.setVisible(true);
            if (DataLibroBotoneReviewIt != null) DataLibroBotoneReviewIt.setVisible(false);
            // Show catalog by default
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(true);
            GruppiPanel.setVisible(false);
            tableLibriLibreria.getSelectionModel().clearSelection();
            loadUnifiedPage(0);
            // Ensure the visible 'Libri' button switches to catalog
            if (Btm_libriMain != null) {
                Btm_libriMain.setOnAction(e -> {
                    ArchorPaneLibriMain.setVisible(false);
                    ArchorPaneLibreriaMain.setVisible(true);
                    UserInfoPane.setVisible(false);
                    PanelDataLibroGeneralReviews.setVisible(false);
                    GruppiPanel.setVisible(false);
                    tableLibriLibreria.getSelectionModel().clearSelection();
                    loadUnifiedPage(0);
                });
            }
        }


        // Add context menu on rows for both left and right click
        setupRowContextMenu();

        // Inizializza colonne della tabella libreria (catalogo paginato)
        if (TitleColumn != null) {
            TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        }
        if (AuthorsColumn != null) {
            AuthorsColumn.setCellValueFactory(cell -> {
                var list = Optional.ofNullable(cell.getValue().getAuthors()).orElse(Collections.emptyList());
                return new SimpleStringProperty(String.join(", ", list));
            });
        }
        tableLibriLibreria.setItems(libriData);
                // Inizializza libreria personale (tabella separata, non influisce sul catalogo)
                if (libreriaPersonaleTabella != null) {
                    libreriaPersonaleTabella.setItems(personalLibraryData);
                    if (PersonaleTitleColumn != null) {
                        PersonaleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                    }
                    if (PersonaleAuthorsColumn != null) {
                        PersonaleAuthorsColumn.setCellValueFactory(cell -> {
                            var list = Optional.ofNullable(cell.getValue().getAuthors()).orElse(Collections.emptyList());
                            return new SimpleStringProperty(String.join(", ", list));
                        });
                    }
                    setupPersonalRowContextMenu();
                }
        // Evita barra horizontal y ajusta a 10 filas exactas sin scroll visible
        tableLibriLibreria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableLibriLibreria.setFixedCellSize(28);
        Platform.runLater(() -> {
            double header = 28;
            var headerNode = tableLibriLibreria.lookup(".column-header-background");
            if (headerNode instanceof Region) {
                header = ((Region) headerNode).getHeight();
            }
            double rowsHeight = tableLibriLibreria.getFixedCellSize() * pageSize;
            double pref = header + rowsHeight + 2;
            tableLibriLibreria.setPrefHeight(pref);
            tableLibriLibreria.setMinHeight(pref);
            tableLibriLibreria.setMaxHeight(pref);
        });
        if (BtnPrevPage != null) {
            BtnPrevPage.setOnAction(e -> {
                if (currentPage > 0) loadUnifiedPage(currentPage - 1);
            });
        }
        if (BtnNextPage != null) {
            BtnNextPage.setOnAction(e -> {
                if (currentPage + 1 < totalPages) loadUnifiedPage(currentPage + 1);
            });
        }

        // Bottoni per passare tra schermate libri e libreria
        Btm_Gruppi.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(true);
            loadGroups();
        });
        if (ListaGruppi != null) {
            ListaGruppi.setItems(groupsData);
            // Open group only on left-click
            ListaGruppi.setCellFactory(lv -> {
                javafx.scene.control.ListCell<PersonalLibrary> cell = new javafx.scene.control.ListCell<>() {
                    @Override
                    protected void updateItem(PersonalLibrary item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getName());
                    }
                };
                ContextMenu cm = new ContextMenu();
                // Evita fondo negro del popup: hace transparente la escena del ContextMenu
                cm.setOnShown(ev -> {
                    if (cm.getScene() != null) {
                        cm.getScene().setFill(Color.TRANSPARENT);
                        if (cm.getScene().getRoot() != null) {
                            cm.getScene().getRoot().setStyle("-fx-background-color: transparent;");
                        }
                    }
                });
                // Estilo directo (oscuro) sin cargar CSS global que define .root
                cm.setStyle("-fx-background-color: #2E2E2E; -fx-background-radius: 6; -fx-border-color: #333333; -fx-border-radius: 6; -fx-padding: 4 0;");
                MenuItem apri = new MenuItem("Apri");
                apri.setStyle("-fx-text-fill: #FFFFFF;");
                apri.setOnAction(ev -> {
                    PersonalLibrary lib = cell.getItem();
                    if (lib == null) return;
                    currentGroupId = lib.getId();
                    showGroupBooks(lib.getId(), lib.getName());
                });
                MenuItem del = new MenuItem("Elimina gruppo");
                del.setStyle("-fx-text-fill: #FFFFFF;");
                del.setOnAction(ev -> {
                    PersonalLibrary lib = cell.getItem();
                    if (lib == null) return;
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Conferma eliminazione");
                    confirm.setHeaderText("Eliminare il gruppo '" + lib.getName() + "'?");
                    confirm.setContentText("Questa azione non può essere annullata.");
                    var res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == javafx.scene.control.ButtonType.OK) {
                        try {
                            personalLibraryService.deletePersonalLibrary(lib.getId());
                            groupsData.remove(lib);
                            if (currentGroupId != null && currentGroupId.equals(lib.getId())) {
                                currentGroupId = null;
                                personalLibraryData.clear();
                            }
                            System.out.println("[Groups] Deleted group '" + lib.getName() + "' (id=" + lib.getId() + ")");
                        } catch (Exception ex) {
                            Alert err = new Alert(Alert.AlertType.ERROR);
                            err.setTitle("Errore");
                            err.setHeaderText("Eliminazione gruppo fallita");
                            err.setContentText(ex.getMessage());
                            err.showAndWait();
                            System.err.println("[Groups] Failed to delete group: " + ex.getMessage());
                        }
                    }
                });
                cm.getItems().addAll(apri, del);
                // Mostrar menú solo en click secundario (context-menu)
                cell.setOnContextMenuRequested(evt -> {
                    if (cell.getItem() != null) {
                        cm.show(cell, evt.getScreenX(), evt.getScreenY());
                    }
                });
                // Abrir grupo con click izquierdo, sin disparar el menú
                cell.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY && !cell.isEmpty()) {
                        PersonalLibrary lib = cell.getItem();
                        if (lib != null) {
                            currentGroupId = lib.getId();
                            showGroupBooks(lib.getId(), lib.getName());
                            e.consume();
                        }
                    }
                });
                return cell;
            });
        }
        if (GroupCreateButton != null) {
            GroupCreateButton.setOnAction(e -> createGroup());
        }

          Btm_libriMain.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(true);
            ArchorPaneLibreriaMain.setVisible(false);
            UserInfoPane.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            tableLibriLibreria.getSelectionModel().clearSelection();
        });


        Btm_libreriaMain.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(true);
            UserInfoPane.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            tableLibriLibreria.getSelectionModel().clearSelection();
            // Carica libri dalla libreria personale dell'utente
            refreshPersonalLibrary();
        });

        Btm_userInfo.setOnAction(e -> {
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(false);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            UserInfoPane.setVisible(true);
            populateUserInfo();
        });

        // Bottone logout: invalida la sessione e torna al pannello login/registrazione
        LogOut.setOnAction(e -> {
            try {
                System.out.println("[Session] Logout requested");
                userSession.logout();
            } catch (Exception ex) {
                System.err.println("[Session] Logout error: " + (ex.getMessage() != null ? ex.getMessage() : ""));
                ex.printStackTrace();
            }
            openLogRegMainPanelAndCloseCurrent();
        });

        // Toggle menu dimensioni con clic sull'icona hamburger
        hamburgerButton.setOnMouseClicked(e -> toggleMenuSize());

        // Imposta stelle valutazioni a zero all'avvio
        setStarRating(HBoxContenutoGeneralReviesStarts, 0);
        setStarRating(HBoxEdizioneGeneralReviesStarts, 0);
        setStarRating(HBoxGradevolezzaGeneralReviesStarts, 0);
        setStarRating(HBoxStileGeneralReviesStarts, 0);
        setStarRating(HBoxVotoFinaleGeneralReviesStarts, 0);
        setStarRating(HBoxOriginalitaGeneralReviesStarts, 0);

        // Click handlers to open category reviews (disabled in view-only mode)
        if (!viewOnlyMode) {
            HBoxStileGeneralReviesStarts.setOnMouseClicked(e -> openCategoryReviewsDialog("Style"));
            HBoxContenutoGeneralReviesStarts.setOnMouseClicked(e -> openCategoryReviewsDialog("Content"));
            HBoxOriginalitaGeneralReviesStarts.setOnMouseClicked(e -> openCategoryReviewsDialog("Originality"));
            HBoxEdizioneGeneralReviesStarts.setOnMouseClicked(e -> openCategoryReviewsDialog("Edition"));
            HBoxGradevolezzaGeneralReviesStarts.setOnMouseClicked(e -> openCategoryReviewsDialog("Pleasantness"));
        }

        applyViewMode();
    }

    public void setViewOnlyMode(boolean viewOnlyMode) {
        this.viewOnlyMode = viewOnlyMode;
        applyViewMode(); // Aggiorna la UI in base alla modalità
    }

    private void applyViewMode() {
        // Sposta qui la logica che nasconde i bottoni se viewOnlyMode è true
        if (viewOnlyMode) {
            if (Btm_userInfo != null) Btm_userInfo.setVisible(false);
            if (Btm_Gruppi != null) Btm_Gruppi.setVisible(false);
            if (Btm_libreriaMain != null) Btm_libreriaMain.setVisible(false);
            if (LogOut != null) LogOut.setVisible(true);
            if (DataLibroBotoneReviewIt != null) DataLibroBotoneReviewIt.setVisible(false);
            
            // Forza la vista sul catalogo
            if (ArchorPaneLibriMain != null) ArchorPaneLibriMain.setVisible(false);
            if (ArchorPaneLibreriaMain != null) ArchorPaneLibreriaMain.setVisible(true);
            if (GruppiPanel != null) GruppiPanel.setVisible(false);
            
            // Logica di caricamento pagina unificata se serve...
             loadUnifiedPage(0);
        }
    }

    private void setupRowContextMenu() {
        tableLibriLibreria.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();

            MenuItem infoItem = new MenuItem("Information");
            infoItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b != null) System.out.println("[ContextMenu] Information clicked for book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
                if (b != null) showBookInfo(b);
            });

            MenuItem addItem = new MenuItem("Aggiunge a libreria Per.");
            addItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b != null) System.out.println("[ContextMenu] Add to Library clicked for book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
                if (b != null) addBookToLibrary(b);
            });

            // Gruppi chained submenu: opens adjacent on hover after short delay
            Label gruppiLabel = new Label("Gruppi ▶");
            CustomMenuItem gruppiItem = new CustomMenuItem(gruppiLabel);
            gruppiItem.setHideOnClick(false);
            ContextMenu groupsMenu = new ContextMenu();
            groupsMenu.getStyleClass().add("dark-menu");
            // Keep open while main menu is open
            groupsMenu.setAutoHide(false);

            Runnable populateGroupsMenu = () -> {
                groupsMenu.getItems().clear();
                Book b = row.getItem();
                try {
                    var libs = personalLibraryService.getPersonalLibraries();
                    if (libs == null || libs.isEmpty()) {
                        MenuItem empty = new MenuItem("Nessun gruppo");
                        empty.setDisable(true);
                        groupsMenu.getItems().add(empty);
                    } else {
                        for (var lib : libs) {
                            MenuItem mi = new MenuItem(lib.getName());
                            mi.setOnAction(ae -> addBookToGroup(b, lib.getId(), lib.getName()));
                            groupsMenu.getItems().add(mi);
                        }
                    }
                } catch (Exception ex) {
                    MenuItem err = new MenuItem("Errore gruppi");
                    err.setDisable(true);
                    groupsMenu.getItems().add(err);
                    System.err.println("[Groups] Failed to load groups in menu: " + ex.getMessage());
                }
            };

            PauseTransition hoverDelay = new PauseTransition(Duration.millis(300));
            hoverDelay.setOnFinished(ev -> {
                populateGroupsMenu.run();
                // Show adjacent to main context menu
                double x = menu.getX() + menu.getWidth() - 2;
                double y = menu.getY();
                groupsMenu.show(row, x, y);
            });

            gruppiLabel.setOnMouseEntered(ev -> {
                hoverDelay.playFromStart();
            });
            gruppiLabel.setOnMouseExited(ev -> {
                // Stop showing delay, but keep submenu open to allow hovering into it
                hoverDelay.stop();
            });
            menu.setOnHiding(ev -> {
                groupsMenu.hide();
            });

            if (!viewOnlyMode) {
                MenuItem reviewItem = new MenuItem("Valutare");
                reviewItem.setOnAction(e -> {
                    Book b = row.getItem();
                    if (b != null) System.out.println("[ContextMenu] Valutare clicked for book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
                    if (b != null) openReviewWindowFor(b);
                });
                menu.getItems().add(reviewItem);
            }

            if (viewOnlyMode) {
                menu.getItems().addAll(infoItem);
            } else {
                // reviewItem is already added above when not view-only
                menu.getItems().addAll(infoItem, addItem, gruppiItem);
            }
            menu.getStyleClass().add("dark-menu");
            row.setContextMenu(menu); // right-click

            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) return;
                tableLibriLibreria.getSelectionModel().select(row.getIndex());
                Book b = row.getItem();
                if (e.getClickCount() == 2 && e.isPrimaryButtonDown()) {
                    // Double-click: add directly to personal library
                    if (b != null) addBookToLibrary(b);
                } else if (e.isPrimaryButtonDown()) {
                    // Single left click: show context menu near cursor
                    menu.show(row, e.getScreenX(), e.getScreenY());
                }
            });
            return row;
        });
    }

    private void addBookToGroup(Book b, long libId, String groupName) {
        if (b == null) return;
        try {
            personalLibraryService.addBookToLibrary(libId, b.getId());
            Alert ok = new Alert(AlertType.INFORMATION);
            ok.setTitle("Added");
            ok.setHeaderText("Book added to group");
            ok.setContentText("Added to '" + groupName + "'.");
            styleAlert(ok);
            ok.showAndWait();
            System.out.println("[PersonalLibrary] Added book id=" + b.getId() + " to group '" + groupName + "' (libId=" + libId + ")");
            if (currentGroupId != null && currentGroupId.equals(libId)) {
                addToPersonalLibraryUI(b);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "";
            if (ex instanceof ApiException && msg.contains("HTTP 409")) {
                Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Già presente");
                info.setHeaderText("Il libro è già nel gruppo");
                info.setContentText(nvl(b.getTitle()));
                styleAlert(info);
                info.showAndWait();
                System.err.println("[Groups][409] Book already present in group. libId=" + libId + ", bookId=" + (b != null ? b.getId() : null) + ", message=" + msg);
            } else {
                Alert err = new Alert(AlertType.ERROR);
                err.setTitle("Error");
                err.setHeaderText("Failed to add book to group");
                err.setContentText(ex.getMessage());
                styleAlert(err);
                err.showAndWait();
                System.err.println("[Groups][Error] Failed to add book to group libId=" + libId + ": " + msg);
                ex.printStackTrace();
            }
        }
    }

    private void showBookInfo(Book b) {
        openBookDetails(b);
    }

    private void openBookDetails(Book b) {
        ArchorPaneLibriMain.setVisible(false);
        ArchorPaneLibreriaMain.setVisible(false);
        GruppiPanel.setVisible(false);
        PanelDataLibroGeneralReviews.setVisible(true);
        currentDetailBook = b;
        if (BookTitleLabel != null) BookTitleLabel.setText(nvl(b.getTitle()));
        if (BookAuthorsLabel != null) {
            var authors = Optional.ofNullable(b.getAuthors()).orElse(Collections.emptyList());
            BookAuthorsLabel.setText(authors.isEmpty() ? "-" : String.join(", ", authors));
        }
        if (BookYearLabel != null) {
            String year = b.getPublishYear() != null ? b.getPublishYear().toString() : "-";
            String month = b.getPublishMonth() != null ? String.format("%02d", b.getPublishMonth()) : null;
            BookYearLabel.setText(month != null ? month + "/" + year : year);
        }
        if (BookCategoriesLabel != null) {
            var cats = Optional.ofNullable(b.getCategories()).orElse(Collections.emptyList());
            BookCategoriesLabel.setText(cats.isEmpty() ? "-" : String.join(", ", cats));
        }
        if (BookPublisherLabel != null) BookPublisherLabel.setText(nvl(b.getPublisher()));
        if (BookPriceLabel != null) BookPriceLabel.setText(b.getPrice() != null ? b.getPrice().toPlainString() : "-");
        if (BookDescriptionLabel != null) BookDescriptionLabel.setText(nvl(b.getDescription()));

        if (btnViewRecommendations != null) {
            btnViewRecommendations.setVisible(true);
        }

        // Update average stars per category and overall
        updateAverageStarsForBook(b.getId());
    }

    private String nvl(String s) { return (s == null || s.isBlank()) ? "-" : s; }

    private void addBookToLibrary(Book b) {
        try {
            // Optimistic UI update: stamp in personal table immediately
            addToPersonalLibraryUI(b);
            System.out.println("[PersonalLibrary] UI stamped book id=" + (b != null ? b.getId() : null) + ", title='" + (b != null ? nvl(b.getTitle()) : "-") + "'");

            var libs = personalLibraryService.getPersonalLibraries();
            if (libs == null || libs.isEmpty()) {
                // Try default library fallback
                PersonalLibrary def = personalLibraryService.getDefaultPersonalLibrary();
                if (def == null) {
                    // Attempt to create default library on first add
                    def = personalLibraryService.createDefaultPersonalLibrary();
                }
                if (def == null) {
                    // Backend default not available; keep UI update only
                    System.out.println("[PersonalLibrary] No backend library found/created; kept UI-only add.");
                    return;
                }
                libs = new java.util.ArrayList<>();
                libs.add(def);
            }
            long libId = libs.get(0).getId();
            personalLibraryService.addBookToLibrary(libId, b.getId());
            Alert ok = new Alert(AlertType.INFORMATION);
            ok.setTitle("Added");
            ok.setHeaderText("Book added to library");
            ok.setContentText("Added to '" + libs.get(0).getName() + "'.");
            styleAlert(ok);
            ok.showAndWait();
        } catch (Exception ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "";
            if (ex instanceof ApiException && msg.contains("HTTP 409")) {
                Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Già presente");
                info.setHeaderText("Il libro è già nella libreria personale");
                info.setContentText(nvl(b != null ? b.getTitle() : ""));
                styleAlert(info);
                info.showAndWait();
                System.err.println("[PersonalLibrary][409] Book already present in default library. bookId=" + (b != null ? b.getId() : null) + ", message=" + msg);
            } else {
                Alert err = new Alert(AlertType.ERROR);
                err.setTitle("Error");
                err.setHeaderText("Failed to add book to library");
                err.setContentText(ex.getMessage());
                styleAlert(err);
                err.showAndWait();
                System.err.println("[PersonalLibrary][Error] Failed to add book to library: " + msg);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleViewRecommendations(ActionEvent event) {
        if (currentDetailBook == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/RecommendedBooks.fxml"));
            
            // USIAMO GUICE per iniettare IRatingService nel nuovo controller
            if (injector != null) {
                loader.setControllerFactory(injector::getInstance);
            }

            Parent root = loader.load();
            
            // Passiamo i dati al controller
            RecommendedBooksController ctrl = loader.getController();
            ctrl.setContext(currentDetailBook.getId(), nvl(currentDetailBook.getTitle()));

            Stage stage = new Stage();
            stage.setTitle("Libri Consigliati");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            
            // Modale: blocca la finestra sotto finché non chiudi questa
            stage.initModality(Modality.WINDOW_MODAL); 
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert err = new Alert(AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Impossibile aprire i consigli");
            err.setContentText(e.getMessage());
            err.showAndWait();
        }
    }

    private void addToPersonalLibraryUI(Book b) {
        if (b == null) return;
        boolean exists = personalLibraryData.stream().anyMatch(x -> x.getId() != null && x.getId().equals(b.getId()));
        if (!exists) {
            personalLibraryData.add(b);
            System.out.println("[PersonalLibrary] Table updated with book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
        }
    }

    private void refreshPersonalLibrary() {
        try {
            var libs = personalLibraryService.getPersonalLibraries();
            PersonalLibrary def = (libs != null && !libs.isEmpty()) ? libs.get(0) : personalLibraryService.getDefaultPersonalLibrary();
            if (def == null) {
                def = personalLibraryService.createDefaultPersonalLibrary();
            }
            if (def == null) {
                System.err.println("[PersonalLibrary] No default library found or created; showing empty.");
                Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Nessuna libreria");
                info.setHeaderText("Non hai ancora una libreria personale");
                info.setContentText("Creane una o aggiungi un libro per crearla automaticamente.");
                styleAlert(info);
                info.showAndWait();
                personalLibraryData.clear();
                return;
            }
            var page = personalLibraryService.getBooksInLibrary(def.getId(), 0, 50);
            var content = Optional.ofNullable(page).map(PaginatedResponse::getContent).orElse(Collections.emptyList());
            personalLibraryData.setAll(content);
            System.out.println("[PersonalLibrary] Loaded " + personalLibraryData.size() + " books from '" + def.getName() + "' (id=" + def.getId() + ")");
        } catch (Exception ex) {
            Alert err = new Alert(AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Caricamento libreria personale fallito");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[PersonalLibrary] Failed to load personal library: " + (ex.getMessage() != null ? ex.getMessage() : ""));
            ex.printStackTrace();
        }
    }

    private void setupPersonalRowContextMenu() {
        libreriaPersonaleTabella.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();

            MenuItem infoItem = new MenuItem("Information");
            infoItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b != null) System.out.println("[PersonalContext] Information clicked for book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
                if (b != null) showBookInfo(b);
            });

            if (!viewOnlyMode) {
                MenuItem reviewItem = new MenuItem("Valutare");
                reviewItem.setOnAction(e -> {
                    Book b = row.getItem();
                    if (b != null) System.out.println("[PersonalContext] Valutare clicked for book id=" + b.getId() + ", title='" + nvl(b.getTitle()) + "'");
                    if (b != null) openReviewWindowFor(b);
                });
                menu.getItems().add(reviewItem);
            }

            MenuItem removeItem = new MenuItem("Elimina dal gruppo");
            removeItem.setOnAction(e -> {
                Book b = row.getItem();
                if (b == null) return;
                if (currentGroupId == null) {
                    Alert warn = new Alert(Alert.AlertType.WARNING);
                    warn.setTitle("Nessun gruppo selezionato");
                    warn.setHeaderText("Seleziona un gruppo per rimuovere il libro");
                    warn.setContentText("Apri Gruppi e clicca sul gruppo desiderato.");
                    styleAlert(warn);
                    warn.showAndWait();
                    return;
                }
                try {
                    personalLibraryService.removeBookFromLibrary(currentGroupId, b.getId());
                    personalLibraryData.removeIf(x -> x.getId().equals(b.getId()));
                    System.out.println("[Groups] Removed book id=" + b.getId() + " from group id=" + currentGroupId);
                } catch (Exception ex) {
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Errore");
                    err.setHeaderText("Impossibile rimuovere il libro dal gruppo");
                    err.setContentText(ex.getMessage());
                    styleAlert(err);
                    err.showAndWait();
                    System.err.println("[Groups] Failed to remove book: " + ex.getMessage());
                }
            });

            if (viewOnlyMode) {
                menu.getItems().addAll(infoItem);
            } else {
                menu.getItems().addAll(infoItem, removeItem);
            }
            menu.getStyleClass().add("dark-menu");
            row.setContextMenu(menu);
            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) return;
                libreriaPersonaleTabella.getSelectionModel().select(row.getIndex());
                Book b = row.getItem();
                if (e.getClickCount() == 2 && e.isPrimaryButtonDown()) {
                    if (b != null) showBookInfo(b);
                } else if (e.isPrimaryButtonDown()) {
                    menu.show(row, e.getScreenX(), e.getScreenY());
                }
            });
            return row;
        });
    }

    private void loadGroups() {
        try {
            var libs = personalLibraryService.getPersonalLibraries();
            groupsData.setAll(Optional.ofNullable(libs).orElse(java.util.Collections.emptyList()));
            System.out.println("[Groups] Loaded " + groupsData.size() + " groups");
        } catch (Exception ex) {
            System.err.println("[Groups] Failed to load groups: " + ex.getMessage());
        }
    }

    private void createGroup() {
        try {
            String name = GroupNameField != null ? GroupNameField.getText() : null;
            if (name == null || name.trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Nome mancante");
                a.setHeaderText("Inserisci il nome del gruppo");
                a.setContentText("Il nome non può essere vuoto.");
                styleAlert(a);
                a.showAndWait();
                return;
            }
            var created = personalLibraryService.createPersonalLibrary(name.trim());
            if (created == null) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setTitle("Errore");
                err.setHeaderText("Creazione gruppo fallita");
                err.setContentText("Impossibile creare il gruppo.");
                styleAlert(err);
                err.showAndWait();
                System.err.println("[Groups] Error creating group '" + name + "' - see previous logs for details.");
                return;
            }
            groupsData.add(created);
            if (GroupNameField != null) GroupNameField.clear();
            System.out.println("[Groups] Created group '" + created.getName() + "'");
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Creazione gruppo fallita");
            err.setContentText(ex.getMessage());
            styleAlert(err);
            err.showAndWait();
            System.err.println("[Groups] Exception while creating group: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private TableColumn<Book, String> TitleColumn;
    @FXML
    private TableColumn<Book, String> AuthorsColumn;
    @FXML
    private Button BtnPrevPage;
    @FXML
    private Button BtnNextPage;
    @FXML
    private TextField SearchField;
    @FXML
    private TextField SearchFieldLibriMain;

    @FXML
    private ChoiceBox<String> FilterChoiceLibriMain;

    @FXML
    private ChoiceBox<String> FilterChoiceLibreria;

    private int currentPage = 0;
    private int totalPages = 1;

    @FXML
    private void handleSearchAction() {
        applySearchAndLoad();
    }

    @FXML
    private void handleSearchKeyReleased(KeyEvent event) {
        // Keep search behavior consistent: trigger via button click only
        // No action on typing; use Search button
    }

    private void applySearchAndLoad() {
        String q1 = (SearchField != null) ? Optional.ofNullable(SearchField.getText()).orElse("").trim() : "";
        String q2 = (SearchFieldLibriMain != null) ? Optional.ofNullable(SearchFieldLibriMain.getText()).orElse("").trim() : "";
        String q = !q1.isEmpty() ? q1 : (!q2.isEmpty() ? q2 : "");
        currentQuery = q.isEmpty() ? null : q;
        loadUnifiedPage(0);
    }

    private void loadUnifiedPage(int page) {
        if (currentQuery == null) {
            loadPage(page);
        } else {
            loadSearchPage(page, currentQuery);
        }
    }

        private enum SearchMode { TITLE, AUTHOR, COMBINED }
        private SearchMode currentMode = SearchMode.TITLE;
    private void loadPage(int page) {
        try {
            suppressSelectionDuringPaging = true;
            var resp = bookService.getBooks(page, pageSize);
            libriData.setAll(resp.getContent());
            // Assicura che dopo il cambio pagina resti visibile la tabella libreria
            tableLibriLibreria.getSelectionModel().clearSelection();
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(true);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            currentPage = resp.getNumber();

            // Setup filter choice boxes
            setupFilterChoices();
            totalPages = resp.getTotalPages();
            if (BtnPrevPage != null) BtnPrevPage.setDisable(currentPage <= 0);
            if (BtnNextPage != null) BtnNextPage.setDisable(currentPage + 1 >= totalPages || resp.getContent().isEmpty());
            System.out.println("Libri caricati pagina " + currentPage + "/" + totalPages + ": " + resp.getNumberOfElements());
        } finally {
            suppressSelectionDuringPaging = false;
        }
    }

    private void setupFilterChoices() {
        var items = javafx.collections.FXCollections.observableArrayList("Title", "Author", "Title + Author");
        if (FilterChoiceLibriMain != null) {
            FilterChoiceLibriMain.setItems(items);
            FilterChoiceLibriMain.setValue("Title");
            FilterChoiceLibriMain.setOnAction(e -> updateModeFromChoice(FilterChoiceLibriMain.getValue()));
        }
        if (FilterChoiceLibreria != null) {
            FilterChoiceLibreria.setItems(items);
            FilterChoiceLibreria.setValue("Title");
            FilterChoiceLibreria.setOnAction(e -> updateModeFromChoice(FilterChoiceLibreria.getValue()));
        }
    }

    private void updateModeFromChoice(String val) {
        if (val == null) return;
        if ("Author".equalsIgnoreCase(val)) {
            currentMode = SearchMode.AUTHOR;
        } else if ("Title + Author".equalsIgnoreCase(val)) {
            currentMode = SearchMode.COMBINED;
        } else {
            currentMode = SearchMode.TITLE;
        }
    }

    private void loadSearchPage(int page, String query) {
        try {
            suppressSelectionDuringPaging = true;
            var resp = switch (currentMode) {
                case TITLE -> bookService.searchBooksByTitle(query, page, pageSize);
                case AUTHOR -> bookService.searchBooksByAuthor(query, page, pageSize);
                case COMBINED -> bookService.searchBooksByTitleOrAuthor(query, page, pageSize);
            };
            var pageSlice = Optional.ofNullable(resp.getContent()).orElse(Collections.emptyList());
            libriData.setAll(pageSlice);
            tableLibriLibreria.getSelectionModel().clearSelection();
            ArchorPaneLibriMain.setVisible(false);
            ArchorPaneLibreriaMain.setVisible(true);
            PanelDataLibroGeneralReviews.setVisible(false);
            GruppiPanel.setVisible(false);
            currentPage = resp.getNumber();
            totalPages = resp.getTotalPages();
            if (BtnPrevPage != null) BtnPrevPage.setDisable(currentPage <= 0);
            if (BtnNextPage != null) BtnNextPage.setDisable(currentPage + 1 >= totalPages || pageSlice.isEmpty());
            System.out.println("Risultati ricerca ('" + query + "') pagina " + currentPage + "/" + totalPages + ": " + pageSlice.size());
        } catch (Exception ex) {
            System.err.println("Errore ricerca libri (pagina " + page + "): " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            suppressSelectionDuringPaging = false;
        }
    }

    private void populateUserInfo() {
        String nickname = userSession.getNickname();
        String name = userSession.getDisplayName();
        java.time.Instant started = userSession.getStartedAt();
        java.time.Instant expires = userSession.getExpiresAt();

        UserNicknameLabel.setText(nickname != null ? nickname : "-");
        UserNameLabel.setText(name != null && !name.isEmpty() ? name : "-");
        SessionStartedLabel.setText(started != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.ofInstant(started, java.time.ZoneId.systemDefault())) : "-");
        SessionExpiresLabel.setText(expires != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.ofInstant(expires, java.time.ZoneId.systemDefault())) : "-");
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

    private void updateAverageStarsForBook(Long bookId) {
        try {
            var ratings = ratingService.getRatingsByBookId(bookId);
            if (ratings == null || ratings.isEmpty()) {
                // No ratings yet; keep zeros
                setStarRating(HBoxStileGeneralReviesStarts, 0);
                setStarRating(HBoxContenutoGeneralReviesStarts, 0);
                setStarRating(HBoxOriginalitaGeneralReviesStarts, 0);
                setStarRating(HBoxEdizioneGeneralReviesStarts, 0);
                setStarRating(HBoxGradevolezzaGeneralReviesStarts, 0);
                setStarRating(HBoxVotoFinaleGeneralReviesStarts, 0);
                return;
            }
            Map<String, List<Integer>> byName = new HashMap<>();
            for (var r : ratings) {
                if (r.getRatingName() == null || r.getEvaluation() == null) continue;
                byName.computeIfAbsent(r.getRatingName(), k -> new java.util.ArrayList<>()).add(r.getEvaluation());
            }
            int style = avgToStars(byName.get("Style"));
            int content = avgToStars(byName.get("Content"));
            int originality = avgToStars(byName.get("Originality"));
            int edition = avgToStars(byName.get("Edition"));
            int pleasantness = avgToStars(byName.get("Pleasantness"));

            setStarRating(HBoxStileGeneralReviesStarts, style);
            setStarRating(HBoxContenutoGeneralReviesStarts, content);
            setStarRating(HBoxOriginalitaGeneralReviesStarts, originality);
            setStarRating(HBoxEdizioneGeneralReviesStarts, edition);
            setStarRating(HBoxGradevolezzaGeneralReviesStarts, pleasantness);

            double totalAvg = avg(
                new int[] { style, content, originality, edition, pleasantness },
                new boolean[] { byName.containsKey("Style"), byName.containsKey("Content"), byName.containsKey("Originality"), byName.containsKey("Edition"), byName.containsKey("Pleasantness") }
            );
            setStarRating(HBoxVotoFinaleGeneralReviesStarts, (int) Math.round(totalAvg));
        } catch (Exception ex) {
            System.err.println("Failed to load ratings for book " + bookId + ": " + ex.getMessage());
        }
    }

    private int avgToStars(List<Integer> values) {
        if (values == null || values.isEmpty()) return 0;
        double sum = 0.0;
        for (int v : values) sum += v;
        double avg = sum / values.size();
        return (int) Math.round(avg);
    }

    private double avg(int[] vals, boolean[] present) {
        double sum = 0.0;
        int count = 0;
        for (int i = 0; i < vals.length; i++) {
            if (present[i]) { sum += vals[i]; count++; }
        }
        return count == 0 ? 0.0 : (sum / count);
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
            // Inject required services into LogRegUserMainPanelControllers
            loader.setControllerFactory(type -> {
                if (type == LogRegUserMainPanelControllers.class) {
                    com.lab_lib.frontend.Utils.HttpUtil http = new com.lab_lib.frontend.Utils.HttpUtil(userSession);
                    com.lab_lib.frontend.Services.AuthService auth = new com.lab_lib.frontend.Services.AuthService(http);
                    return new LogRegUserMainPanelControllers(auth, userSession);
                }
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            var styles = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (styles != null) scene.getStylesheets().add(styles.toExternalForm());
            var base = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (base != null) scene.getStylesheets().add(base.toExternalForm());
            var buttons = getClass().getResource("/com/lab_lib/frontend/Css/Buttons.css");
            if (buttons != null) scene.getStylesheets().add(buttons.toExternalForm());
            newStage.setScene(scene);
            newStage.setTitle("Login/Register Panel");
            newStage.setResizable(false);
            newStage.show();

            // Chiude la finestra corrente
            Stage currentStage = (Stage) Btm_libriMain.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Impossibile aprire LogRegMainPanel.fxml");
        }
    }

    private void openCategoryReviewsDialog(String ratingName) {
        try {
            if (currentDetailBook == null) return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/CategoryReviews.fxml"));
            Parent root = loader.load();
            var ctrl = (CategoryReviewsController) loader.getController();
            ctrl.setContext(ratingService, currentDetailBook.getId(), ratingName, nvl(currentDetailBook.getTitle()));

            Stage dlg = new Stage();
            dlg.setTitle("Reviews: " + ratingName);
            dlg.setScene(new Scene(root));
            dlg.setResizable(false);
            dlg.initModality(Modality.WINDOW_MODAL);
            dlg.initOwner(VboxMenuOptions.getScene().getWindow());
            dlg.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // Rimosso metodo placeholder addLibro: ora la tabella mostra dati reali paginati dal backend (Models.Book)


    private void styleAlert(Alert alert) {
        try {
            var css1 = getClass().getResource("/com/lab_lib/frontend/Css/styles.css");
            if (css1 != null) alert.getDialogPane().getStylesheets().add(css1.toExternalForm());
            var css2 = getClass().getResource("/com/lab_lib/frontend/Css/CSS.css");
            if (css2 != null) alert.getDialogPane().getStylesheets().add(css2.toExternalForm());
        } catch (Exception ignore) {}
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
            var ctrl = (ValutaControllers) loader.getController();
            if (currentDetailBook != null) {
                ctrl.setContext(currentDetailBook.getId(), nvl(currentDetailBook.getTitle()), id -> addToPersonalLibraryUI(currentDetailBook));
            }

            Stage reviewStage = new Stage();
            reviewStage.setTitle("Scheda di Valutazione del Libro");
            reviewStage.setScene(new Scene(root));
            reviewStage.setResizable(false);
            reviewStage.initModality(Modality.WINDOW_MODAL); // blocca l'interazione con la finestra principale
            reviewStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // imposta la finestra principale come owner
            reviewStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openReviewWindowFor(Book b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/SchedaDiValutazioneLibro.fxml"));
            
            // USA GUICE PER CREARE IL CONTROLLER
            if (injector != null) {
                loader.setControllerFactory(injector::getInstance);
            }
            
            Parent root = loader.load();
            var ctrl = (ValutaControllers) loader.getController();
            
            // CORREZIONE setContext: NON PASSARE I SERVIZI (Rating, LibraryService)
            // Passa solo ID, Titolo e Callback.
            ctrl.setContext(b.getId(), nvl(b.getTitle()), id -> addToPersonalLibraryUI(b));

            // CORREZIONE ERRORE "cannot find symbol variable stage"
            // Devi dichiarare lo stage qui dentro
            Stage stage = new Stage(); // <--- Questa riga mancava o era spostata
            stage.setTitle("Scheda di Valutazione del Libro");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(VboxMenuOptions.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
