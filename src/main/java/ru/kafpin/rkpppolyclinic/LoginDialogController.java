package ru.kafpin.rkpppolyclinic;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginDialogController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private Stage dialogStage;
    private boolean okClicked = false;
    private String username;
    private String password;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @FXML
    private void onOk() {
        if (txtUsername.getText().trim().isEmpty()) {
            showError("Логин не может быть пустым");
            return;
        }
        username = txtUsername.getText().trim();
        password = txtPassword.getText();
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}