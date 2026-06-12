package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.SickLeaveDao;
import ru.kafpin.rkpppolyclinic.models.Diagnosis;
import ru.kafpin.rkpppolyclinic.models.SickLeave;

public class DiagnosisDialogController {

    @FXML private ComboBox<SickLeave> cbSickLeave;
    @FXML private TextField tfIcdCode;

    private Stage dialogStage;
    private Diagnosis diagnosis;
    private boolean okClicked = false;
    private SickLeaveDao sickLeaveDao = new SickLeaveDao();

    @FXML
    public void initialize() {
        cbSickLeave.setItems(FXCollections.observableArrayList(sickLeaveDao.findAll()));
        cbSickLeave.setConverter(new javafx.util.StringConverter<SickLeave>() {
            @Override
            public String toString(SickLeave sl) {
                if (sl == null) return "";
                return "Больничный #" + sl.getId() + " (визит " + sl.getVisitId() + ")";
            }
            @Override
            public SickLeave fromString(String s) { return null; }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        // При редактировании можно предвыбрать, но мы делаем только добавление
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        SickLeave selected = cbSickLeave.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите больничный лист");
            return;
        }
        String icd = tfIcdCode.getText().trim();
        if (icd.isEmpty()) {
            showError("Введите код МКБ");
            return;
        }

        diagnosis.setSickLeaveId(selected.getId());
        diagnosis.setIcdCode(icd);
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}