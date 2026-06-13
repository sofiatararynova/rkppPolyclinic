package ru.kafpin.rkpppolyclinic;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.kafpin.rkpppolyclinic.dao.DiagnosisDao;

import java.util.List;

public class DiagnosisStatisticsController {

    @FXML private TableView<DiseaseStat> tvStatistics;
    @FXML private TableColumn<DiseaseStat, String> colIcdCode;
    @FXML private TableColumn<DiseaseStat, Integer> colCount;

    private DiagnosisDao diagnosisDao = new DiagnosisDao();

    @FXML
    public void initialize() {
        colIcdCode.setCellValueFactory(new PropertyValueFactory<>("icdCode"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        loadData();
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    private void loadData() {
        List<Object[]> data = diagnosisDao.getDiseaseStatistics();
        tvStatistics.getItems().clear();
        for (Object[] row : data) {
            tvStatistics.getItems().add(new DiseaseStat((String) row[0], (Integer) row[1]));
        }
    }

    public static class DiseaseStat {
        private final String icdCode;
        private final int count;
        public DiseaseStat(String icdCode, int count) {
            this.icdCode = icdCode;
            this.count = count;
        }
        public String getIcdCode() { return icdCode; }
        public int getCount() { return count; }
    }
}