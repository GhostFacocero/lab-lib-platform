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

        // Apri la finestra MainBookStore.fxml dopo login
        Btm_LoginContinue1.setOnAction(e -> openMainBookStore());

        // Apri la finestra MainBookStore.fxml dopo registrazione
        Btm_Register_RegisterPane.setOnAction(e -> openMainBookStore());

        // Azione placeholder per UserModePrincipalPageBtm
        UserModePrincipalPageBtm.setOnAction(e -> {
            System.out.println("UserModePrincipalPageBtm clicked");
        });
    }

    /**
     * Metodo privato per aprire la finestra principale MainBookStore.fxml.
     * Chiude la finestra corrente dopo l'apertura.
     */
    private void openMainBookStore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_lib/frontend/Pages/MainBookStore.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Main Book Store");
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