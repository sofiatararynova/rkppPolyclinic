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
import ru.kafpin.rkpppolyclinic.dao.ScheduleDao;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;
import ru.kafpin.rkpppolyclinic.models.Schedule;
import ru.kafpin.rkpppolyclinic.models.Doctor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ScheduleController {

    private ScheduleDao scheduleDao = new ScheduleDao();
    private DoctorDao doctorDao = new DoctorDao();

    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    private Map<Long, Doctor> doctorsMap = new HashMap<>();

    @FXML private TableView<Schedule> tvSchedule;
    @FXML private TableColumn<Schedule, Long> colDoctorId;
    @FXML private TableColumn<Schedule, String> colDoctorName;
    @FXML private TableColumn<Schedule, String> colDateTime;
    @FXML private TableColumn<Schedule, String> colStatus;

    @FXML
    public void initialize() {

        loadDoctorsMap();


        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));

        colDoctorName.setCellValueFactory(cellData -> {
            long docId = cellData.getValue().getDoctorId();
            Doctor doctor = doctorsMap.get(docId);
            String fullName = (doctor != null) ? doctor.getLastName() + " " + doctor.getFirstName() : "Неизвестен";
            return new javafx.beans.property.SimpleStringProperty(fullName);
        });

        colDateTime.setCellValueFactory(cellData -> {
            var dateTime = cellData.getValue().getAppointmentDateTime();
            String formatted = (dateTime != null)
                    ? dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                    : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        colStatus.setCellValueFactory(new PropertyValueFactory<>("appointmentStatus"));

        loadData();

        tvSchedule.setItems(scheduleList);
    }


    private void loadDoctorsMap() {
        doctorsMap.clear();
        for (Doctor d : doctorDao.findAll()) {
            doctorsMap.put(d.getId(), d);
        }
    }

    private void loadData() {
        scheduleList.clear();
        scheduleList.addAll(scheduleDao.findAll());
        // Если добавились новые врачи – обновляем кэш
        loadDoctorsMap();
    }


    @FXML
    private void onAdd() throws IOException {
        Schedule newSchedule = new Schedule();
        if (showDialog(newSchedule)) {
            scheduleDao.save(newSchedule);
            loadData();
        }
    }


    @FXML
    private void onEdit() throws IOException {
        Schedule selected = tvSchedule.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите запись для редактирования");
            return;
        }
        if (showDialog(selected)) {
            scheduleDao.update(selected);
            loadData();
        }
    }


    @FXML
    private void onDelete() {
        Schedule selected = tvSchedule.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите запись для удаления");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить выбранную запись?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                scheduleDao.delete(selected.getId());
                loadData();
            }
        });
    }


    private boolean showDialog(Schedule schedule) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule-dialog.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.load()));
        dialogStage.setTitle(schedule.getId() == 0 ? "Добавление слота" : "Редактирование слота");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(MainApplication.getStage());

        ScheduleDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setSchedule(schedule);


        dialogStage.showAndWait();

        return controller.isOkClicked();
    }


    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}