package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ScheduleDialogController {

    @FXML private ComboBox<Doctor> cbDoctor;
    @FXML private DatePicker dpDate;
    @FXML private ComboBox<String> cbTime;
    @FXML private ComboBox<String> cbStatus;

    private Stage dialogStage;
    private Schedule schedule;
    private boolean okClicked = false;
    private DoctorDao doctorDao = new DoctorDao();

    @FXML
    public void initialize() {
        // Заполняем врачей
        cbDoctor.setItems(FXCollections.observableArrayList(doctorDao.findAll()));
        cbDoctor.setConverter(new javafx.util.StringConverter<Doctor>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : doctor.getLastName() + " " + doctor.getFirstName();
            }
            @Override
            public Doctor fromString(String string) { return null; }
        });

        // Заполняем возможное время приёма
        cbTime.setItems(FXCollections.observableArrayList(
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
        ));

        // Заполняем статусы
        cbStatus.setItems(FXCollections.observableArrayList("свободно", "занято", "отменён"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        if (schedule.getDoctorId() != 0) {
            Doctor d = doctorDao.findById(schedule.getDoctorId());
            cbDoctor.getSelectionModel().select(d);
        }
        if (schedule.getAppointmentDateTime() != null) {
            dpDate.setValue(schedule.getAppointmentDateTime().toLocalDate());
            cbTime.setValue(schedule.getAppointmentDateTime().toLocalTime().toString());
        }
        if (schedule.getAppointmentStatus() != null) {
            cbStatus.setValue(schedule.getAppointmentStatus());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        if (cbDoctor.getSelectionModel().getSelectedItem() == null) {
            showError("Выберите врача");
            return;
        }
        if (dpDate.getValue() == null) {
            showError("Выберите дату");
            return;
        }
        if (cbTime.getValue() == null) {
            showError("Выберите время");
            return;
        }
        if (cbStatus.getValue() == null) {
            showError("Выберите статус");
            return;
        }

        schedule.setDoctorId(cbDoctor.getSelectionModel().getSelectedItem().getId());
        schedule.setAppointmentDateTime(LocalDateTime.of(dpDate.getValue(), LocalTime.parse(cbTime.getValue())));
        schedule.setAppointmentStatus(cbStatus.getValue());

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}