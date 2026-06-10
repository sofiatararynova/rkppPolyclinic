package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}