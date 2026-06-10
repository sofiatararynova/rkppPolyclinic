package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.*;
import ru.kafpin.rkpppolyclinic.models.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainController {
    private Dao<Patient, Long> patientDao;
    private Dao<Doctor, Long> doctorDao;
    private Dao<MedicalAppointment, Long> appointmentDao;
    private Dao<Diagnosis, Long> diagnosisDao;

    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<MedicalAppointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Diagnosis> diagnoses = FXCollections.observableArrayList();

    @FXML private TableView<Patient> tvPatientsTable;
    @FXML private TableColumn<Patient, String> pLastNameColumn;
    @FXML private TableColumn<Patient, String> pFirstNameColumn;
    @FXML private TableColumn<Patient, String> pPatronymicColumn;
    @FXML private TableColumn<Patient, String> pAddressColumn;
    @FXML private Label lbPatientInfo;

    @FXML private TableView<Doctor> tvDoctorsTable;
    @FXML private TableColumn<Doctor, String> dLastNameColumn;
    @FXML private TableColumn<Doctor, String> dFirstNameColumn;
    @FXML private TableColumn<Doctor, String> dPatronymicColumn;
    @FXML private TableColumn<Doctor, String> dSpecialtyTextColumn;
    @FXML private TableColumn<Doctor, Integer> dExperienceColumn;
    @FXML private Label lbDoctorInfo;

    @FXML private TableView<MedicalAppointment> tvAppointmentsTable;
    @FXML private TableColumn<MedicalAppointment, Long> aPatientIdColumn;
    @FXML private TableColumn<MedicalAppointment, Long> aDoctorIdColumn;
    @FXML private TableColumn<MedicalAppointment, LocalDateTime> aDateTimeColumn;
    @FXML private TableColumn<MedicalAppointment, String> aOfficeColumn;
    @FXML private TableColumn<MedicalAppointment, String> aStatusColumn;
    @FXML private Label lbAppointmentInfo;

    @FXML private TableView<Diagnosis> tvDiagnosesTable;
    @FXML private TableColumn<Diagnosis, Long> dSickLeaveColumn;
    @FXML private TableColumn<Diagnosis, String> dIcdColumn;
    @FXML private Label lbDiagnosisInfo;

    public MainController() {
        this.patientDao = new PatientDao();
        this.doctorDao = new DoctorDao();
        this.appointmentDao = new MedicalAppointmentDao();
        this.diagnosisDao = new DiagnosisDao();
    }

    @FXML
    public void initialize() {
        pLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        pFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        pPatronymicColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        pAddressColumn.setCellValueFactory(new PropertyValueFactory<>("residentialAddress"));
        patients.addAll(patientDao.findAll());
        tvPatientsTable.setItems(patients);

        dLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        dPatronymicColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        dSpecialtyTextColumn.setCellValueFactory(new PropertyValueFactory<>("specialtyText"));
        dExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experienceYears"));
        doctors.addAll(doctorDao.findAll());
        tvDoctorsTable.setItems(doctors);

        aPatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        aDoctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        aDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        aOfficeColumn.setCellValueFactory(new PropertyValueFactory<>("officeNumber"));
        aStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        appointments.addAll(appointmentDao.findAll());
        tvAppointmentsTable.setItems(appointments);

        dSickLeaveColumn.setCellValueFactory(new PropertyValueFactory<>("sickLeaveId"));
        dIcdColumn.setCellValueFactory(new PropertyValueFactory<>("icdCode"));
        diagnoses.addAll(diagnosisDao.findAll());
        tvDiagnosesTable.setItems(diagnoses);
    }

    @FXML void onAddPatientClick(ActionEvent event) throws IOException {
        Patient p = new Patient(); showPatientDialog(p);
        if (p.getLastName() != null) { patientDao.save(p); patients.add(p); lbPatientInfo.setText("Добавлен пациент"); }
    }
    @FXML void onEditPatientClick(ActionEvent event) throws IOException {
        Patient selected = tvPatientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; showPatientDialog(selected); patientDao.update(selected); tvPatientsTable.refresh();
    }
    @FXML void onDeletePatientClick(ActionEvent event) {
        Patient selected = tvPatientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; patientDao.delete(selected.getId()); patients.remove(selected);
    }

    @FXML void onAddDoctorClick(ActionEvent event) throws IOException {
        Doctor d = new Doctor(); showDoctorDialog(d);
        if (d.getLastName() != null) { doctorDao.save(d); doctors.add(d); }
    }
    @FXML void onEditDoctorClick(ActionEvent event) throws IOException {
        Doctor selected = tvDoctorsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; showDoctorDialog(selected); doctorDao.update(selected); tvDoctorsTable.refresh();
    }
    @FXML void onDeleteDoctorClick(ActionEvent event) {
        Doctor selected = tvDoctorsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; doctorDao.delete(selected.getId()); doctors.remove(selected);
    }

    @FXML void onAddAppointmentClick(ActionEvent event) throws IOException {
        MedicalAppointment app = new MedicalAppointment(); showAppointmentDialog(app);
        if (app.getPatientId() > 0) { appointmentDao.save(app); appointments.add(app); }
    }
    @FXML void onEditAppointmentClick(ActionEvent event) throws IOException {
        MedicalAppointment selected = tvAppointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; showAppointmentDialog(selected); appointmentDao.update(selected); tvAppointmentsTable.refresh();
    }
    @FXML void onDeleteAppointmentClick(ActionEvent event) {
        MedicalAppointment selected = tvAppointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return; appointmentDao.delete(selected.getId()); appointments.remove(selected);
    }

    // МЕТОДЫ ДИАГНОЗОВ
    @FXML void onAddDiagnosisClick(ActionEvent event) throws IOException {
        Diagnosis diag = new Diagnosis(); showDiagnosisDialog(diag);
        if (diag.getIcdCode() != null && !diag.getIcdCode().isEmpty()) {
            diagnosisDao.save(diag);
            diagnoses.add(diag);
            lbDiagnosisInfo.setText("Добавлен диагноз");
        }
    }
    @FXML void onEditDiagnosisClick(ActionEvent event) throws IOException {
        Diagnosis selected = tvDiagnosesTable.getSelectionModel().getSelectedItem();
        if (selected == null) { lbDiagnosisInfo.setText("Выберите диагноз!"); return; }
        showDiagnosisDialog(selected);
        diagnosisDao.update(selected);
        tvDiagnosesTable.refresh();
        lbDiagnosisInfo.setText("Диагноз обновлен");
    }
    @FXML void onDeleteDiagnosisClick(ActionEvent event) {
        Diagnosis selected = tvDiagnosesTable.getSelectionModel().getSelectedItem();
        if (selected == null) { lbDiagnosisInfo.setText("Выберите диагноз!"); return; }
        diagnosisDao.delete(selected.getSickLeaveId());
        diagnoses.remove(selected);
        lbDiagnosisInfo.setText("Диагноз удален");
    }

    private void showPatientDialog(Patient p) throws IOException {
        openWindow("new-patient.fxml", "Пациент", p);
    }
    private void showDoctorDialog(Doctor d) throws IOException {
        openWindow("new-doctor.fxml", "Врач", d);
    }
    private void showAppointmentDialog(MedicalAppointment app) throws IOException {
        openWindow("new-appointment.fxml", "Талон", app);
    }
    private void showDiagnosisDialog(Diagnosis diag) throws IOException {
        openWindow("new-diagnosis.fxml", "Диагноз", diag);
    }

    private FXMLLoader openWindow(String fxml, String title, Object model) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        if (fxml.contains("patient")) {
            NewPatientController c = loader.getController();
            c.setStage(stage);
            c.setPatient((Patient) model);
        }
        if (fxml.contains("doctor")) {
            NewDoctorController c = loader.getController();
            c.setStage(stage);
            c.setDoctor((Doctor) model);
        }
        if (fxml.contains("appointment")) {
            NewAppointmentController c = loader.getController();
            c.setStage(stage);
            c.setAppointment((MedicalAppointment) model);
        }
        if (fxml.contains("diagnosis")) {
            NewDiagnosisController c = loader.getController();
            c.setStage(stage);
            c.setDiagnosis((Diagnosis) model);
        }

        stage.showAndWait();
        return loader;
    }
}