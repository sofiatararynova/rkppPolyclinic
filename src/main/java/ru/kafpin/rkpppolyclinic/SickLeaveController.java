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
import ru.kafpin.rkpppolyclinic.dao.*;
import ru.kafpin.rkpppolyclinic.models.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SickLeaveController {

    private SickLeaveDao sickLeaveDao = new SickLeaveDao();
    private DiagnosisDao diagnosisDao = new DiagnosisDao();  // для получения диагноза
    private ObservableList<SickLeave> sickLeaveList = FXCollections.observableArrayList();

    @FXML private TableView<SickLeave> tvSickLeave;
    @FXML private TableColumn<SickLeave, String> colPatient;
    @FXML private TableColumn<SickLeave, String> colIssueDate;
    @FXML private TableColumn<SickLeave, String> colContent;
    @FXML private TableColumn<SickLeave, String> colDiagnosis;
    @FXML private TableColumn<SickLeave, String> colStatus;

    @FXML
    public void initialize() {
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientFullName"));
        colIssueDate.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getIssueDate();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "");
        });
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Настройка колонки диагноза
        colDiagnosis.setCellValueFactory(cellData -> {
            SickLeave sl = cellData.getValue();
            if (sl != null && sl.getId() != 0) {
                List<Diagnosis> diag = diagnosisDao.findBySickLeaveId(sl.getId());
                if (!diag.isEmpty()) {
                    return new javafx.beans.property.SimpleStringProperty(diag.get(0).getIcdCode());
                }
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        loadData();
        tvSickLeave.setItems(sickLeaveList);
    }

    private void loadData() {
        sickLeaveList.clear();
        sickLeaveList.addAll(sickLeaveDao.findAll());
    }

    @FXML private void onAdd() throws IOException {
        SickLeave newSL = new SickLeave();
        if (showDialog(newSL)) {
            sickLeaveDao.save(newSL);
            loadData();
        }
    }

    @FXML private void onEdit() throws IOException {
        SickLeave selected = tvSickLeave.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите больничный лист");
            return;
        }
        if (showDialog(selected)) {
            sickLeaveDao.update(selected);
            loadData();
        }
    }

    @FXML private void onDelete() {
        SickLeave selected = tvSickLeave.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удалить больничный лист?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                sickLeaveDao.delete(selected.getId());
                loadData();
            }
        });
    }

    private boolean showDialog(SickLeave sickLeave) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sickleave-dialog.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.load()));
        dialogStage.setTitle(sickLeave.getId() == 0 ? "Добавление больничного листа" : "Редактирование больничного листа");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(MainApplication.getStage());

        SickLeaveDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setSickLeave(sickLeave);

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