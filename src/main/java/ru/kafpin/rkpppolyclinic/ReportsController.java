package ru.kafpin.rkpppolyclinic;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.kafpin.rkpppolyclinic.dao.DoctorDao;

import java.time.LocalDate;
import java.util.List;

public class ReportsController {

    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TableView<VisitStat> tvVisitsCount;
    @FXML private TableColumn<VisitStat, String> colDoctorName;
    @FXML private TableColumn<VisitStat, Integer> colCompleted;
    @FXML private TableColumn<VisitStat, Integer> colPlanned;
    @FXML private TableColumn<VisitStat, Integer> colCancelled;

    private DoctorDao doctorDao = new DoctorDao();

    @FXML
    public void initialize() {
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colCompleted.setCellValueFactory(new PropertyValueFactory<>("completed"));
        colPlanned.setCellValueFactory(new PropertyValueFactory<>("planned"));
        colCancelled.setCellValueFactory(new PropertyValueFactory<>("cancelled"));
        dpStart.setValue(LocalDate.now().minusMonths(1));
        dpEnd.setValue(LocalDate.now());
    }

    @FXML
    private void onShowVisitsCount() {
        if (dpStart.getValue() == null || dpEnd.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Выберите обе даты");
            alert.showAndWait();
            return;
        }
        List<Object[]> data = doctorDao.getVisitsCountByDoctor(dpStart.getValue(), dpEnd.getValue());
        tvVisitsCount.getItems().clear();
        for (Object[] row : data) {
            tvVisitsCount.getItems().add(new VisitStat(
                    (String) row[1],
                    (Integer) row[2],
                    (Integer) row[3],
                    (Integer) row[4]
            ));
        }
    }

    public static class VisitStat {
        private final String doctorName;
        private final int completed;
        private final int planned;
        private final int cancelled;
        public VisitStat(String doctorName, int completed, int planned, int cancelled) {
            this.doctorName = doctorName;
            this.completed = completed;
            this.planned = planned;
            this.cancelled = cancelled;
        }
        public String getDoctorName() { return doctorName; }
        public int getCompleted() { return completed; }
        public int getPlanned() { return planned; }
        public int getCancelled() { return cancelled; }
    }
}