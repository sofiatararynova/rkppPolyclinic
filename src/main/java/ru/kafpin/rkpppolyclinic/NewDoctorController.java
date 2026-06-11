package ru.kafpin.rkpppolyclinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.SpecialtyDao;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.Specialty;

public class NewDoctorController {
    private Stage stage;
    private Doctor doctor;
    private SpecialtyDao specialtyDao = new SpecialtyDao();

    @FXML private TextField tfLastName;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfMiddleName;
    @FXML private ComboBox<Specialty> cbSpecialty;
//    @FXML private TextField tfSpecialtyText;
    @FXML private TextField tfExperience;
    @FXML private DatePicker dpDateOfBirth;
    @FXML private TextField tfResidentialAddress;

    @FXML
    public void initialize() {
        cbSpecialty.getItems().addAll(specialtyDao.findAll());
    }

    @FXML
    void onCancelClick(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onOkClick(ActionEvent event) {
        if (tfLastName.getText().isEmpty() || tfFirstName.getText().isEmpty()) {
            showError("Заполните обязательные поля: Фамилия и Имя");
            return;
        }

        doctor.setLastName(tfLastName.getText());
        doctor.setFirstName(tfFirstName.getText());
        doctor.setMiddleName(tfMiddleName.getText());

        Specialty selected = cbSpecialty.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите специальность из списка");
            return;
        }

        doctor.setSpecialtyId(selected.getId());
        doctor.setSpecialtyText(selected.getName());

//        doctor.setSpecialtyText(tfSpecialtyText.getText());

//        try {
//            doctor.setExperienceYears(tfExperience.getText().isEmpty() ? 0 : Integer.parseInt(tfExperience.getText()));
//        } catch (NumberFormatException e) {
//            showError("Стаж должен быть числом!");
//            return;
//        }

        String experienceText = tfExperience.getText().trim();
        if (experienceText.isEmpty()) {
            showError("Введите стаж");
            return;
        }
        int experience;
        try {
            experience = Integer.parseInt(experienceText);
        } catch (NumberFormatException e) {
            showError("Стаж должен быть целым числом");
            return;
        }
        if (experience < 0 || experience > 70) {
            showError("Стаж должен быть от 0 до 70 лет");
            return;
        }
        doctor.setExperienceYears(experience);


        doctor.setDateOfBirth(dpDateOfBirth.getValue());
        doctor.setResidentialAddress(tfResidentialAddress.getText());

        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        tfLastName.setText(doctor.getLastName());
        tfFirstName.setText(doctor.getFirstName());
        tfMiddleName.setText(doctor.getMiddleName());
//        tfSpecialtyText.setText(doctor.getSpecialtyText());
        tfExperience.setText(String.valueOf(doctor.getExperienceYears()));
        dpDateOfBirth.setValue(doctor.getDateOfBirth());
        tfResidentialAddress.setText(doctor.getResidentialAddress());

        if (doctor.getSpecialtyId() > 0) {
            for (Specialty s : cbSpecialty.getItems()) {
                if (s.getId() == doctor.getSpecialtyId()) {
                    cbSpecialty.getSelectionModel().select(s);
                    break;
                }
            }
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