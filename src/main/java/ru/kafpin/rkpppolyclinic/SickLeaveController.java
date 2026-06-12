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
import ru.kafpin.rkpppolyclinic.dao.SickLeaveDao;
import ru.kafpin.rkpppolyclinic.models.SickLeave;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SickLeaveController {

    private SickLeaveDao sickLeaveDao = new SickLeaveDao();
    private ObservableList<SickLeave> sickLeaveList = FXCollections.observableArrayList();

    @FXML private TableView<SickLeave> tvSickLeave;
    @FXML private TableColumn<SickLeave, Long> colVisitId;
    @FXML private TableColumn<SickLeave, String> colIssueDate;
    @FXML private TableColumn<SickLeave, String> colContent;
    @FXML private TableColumn<SickLeave, String> colStatus;

    @FXML
    public void initialize() {
        colVisitId.setCellValueFactory(new PropertyValueFactory<>("visitId"));
        colIssueDate.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getIssueDate();
            String formatted = (date != null) ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();
        tvSickLeave.setItems(sickLeaveList);
    }

    private void loadData() {
        sickLeaveList.clear();
        sickLeaveList.addAll(sickLeaveDao.findAll());
    }

    @FXML
    private void onAdd() throws IOException {
        SickLeave newSickLeave = new SickLeave();
        if (showDialog(newSickLeave)) {
            sickLeaveDao.save(newSickLeave);
            loadData();
        }
    }

    @FXML
    private void onEdit() throws IOException {
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

    @FXML
    private void onDelete() {
        SickLeave selected = tvSickLeave.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите больничный лист");
            return;
        }
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