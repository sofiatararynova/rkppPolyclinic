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
import java.util.Map;
import java.util.stream.Collectors;
import ru.kafpin.rkpppolyclinic.ScheduleController;

public class VisitController {

    private VisitDao visitDao = new VisitDao();
    private PatientDao patientDao = new PatientDao();
    private ScheduleDao scheduleDao = new ScheduleDao();
    private DoctorDao doctorDao = new DoctorDao();

    private ObservableList<Visit> visitList = FXCollections.observableArrayList();
    private Map<Long, Patient> patientMap;
    private Map<Long, Schedule> scheduleMap;
    private Map<Long, Doctor> doctorMap;

    @FXML private TableView<Visit> tvVisit;
    @FXML private TableColumn<Visit, String> colPatient;
    @FXML private TableColumn<Visit, String> colDoctor;
    @FXML private TableColumn<Visit, String> colDateTime;
    @FXML private TableColumn<Visit, String> colStatus;

    @FXML
    public void initialize() {
        loadMaps();

        colPatient.setCellValueFactory(cellData -> {
            Patient p = patientMap.get(cellData.getValue().getPatientId());
            String name = (p != null) ? p.getLastName() + " " + p.getFirstName() : "";
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        colDoctor.setCellValueFactory(cellData -> {
            Schedule s = scheduleMap.get(cellData.getValue().getScheduleId());
            if (s != null) {
                Doctor d = doctorMap.get(s.getDoctorId());
                String doctorName = (d != null) ? d.getLastName() + " " + d.getFirstName() : "";
                return new javafx.beans.property.SimpleStringProperty(doctorName);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colDateTime.setCellValueFactory(cellData -> {
            var dt = cellData.getValue().getDateTime();
            String formatted = (dt != null) ? dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();
        tvVisit.setItems(visitList);
    }

    private void loadMaps() {
        patientMap = patientDao.findAll().stream().collect(Collectors.toMap(Patient::getId, p -> p));
        scheduleMap = scheduleDao.findAll().stream().collect(Collectors.toMap(Schedule::getId, s -> s));
        doctorMap = doctorDao.findAll().stream().collect(Collectors.toMap(Doctor::getId, d -> d));
    }

    private void loadData() {
        visitList.clear();
        visitList.addAll(visitDao.findAll());
        loadMaps();
    }

    @FXML
    private void onAdd() throws IOException {
        Visit newVisit = new Visit();
        if (showDialog(newVisit)) {
            visitDao.save(newVisit);
            loadData();
            ScheduleController.refreshTable();
        }
    }

    @FXML
    private void onEdit() throws IOException {
        Visit selected = tvVisit.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите визит");
            return;
        }
        if (showDialog(selected)) {
            visitDao.update(selected);
            loadData();
            ScheduleController.refreshTable();
        }
    }

    @FXML
    private void onDelete() {
        Visit selected = tvVisit.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите визит");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удалить визит?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                visitDao.delete(selected.getId());
                loadData();
                ScheduleController.refreshTable();
            }
        });
    }

    private boolean showDialog(Visit visit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("visit-dialog.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.load()));
        dialogStage.setTitle(visit.getId() == 0 ? "Добавление визита" : "Редактирование визита");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(MainApplication.getStage());

        VisitDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVisit(visit, scheduleDao, patientDao);

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