package com.lab_lib.frontend.Pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IAuthService;
import com.lab_lib.frontend.Models.AuthResponse;
import com.lab_lib.frontend.Models.LoginRequest;
import com.lab_lib.frontend.Models.RegisterRequest;
import com.lab_lib.frontend.Utils.UserSession;
import com.lab_lib.frontend.Pages.MainBookStoreControllers;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Services.BookService;
import com.lab_lib.frontend.Services.PersonalLibraryService;
import com.lab_lib.frontend.Services.RatingService;
import com.lab_lib.frontend.Utils.HttpUtil;

/**
 * Controller per login e registrazione utente.
 * Gestisce la creazione/riconoscimento dell'utente e la sessione di 30 minuti.
 */
public class LogRegUserMainPanelControllers {

    @FXML
    private VBox ArchorPaneLogRegUser;

    @FXML
    private Button Btm_LoginContinue1;  // open /LabBFrontEnd/src/main/resources/FXML/MainBookStore.fxml

    @FXML
    private Button Btm_Register_RegisterPane; // open /LabBFrontEnd/src/main/resources/FXML/MainBookStore.fxml

    @FXML
    private Button LoginPrincipalPageBtm; // muestra LoginVbox

    @FXML
    private VBox LoginVBox;

    @FXML
    private TextField Login_Gmail_TextArea1;

    @FXML
    private TextField Login_Gmail_TextArea11;

    @FXML
    private ImageView LogoMain;

    @FXML
    private Button RegisterPrincipalPageBtm; // open RegisterVbox

    @FXML
    private VBox RegisterVBox;

    @FXML
    private SplitPane SpliPaneMainLogReg;

    @FXML
    private StackPane StackPanelLogReg;

    @FXML
    private Button UserModePrincipalPageBtm;
    
    @FXML
    private Label BackLoginRegister;

    @FXML
    private Label BackLoginRegister2;
    // Label per mostrare errori UI
    @FXML private Label Reg_Error;
    @FXML private Label Login_Error;


    // Campi di registrazione (fx:id aggiunti in FXML)
    @FXML private TextField Reg_Name;
    @FXML private TextField Reg_Surname;
    @FXML private TextField Reg_Nickname;
    @FXML private TextField Reg_Email;
    @FXML private TextField Reg_Cf;
    @FXML private TextField Reg_Password;
    @FXML private TextField Reg_ConfirmPassword;

    private final IAuthService authService;
    private final UserSession userSession;

    @Inject
    public LogRegUserMainPanelControllers(IAuthService authService, UserSession userSession) {
        // Iniezione servizi (Google Guice)
        this.authService = authService;
        this.userSession = userSession;
    }

