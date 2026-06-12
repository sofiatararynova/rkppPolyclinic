package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Doctor;
import ru.kafpin.rkpppolyclinic.models.DoctorPatientVisit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorDao implements Dao<Doctor, Long> {

    private static final String FIND_BY_ID = "SELECT id, specialty_id, last_name, first_name, middle_name, specialty_text, experience_years, date_of_birth, residential_address FROM myschema.doctor WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, specialty_id, last_name, first_name, middle_name, specialty_text, experience_years, date_of_birth, residential_address FROM myschema.doctor";
    private static final String SAVE = "INSERT INTO myschema.doctor (specialty_id, last_name, first_name, middle_name, specialty_text, experience_years, date_of_birth, residential_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.doctor SET specialty_id = ?, last_name = ?, first_name = ?, middle_name = ?, specialty_text = ?, experience_years = ?, date_of_birth = ?, residential_address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.doctor WHERE id = ?";

    @Override
    public Doctor findById(Long id) {
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
    public List<Doctor> findAll() {
        List<Doctor> list = new ArrayList<>();
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
    public void save(Doctor entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            if (entity.getSpecialtyId() == 0) stmt.setNull(1, Types.INTEGER); else stmt.setLong(1, entity.getSpecialtyId());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getFirstName());
            stmt.setString(4, entity.getMiddleName());
            stmt.setString(5, entity.getSpecialtyText());
            stmt.setInt(6, entity.getExperienceYears());
            stmt.setDate(7, entity.getDateOfBirth() != null ? Date.valueOf(entity.getDateOfBirth()) : null);
            stmt.setString(8, entity.getResidentialAddress());
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
    public void update(Doctor entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            if (entity.getSpecialtyId() == 0) stmt.setNull(1, Types.INTEGER); else stmt.setLong(1, entity.getSpecialtyId());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getFirstName());
            stmt.setString(4, entity.getMiddleName());
            stmt.setString(5, entity.getSpecialtyText());
            stmt.setInt(6, entity.getExperienceYears());
            stmt.setDate(7, entity.getDateOfBirth() != null ? Date.valueOf(entity.getDateOfBirth()) : null);
            stmt.setString(8, entity.getResidentialAddress());
            stmt.setLong(9, entity.getId());
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

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Date dbDate = rs.getDate("date_of_birth");
        return new Doctor(
                rs.getLong("id"),
                rs.getLong("specialty_id"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("specialty_text"),
                rs.getInt("experience_years"),
                dbDate != null ? dbDate.toLocalDate() : null,
                rs.getString("residential_address")
        );
    }

    public List<DoctorPatientVisit> getPatientsByDoctor(Long doctorId, LocalDate startDate, LocalDate endDate) {
        List<DoctorPatientVisit> result = new ArrayList<>();
        String sql = "{call myschema.get_patients_by_doctor(?, ?, ?)}";
        try (CallableStatement stmt = DBHelper.getConnection().prepareCall(sql)) {
            stmt.setLong(1, doctorId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new DoctorPatientVisit(
                        rs.getLong("patient_id"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getTimestamp("visit_date") != null ? rs.getTimestamp("visit_date").toLocalDateTime() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Object[]> getVisitsCountByDoctor(LocalDate startDate, LocalDate endDate) {
        List<Object[]> result = new ArrayList<>();
        String sql =
                "SELECT d.id, d.last_name || ' ' || d.first_name AS doctor_name, " +
                        "COUNT(CASE WHEN v.status = 'завершён' THEN 1 END) AS completed, " +
                        "COUNT(CASE WHEN v.status = 'запланирован' THEN 1 END) AS planned, " +
                        "COUNT(CASE WHEN v.status = 'отменён' THEN 1 END) AS cancelled " +
                        "FROM myschema.doctor d " +
                        "LEFT JOIN myschema.schedule s ON d.id = s.doctor_id " +
                        "LEFT JOIN myschema.visit v ON s.id = v.schedule_id " +
                        "WHERE (v.date_time BETWEEN ? AND ? OR v.id IS NULL) " +
                        "GROUP BY d.id, d.last_name, d.first_name " +
                        "ORDER BY d.last_name";
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{
                        rs.getLong("id"),
                        rs.getString("doctor_name"),
                        rs.getInt("completed"),
                        rs.getInt("planned"),
                        rs.getInt("cancelled")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}