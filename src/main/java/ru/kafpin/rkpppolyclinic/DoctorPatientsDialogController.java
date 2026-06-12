package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.DoctorPatientVisit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoctorPatientsDialogController {

    @FXML private Label lblDoctor;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TableView<DoctorPatientVisit> tvPatients;
    @FXML private TableColumn<DoctorPatientVisit, String> colLastName;
    @FXML private TableColumn<DoctorPatientVisit, String> colFirstName;
    @FXML private TableColumn<DoctorPatientVisit, String> colMiddleName;
    @FXML private TableColumn<DoctorPatientVisit, String> colVisitDate;

    private Doctor doctor;
    private DoctorDao doctorDao = new DoctorDao();

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        lblDoctor.setText("Врач: " + doctor.getLastName() + " " + doctor.getFirstName());
        dpStart.setValue(LocalDate.now().minusMonths(1));
        dpEnd.setValue(LocalDate.now());
    }

    @FXML
    public void initialize() {
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colVisitDate.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getVisitDate();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : ""
            );
        });
    }

    @FXML
    private void onShow() {
        if (dpStart.getValue() == null || dpEnd.getValue() == null) {
            showError("Выберите даты");
            return;
        }
        List<DoctorPatientVisit> data = doctorDao.getPatientsByDoctor(doctor.getId(), dpStart.getValue(), dpEnd.getValue());
        tvPatients.setItems(FXCollections.observableArrayList(data));
    }

    @FXML
    private void onClose() {
        Stage s = (Stage) tvPatients.getScene().getWindow();
        s.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}