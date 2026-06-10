package ru.kafpin.rkpppolyclinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;
import ru.kafpin.rkpppolyclinic.dao.PatientDao;
import ru.kafpin.rkpppolyclinic.models.MedicalAppointment;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class NewAppointmentController {
    private Stage stage;
    private MedicalAppointment appointment;
    private PatientDao patientDao = new PatientDao();
    private DoctorDao doctorDao = new DoctorDao();

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private ComboBox<Doctor> cbDoctor;
    @FXML private DatePicker dpDate;
    @FXML private TextField tfTime;
    @FXML private TextField tfOffice;
    @FXML private TextField tfStatus;

    @FXML
    public void initialize() {
        cbPatient.getItems().addAll(patientDao.findAll());
        cbPatient.setConverter(new StringConverter<>() {
            @Override public String toString(Patient p) { return p == null ? "" : p.getLastName() + " " + p.getFirstName(); }
            @Override public Patient fromString(String string) { return null; }
        });

        cbDoctor.getItems().addAll(doctorDao.findAll());
        cbDoctor.setConverter(new StringConverter<>() {
            @Override public String toString(Doctor d) { return d == null ? "" : d.getLastName() + " " + d.getFirstName() + " (" + d.getSpecialtyText() + ")"; }
            @Override public Doctor fromString(String string) { return null; }
        });
    }

    @FXML
    void onCancelClick(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onOkClick(ActionEvent event) {
        Doctor doctor = cbDoctor.getSelectionModel().getSelectedItem();
        LocalDate date = dpDate.getValue();

        if (doctor == null || date == null || tfTime.getText().isEmpty()) {
            showError("Заполните врача, дату и время приема!");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(tfTime.getText());
            appointment.setAppointmentDateTime(date.atTime(time));
        } catch (DateTimeParseException e) {
            showError("Некорректный формат времени! Используйте ЧЧ:ММ (например, 14:15)");
            return;
        }

        Patient patient = cbPatient.getSelectionModel().getSelectedItem();
        appointment.setPatientId(patient != null ? patient.getId() : 0);
        appointment.setDoctorId(doctor.getId());
        appointment.setOfficeNumber(tfOffice.getText());
        appointment.setStatusText(tfStatus.getText());

        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAppointment(MedicalAppointment appointment) {
        this.appointment = appointment;
        tfOffice.setText(appointment.getOfficeNumber());
        tfStatus.setText(appointment.getStatusText());

        if (appointment.getAppointmentDateTime() != null) {
            dpDate.setValue(appointment.getAppointmentDateTime().toLocalDate());
            tfTime.setText(appointment.getAppointmentDateTime().toLocalTime().toString());
        }

        if (appointment.getPatientId() > 0) {
            cbPatient.getItems().stream().filter(p -> p.getId() == appointment.getPatientId()).findFirst().ifPresent(p -> cbPatient.getSelectionModel().select(p));
        }
        if (appointment.getDoctorId() > 0) {
            cbDoctor.getItems().stream().filter(d -> d.getId() == appointment.getDoctorId()).findFirst().ifPresent(d -> cbDoctor.getSelectionModel().select(d));
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}