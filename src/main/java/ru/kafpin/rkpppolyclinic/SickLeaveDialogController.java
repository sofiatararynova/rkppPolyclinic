package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.VisitDao;
import ru.kafpin.rkpppolyclinic.models.SickLeave;
import ru.kafpin.rkpppolyclinic.models.Visit;

import java.time.LocalDate;
import java.util.List;

public class SickLeaveDialogController {

    @FXML private ComboBox<Visit> cbVisit;
    @FXML private DatePicker dpIssueDate;
    @FXML private TextField tfContent;
    @FXML private ComboBox<String> cbStatus;

    private Stage dialogStage;
    private SickLeave sickLeave;
    private boolean okClicked = false;
    private VisitDao visitDao = new VisitDao();

    @FXML
    public void initialize() {
        cbStatus.setItems(FXCollections.observableArrayList("активен", "закрыт", "аннулирован"));
        cbVisit.setConverter(new javafx.util.StringConverter<Visit>() {
            @Override
            public String toString(Visit v) {
                return v == null ? "" : "Визит #" + v.getId() + " (пациент " + v.getPatientId() + ")";
            }
            @Override
            public Visit fromString(String s) { return null; }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setSickLeave(SickLeave sickLeave) {
        this.sickLeave = sickLeave;
        List<Visit> visits = visitDao.findAll();
        cbVisit.setItems(FXCollections.observableArrayList(visits));

        if (sickLeave.getVisitId() != 0) {
            Visit v = visitDao.findById(sickLeave.getVisitId());
            cbVisit.getSelectionModel().select(v);
        }
        if (sickLeave.getIssueDate() != null) {
            dpIssueDate.setValue(sickLeave.getIssueDate());
        }
        tfContent.setText(sickLeave.getContent());
        cbStatus.setValue(sickLeave.getStatus());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        if (cbVisit.getSelectionModel().getSelectedItem() == null) {
            showError("Выберите визит");
            return;
        }
        if (dpIssueDate.getValue() == null) {
            showError("Выберите дату выдачи");
            return;
        }
        if (cbStatus.getValue() == null) {
            showError("Выберите статус");
            return;
        }

        sickLeave.setVisitId(cbVisit.getSelectionModel().getSelectedItem().getId());
        sickLeave.setIssueDate(dpIssueDate.getValue());
        sickLeave.setContent(tfContent.getText());
        sickLeave.setStatus(cbStatus.getValue());

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