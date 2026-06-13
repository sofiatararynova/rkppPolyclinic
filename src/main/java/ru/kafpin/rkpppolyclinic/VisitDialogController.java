package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.*;
import ru.kafpin.rkpppolyclinic.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class VisitDialogController {

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private ComboBox<Schedule> cbSchedule;
    @FXML private ComboBox<String> cbStatus;
    @FXML private DatePicker dpSickLeaveDate;
    @FXML private TextField tfSickLeaveContent;
    @FXML private ComboBox<String> cbSickLeaveStatus;
    @FXML private TextField tfIcdCode;

    private Stage dialogStage;
    private Visit visit;
    private boolean okClicked = false;
    private ScheduleDao scheduleDao;
    private PatientDao patientDao;
    private SickLeaveDao sickLeaveDao = new SickLeaveDao();
    private DiagnosisDao diagnosisDao = new DiagnosisDao();

    @FXML
    public void initialize() {
        cbStatus.setItems(FXCollections.observableArrayList("запланирован", "завершён", "отменён"));
        cbSickLeaveStatus.setItems(FXCollections.observableArrayList("активен", "закрыт", "аннулирован"));

        cbPatient.setConverter(new javafx.util.StringConverter<Patient>() {
            @Override public String toString(Patient p) { return p == null ? "" : p.getLastName() + " " + p.getFirstName(); }
            @Override public Patient fromString(String s) { return null; }
        });
        cbSchedule.setConverter(new javafx.util.StringConverter<Schedule>() {
            @Override public String toString(Schedule s) {
                if (s == null) return "";
                return "Врач " + s.getDoctorId() + ", " +
                        s.getAppointmentDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        " (" + s.getAppointmentStatus() + ")";
            }
            @Override public Schedule fromString(String s) { return null; }
        });
    }

    public void setDialogStage(Stage stage) { this.dialogStage = stage; }

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
        if (visit.getStatus() != null) cbStatus.setValue(visit.getStatus());

        // Загружаем существующий больничный и диагноз для этого визита
        SickLeave existing = sickLeaveDao.findByVisitId(visit.getId());
        if (existing != null) {
            dpSickLeaveDate.setValue(existing.getIssueDate());
            tfSickLeaveContent.setText(existing.getContent());
            cbSickLeaveStatus.setValue(existing.getStatus());
            List<Diagnosis> diagnoses = diagnosisDao.findBySickLeaveId(existing.getId());
            if (!diagnoses.isEmpty()) tfIcdCode.setText(diagnoses.get(0).getIcdCode());
        }
    }

    public boolean isOkClicked() { return okClicked; }

    @FXML
    private void onOk() {
        if (cbPatient.getSelectionModel().getSelectedItem() == null) { showError("Выберите пациента"); return; }
        if (cbSchedule.getSelectionModel().getSelectedItem() == null) { showError("Выберите слот расписания"); return; }
        if (cbStatus.getValue() == null) { showError("Выберите статус визита"); return; }

        Schedule selectedSchedule = cbSchedule.getSelectionModel().getSelectedItem();
        Patient selectedPatient = cbPatient.getSelectionModel().getSelectedItem();

        visit.setPatientId(selectedPatient.getId());
        visit.setScheduleId(selectedSchedule.getId());
        visit.setDateTime(selectedSchedule.getAppointmentDateTime());
        visit.setStatus(cbStatus.getValue());

        if (dpSickLeaveDate.getValue() != null) {
            SickLeave sickLeave = sickLeaveDao.findByVisitId(visit.getId());
            if (sickLeave == null) {
                sickLeave = new SickLeave();
                sickLeave.setVisitId(visit.getId());
            }
            sickLeave.setIssueDate(dpSickLeaveDate.getValue());
            sickLeave.setContent(tfSickLeaveContent.getText());
            sickLeave.setStatus(cbSickLeaveStatus.getValue() != null ? cbSickLeaveStatus.getValue() : "активен");
            if (sickLeave.getId() == 0) sickLeaveDao.save(sickLeave);
            else sickLeaveDao.update(sickLeave);

            // Диагноз
            if (!tfIcdCode.getText().trim().isEmpty()) {
                List<Diagnosis> existingDiag = diagnosisDao.findBySickLeaveId(sickLeave.getId());
                for (Diagnosis d : existingDiag) diagnosisDao.delete(d.getSickLeaveId(), d.getIcdCode());
                Diagnosis newDiag = new Diagnosis(sickLeave.getId(), tfIcdCode.getText().trim());
                diagnosisDao.save(newDiag);
            }
        } else {
            // Если дата не указана, удаляем существующий больничный и диагноз
            SickLeave sl = sickLeaveDao.findByVisitId(visit.getId());
            if (sl != null) {
                List<Diagnosis> diags = diagnosisDao.findBySickLeaveId(sl.getId());
                for (Diagnosis d : diags) diagnosisDao.delete(d.getSickLeaveId(), d.getIcdCode());
                sickLeaveDao.delete(sl.getId());
            }
        }

        okClicked = true;
        dialogStage.close();
    }

    @FXML private void onCancel() { dialogStage.close(); }
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}