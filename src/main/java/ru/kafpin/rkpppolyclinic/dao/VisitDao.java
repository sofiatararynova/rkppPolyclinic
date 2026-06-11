package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Visit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitDao implements Dao<Visit, Long> {

    private static final String FIND_ALL = "SELECT id, schedule_id, patient_id, date_time, status FROM myschema.visit";
    private static final String FIND_BY_ID = "SELECT id, schedule_id, patient_id, date_time, status FROM myschema.visit WHERE id = ?";
    private static final String SAVE = "INSERT INTO myschema.visit (schedule_id, patient_id, date_time, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.visit SET schedule_id = ?, patient_id = ?, date_time = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.visit WHERE id = ?";

    @Override
    public Visit findById(Long id) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(FIND_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Visit> findAll() {
        List<Visit> list = new ArrayList<>();
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Visit entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getScheduleId());
            stmt.setLong(2, entity.getPatientId());
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));
            stmt.setString(4, entity.getStatus());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Visit entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setLong(1, entity.getScheduleId());
            stmt.setLong(2, entity.getPatientId());
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));
            stmt.setString(4, entity.getStatus());
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Visit mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("date_time");
        return new Visit(
                rs.getLong("id"),
                rs.getLong("schedule_id"),
                rs.getLong("patient_id"),
                ts != null ? ts.toLocalDateTime() : null,
                rs.getString("status")
        );
    }
}
