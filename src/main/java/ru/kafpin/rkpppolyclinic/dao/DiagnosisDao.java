package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Diagnosis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDao {

    private static final String FIND_ALL = "SELECT sick_leave_id, icd_code FROM myschema.diagnosis";
    private static final String SAVE = "INSERT INTO myschema.diagnosis (sick_leave_id, icd_code) VALUES (?, ?)";
    //private static final String UPDATE = "UPDATE myschema.diagnosis SET icd_code = ? WHERE sick_leave_id = ?";
    private static final String DELETE = "DELETE FROM myschema.diagnosis WHERE sick_leave_id = ? AND icd_code = ?";

//    @Override
//    public Diagnosis findById(Long id) {
//        String sql = "SELECT sick_leave_id, icd_code FROM myschema.diagnosis WHERE sick_leave_id = ?";
//        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(sql)) {
//            stmt.setLong(1, id);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return new Diagnosis(rs.getLong("sick_leave_id"), rs.getString("icd_code"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    //@Override
    public List<Diagnosis> findAll() {
        List<Diagnosis> list = new ArrayList<>();
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {
            while (rs.next()) {
                list.add(new Diagnosis(rs.getLong("sick_leave_id"), rs.getString("icd_code")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //@Override
    public void save(Diagnosis entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE)) {
            stmt.setLong(1, entity.getSickLeaveId());
            stmt.setString(2, entity.getIcdCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void update(Diagnosis entity) {
//        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
//            stmt.setString(1, entity.getIcdCode());
//            stmt.setLong(2, entity.getSickLeaveId());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

   // @Override
    public void delete(Long sickLeaveId, String icdCode) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(DELETE)) {
            stmt.setLong(1, sickLeaveId);
            stmt.setString(2, icdCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}