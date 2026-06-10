package ru.kafpin.rkpppolyclinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.models.Patient;

public class NewPatientController {
    private Stage stage;
    private Patient patient;

    @FXML private TextField tfLastName;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfMiddleName;
    @FXML private DatePicker dpDateOfBirth;
    @FXML private TextField tfResidentialAddress;

    @FXML
    void onCancelClick(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onOkClick(ActionEvent event) {
        if (tfLastName.getText().isEmpty() || tfFirstName.getText().isEmpty()) {
            showError("Заполните обязательные поля: Фамилия и Имя");
            return;
        }

        patient.setLastName(tfLastName.getText());
        patient.setFirstName(tfFirstName.getText());
        patient.setMiddleName(tfMiddleName.getText());
        patient.setDateOfBirth(dpDateOfBirth.getValue());
        patient.setResidentialAddress(tfResidentialAddress.getText());

        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        tfLastName.setText(patient.getLastName());
        tfFirstName.setText(patient.getFirstName());
        tfMiddleName.setText(patient.getMiddleName());
        dpDateOfBirth.setValue(patient.getDateOfBirth());
        tfResidentialAddress.setText(patient.getResidentialAddress());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}