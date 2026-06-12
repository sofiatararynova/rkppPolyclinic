package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;
import ru.kafpin.rkpppolyclinic.dao.PatientDao;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.Patient;
import ru.kafpin.rkpppolyclinic.DoctorPatientsDialogController;

import java.io.IOException;

public class MainController {
    private PatientDao patientDao;
    private DoctorDao doctorDao;

    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();

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

    public MainController() {
        this.patientDao = new PatientDao();
        this.doctorDao = new DoctorDao();
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

    private void showPatientDialog(Patient p) throws IOException {
        openWindow("new-patient.fxml", "Пациент", p);
    }
    private void showDoctorDialog(Doctor d) throws IOException {
        openWindow("new-doctor.fxml", "Врач", d);
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

        stage.showAndWait();
        return loader;
    }

    @FXML
    void onDoctorPatientsClick(ActionEvent event) throws IOException {
        Doctor selected = tvDoctorsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Выберите врача");
            alert.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-patients.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Пациенты врача за период");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        DoctorPatientsDialogController controller = loader.getController();
        controller.setDoctor(selected);
        stage.showAndWait();
    }
}