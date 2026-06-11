package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDao implements Dao<Schedule, Long> {

    private static final String FIND_ALL = "SELECT id, doctor_id, appointment_date_time, appointment_status FROM myschema.schedule";
    private static final String FIND_BY_ID = "SELECT id, doctor_id, appointment_date_time, appointment_status FROM myschema.schedule WHERE id = ?";
    private static final String SAVE = "INSERT INTO myschema.schedule (doctor_id, appointment_date_time, appointment_status) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.schedule SET doctor_id = ?, appointment_date_time = ?, appointment_status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.schedule WHERE id = ?";

    @Override
    public Schedule findById(Long id) {
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
    public List<Schedule> findAll() {
        List<Schedule> list = new ArrayList<>();
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
    public void save(Schedule entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getAppointmentDateTime()));
            stmt.setString(3, entity.getAppointmentStatus());
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
    public void update(Schedule entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setLong(1, entity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getAppointmentDateTime()));
            stmt.setString(3, entity.getAppointmentStatus());
            stmt.setLong(4, entity.getId());
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

    private Schedule mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("appointment_date_time");
        return new Schedule(
                rs.getLong("id"),
                rs.getLong("doctor_id"),
                ts != null ? ts.toLocalDateTime() : null,
                rs.getString("appointment_status")
        );
    }
}