    @FXML
    public void initialize() {

        // Click sulle label per tornare al pannello principale
        BackLoginRegister.setOnMouseClicked(e -> showMainPane());
        BackLoginRegister2.setOnMouseClicked(e -> showMainPane());

        // All'avvio mostra solo il pannello principale
        ArchorPaneLogRegUser.setVisible(true);
        LoginVBox.setVisible(false);
        RegisterVBox.setVisible(false);

        // Mostra la schermata login e nasconde le altre
        LoginPrincipalPageBtm.setOnAction(e -> {
            ArchorPaneLogRegUser.setVisible(false);
            LoginVBox.setVisible(true);
            RegisterVBox.setVisible(false);
        });

        // Mostra la schermata registrazione e nasconde le altre
        RegisterPrincipalPageBtm.setOnAction(e -> {
            ArchorPaneLogRegUser.setVisible(false);
            LoginVBox.setVisible(false);
            RegisterVBox.setVisible(true);
        });

        // Login: raccoglie nickname e password e avvia sessione
        Btm_LoginContinue1.setOnAction(e -> {
            // ATTENZIONE: il campo etichettato "Gmail" in FXML rappresenta il nickname richiesto dal backend
            String nickname = Login_Gmail_TextArea1.getText() != null ? Login_Gmail_TextArea1.getText().trim() : "";
            String password = Login_Gmail_TextArea11.getText() != null ? Login_Gmail_TextArea11.getText().trim() : "";
            Login_Error.setText("");
            if (nickname.isEmpty() || password.isEmpty()) {
                Login_Error.setText("Inserisci nickname e password.");
                return;
            }
            try {
                AuthResponse resp = authService.login(new LoginRequest(nickname, password));
                if (resp != null && resp.getToken() != null) {
                    userSession.startSession(resp.getToken());
                    userSession.setNickname(nickname);
                    viewOnlyMode = false;
                    ensureDefaultPersonalLibrary();
                    openMainBookStore();
                } else {
                    Login_Error.setText("Login fallito: token mancante");
                }
            } catch (Exception ex) {
                String msg = ex.getMessage() != null ? ex.getMessage() : "";
                if (msg.contains("Invalid nickname or password")) {
                    Login_Error.setText("Nickname o password non validi.");
                } else {
                    Login_Error.setText("Errore login: " + msg);
                }
                // Log anche in console per diagnosi
                System.err.println("Errore login (console): " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Registrazione: valida i campi, invia al backend e avvia sessione
        Btm_Register_RegisterPane.setOnAction(e -> {
            String pass = Reg_Password.getText();
            String conf = Reg_ConfirmPassword.getText();
            Reg_Error.setText("");
            if (pass == null || !pass.equals(conf)) {
                Reg_Error.setText("Password e conferma non coincidono");
                return;
            }
            RegisterRequest req = new RegisterRequest();
            req.setName(Reg_Name.getText());
            req.setSurname(Reg_Surname.getText());
            req.setNickname(Reg_Nickname.getText());
            req.setEmail(Reg_Email.getText());
            req.setCf(Reg_Cf.getText());
            req.setPassword(pass);
            try {
                AuthResponse resp = authService.register(req);
                if (resp != null && resp.getToken() != null) {
                    userSession.startSession(resp.getToken());
                    userSession.setNickname(req.getNickname());
                    String fullName = (req.getName() != null ? req.getName() : "") +
                            (req.getSurname() != null ? (" " + req.getSurname()) : "");
                    userSession.setDisplayName(fullName.trim());
                    viewOnlyMode = false;
                    ensureDefaultPersonalLibrary();
                    openMainBookStore();
                } else {
                    Reg_Error.setText("Registrazione fallita: token mancante");
                }
            } catch (Exception ex) {
                // Mostra un messaggio amichevole per casi comuni del backend
                String msg = ex.getMessage();
                if (msg != null) {
                    if (msg.contains("Email already in use")) {
                        Reg_Error.setText("Email già in uso. Prova il login oppure usa un'altra email.");
                    } else if (msg.contains("Nickname already in use")) {
                        Reg_Error.setText("Nickname già in uso. Prova il login oppure usa un altro nickname.");
                    } else if (msg.contains("Password must be at least 8 characters")) {
                        Reg_Error.setText("La password deve avere almeno 8 caratteri.");
                    } else {
                        Reg_Error.setText("Errore registrazione: " + msg);
                    }
                } else {
                    Reg_Error.setText("Errore registrazione sconosciuto.");
                }
                // Log degli errori di registrazione anche in console
                System.err.println("Registrazione fallita (console): " + (msg != null ? msg : "nessun messaggio"));
                ex.printStackTrace();
            }
        });

        // Azione placeholder per UserModePrincipalPageBtm
        UserModePrincipalPageBtm.setOnAction(e -> {
            // Abre el panel principal en modo sólo lectura (ver libros)
            viewOnlyMode = true;
            openMainBookStore();
        });
    }

    /**
     * Ensure the user's default personal library exists after login/registration.
     * If none is found, attempt to create it via the backend.
     */
    private void ensureDefaultPersonalLibrary() {
        try {
            HttpUtil http = new HttpUtil(userSession);
            IPersonalLibraryService pls = new PersonalLibraryService(http);
            var libs = pls.getPersonalLibraries();
            if (libs == null || libs.isEmpty()) {
                var created = pls.createDefaultPersonalLibrary();
                if (created == null) {
                    System.err.println("[PersonalLibrary] No libraries found and default creation failed.");
                } else {
                    System.out.println("[PersonalLibrary] Default library ensured: " + created.getName());
                }
            }
        } catch (Exception ex) {
            System.err.println("[PersonalLibrary] ensureDefaultPersonalLibrary error: " + ex.getMessage());
        }
    }

    /**
     * Metodo privato per aprire la finestra principale MainBookStore.fxml.
     * Chiude la finestra corrente dopo l'apertura.
     */
    private boolean viewOnlyMode = false;

    private void openMainBookStore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/MainBookStore.fxml"));
            // Usa una factory per iniettare l'UserSession nel controller della pagina successiva
            loader.setControllerFactory(type -> {
                if (type == MainBookStoreControllers.class) {
                    HttpUtil http = new HttpUtil(userSession);
                    IBookService bs = new BookService(http);
                    IPersonalLibraryService pls = new PersonalLibraryService(http);
                    IRatingService rs = new RatingService(http);
                    return new MainBookStoreControllers(userSession, bs, pls, rs, viewOnlyMode);
                }
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Main Book Store");
            newStage.setResizable(false);
            newStage.show();

            // Chiude la finestra corrente
            Stage currentStage = (Stage) ArchorPaneLogRegUser.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Impossibile aprire MainBookStore.fxml");
        }
    }

    /**
     * Mostra il pannello principale e nasconde login e registrazione.
     */
    private void showMainPane() {
        ArchorPaneLogRegUser.setVisible(true);
        LoginVBox.setVisible(false);
        RegisterVBox.setVisible(false);
    }

}