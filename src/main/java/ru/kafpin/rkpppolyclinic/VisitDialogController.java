package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.PatientDao;
import ru.kafpin.rkpppolyclinic.dao.ScheduleDao;
import ru.kafpin.rkpppolyclinic.models.Patient;
import ru.kafpin.rkpppolyclinic.models.Schedule;
import ru.kafpin.rkpppolyclinic.models.Visit;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class VisitDialogController {

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private ComboBox<Schedule> cbSchedule;
    @FXML private ComboBox<String> cbStatus;

    private Stage dialogStage;
    private Visit visit;
    private boolean okClicked = false;
    private ScheduleDao scheduleDao;
    private PatientDao patientDao;

    @FXML
    public void initialize() {
        cbStatus.setItems(FXCollections.observableArrayList("запланирован", "завершён", "отменён"));

        cbPatient.setConverter(new javafx.util.StringConverter<Patient>() {
            @Override
            public String toString(Patient p) {
                return p == null ? "" : p.getLastName() + " " + p.getFirstName();
            }
            @Override
            public Patient fromString(String s) { return null; }
        });

        cbSchedule.setConverter(new javafx.util.StringConverter<Schedule>() {
            @Override
            public String toString(Schedule s) {
                if (s == null) return "";
                return "Врач " + s.getDoctorId() + ", " +
                        s.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        " (" + s.getAppointmentStatus() + ")";
            }
            @Override
            public Schedule fromString(String s) { return null; }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setVisit(Visit visit, ScheduleDao scheduleDao, PatientDao patientDao) {
        this.visit = visit;
        this.scheduleDao = scheduleDao;
        this.patientDao = patientDao;

        List<Patient> patients = patientDao.findAll();
        cbPatient.setItems(FXCollections.observableArrayList(patients));

        List<Schedule> schedules = scheduleDao.findAll();
        cbSchedule.setItems(FXCollections.observableArrayList(schedules));

        if (visit.getPatientId() != 0) {
            Patient p = patientDao.findById(visit.getPatientId());
            cbPatient.getSelectionModel().select(p);
        }
        if (visit.getScheduleId() != 0) {
            Schedule s = scheduleDao.findById(visit.getScheduleId());
            cbSchedule.getSelectionModel().select(s);
        }
        if (visit.getStatus() != null) {
            cbStatus.setValue(visit.getStatus());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        if (cbPatient.getSelectionModel().getSelectedItem() == null) {
            showError("Выберите пациента");
            return;
        }
        if (cbSchedule.getSelectionModel().getSelectedItem() == null) {
            showError("Выберите слот расписания");
            return;
        }
        if (cbStatus.getValue() == null) {
            showError("Выберите статус");
            return;
        }

        Schedule selectedSchedule = cbSchedule.getSelectionModel().getSelectedItem();
        Patient selectedPatient = cbPatient.getSelectionModel().getSelectedItem();

        visit.setPatientId(selectedPatient.getId());
        visit.setScheduleId(selectedSchedule.getId());
        visit.setDateTime(selectedSchedule.getAppointmentDateTime());
        visit.setStatus(cbStatus.getValue());

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