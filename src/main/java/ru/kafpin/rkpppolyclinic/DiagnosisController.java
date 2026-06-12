package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.DiagnosisDao;
import ru.kafpin.rkpppolyclinic.models.Diagnosis;

import java.io.IOException;

public class DiagnosisController {

    private DiagnosisDao diagnosisDao = new DiagnosisDao();
    private ObservableList<Diagnosis> diagnosisList = FXCollections.observableArrayList();

    @FXML private TableView<Diagnosis> tvDiagnosis;
    @FXML private TableColumn<Diagnosis, Long> colSickLeaveId;
    @FXML private TableColumn<Diagnosis, String> colIcdCode;

    @FXML
    public void initialize() {
        colSickLeaveId.setCellValueFactory(new PropertyValueFactory<>("sickLeaveId"));
        colIcdCode.setCellValueFactory(new PropertyValueFactory<>("icdCode"));
        loadData();
        tvDiagnosis.setItems(diagnosisList);
    }

    private void loadData() {
        diagnosisList.clear();
        diagnosisList.addAll(diagnosisDao.findAll());
    }

    @FXML
    private void onAdd() throws IOException {
        Diagnosis newDiagnosis = new Diagnosis();
        if (showDialog(newDiagnosis)) {
            diagnosisDao.save(newDiagnosis);
            loadData();
        }
    }

    @FXML
    private void onDelete() {
        Diagnosis selected = tvDiagnosis.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите диагноз для удаления");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удалить диагноз?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                diagnosisDao.delete(selected.getSickLeaveId(), selected.getIcdCode());
                loadData();
            }
        });
    }

    private boolean showDialog(Diagnosis diagnosis) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("diagnosis-dialog.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.load()));
        dialogStage.setTitle("Добавление диагноза");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(MainApplication.getStage());

        DiagnosisDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setDiagnosis(diagnosis);

        dialogStage.showAndWait();
        return controller.isOkClicked();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}