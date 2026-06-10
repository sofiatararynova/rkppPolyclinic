package ru.kafpin.rkpppolyclinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.models.Diagnosis;

public class NewDiagnosisController {
    private Stage stage;
    private Diagnosis diagnosis;

    @FXML private TextField tfSickLeaveId;
    @FXML private TextField tfIcdCode;

    @FXML
    void onCancelClick(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onOkClick(ActionEvent event) {
        String idText = tfSickLeaveId.getText();
        String icdText = tfIcdCode.getText();

        if (idText == null || idText.trim().isEmpty() || icdText == null || icdText.trim().isEmpty()) {
            showError("Заполните все поля формы!");
            return;
        }

        try {
            long cleanId = Long.parseLong(idText.trim());
            diagnosis.setSickLeaveId(cleanId);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка парсинга значения: '" + idText + "'");
            showError("ID Больничного листа должно состоять только из цифр и не содержать пробелов!");
            return;
        }

        diagnosis.setIcdCode(icdText.trim());
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        if (diagnosis.getSickLeaveId() > 0) {
            tfSickLeaveId.setText(String.valueOf(diagnosis.getSickLeaveId()));
            tfSickLeaveId.setEditable(false);
        } else {
            tfSickLeaveId.setText("");
            tfSickLeaveId.setEditable(true);
        }
        tfIcdCode.setText(diagnosis.getIcdCode() != null ? diagnosis.getIcdCode() : "");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}