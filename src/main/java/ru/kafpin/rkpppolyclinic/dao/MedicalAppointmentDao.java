package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.MedicalAppointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalAppointmentDao implements Dao<MedicalAppointment, Long> {

    private static final String FIND_ALL = "SELECT id, doctor_id, appointment_date_time, appointment_status FROM myschema.schedule";
    private static final String SAVE = "INSERT INTO myschema.schedule (doctor_id, appointment_date_time, appointment_status) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.schedule SET doctor_id = ?, appointment_date_time = ?, appointment_status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.schedule WHERE id = ?";

    @Override
    public MedicalAppointment findById(Long id) {
        String sql = "SELECT id, doctor_id, appointment_date_time, appointment_status FROM myschema.schedule WHERE id = ?";
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(sql)) {
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
    public List<MedicalAppointment> findAll() {
        List<MedicalAppointment> list = new ArrayList<>();
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
    public void save(MedicalAppointment entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getAppointmentDateTime()));
            stmt.setString(3, entity.getStatusText());
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
    public void update(MedicalAppointment entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setLong(1, entity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getAppointmentDateTime()));
            stmt.setString(3, entity.getStatusText());
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

    private MedicalAppointment mapRow(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("appointment_date_time");
        return new MedicalAppointment(
                rs.getLong("id"),
                0, // В таблице schedule нет поля patient_id, оставляем 0
                rs.getLong("doctor_id"),
                timestamp != null ? timestamp.toLocalDateTime() : null,
                "-", // В таблице schedule нет поля office_number, ставим прочерк
                rs.getString("appointment_status")
        );
    }
}