package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements Dao<Patient, Long> {

    private static final String FIND_BY_ID = "SELECT id, last_name, first_name, middle_name, date_of_birth, residential_address FROM myschema.patient WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, last_name, first_name, middle_name, date_of_birth, residential_address FROM myschema.patient";
    private static final String SAVE = "INSERT INTO myschema.patient (last_name, first_name, middle_name, date_of_birth, residential_address) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.patient SET last_name = ?, first_name = ?, middle_name = ?, date_of_birth = ?, residential_address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.patient WHERE id = ?";

    @Override
    public Patient findById(Long id) {
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
    public List<Patient> findAll() {
        List<Patient> list = new ArrayList<>();
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
    public void save(Patient entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getLastName());
            stmt.setString(2, entity.getFirstName());
            stmt.setString(3, entity.getMiddleName());
            stmt.setDate(4, entity.getDateOfBirth() != null ? Date.valueOf(entity.getDateOfBirth()) : null);
            stmt.setString(5, entity.getResidentialAddress());
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
    public void update(Patient entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setString(1, entity.getLastName());
            stmt.setString(2, entity.getFirstName());
            stmt.setString(3, entity.getMiddleName());
            stmt.setDate(4, entity.getDateOfBirth() != null ? Date.valueOf(entity.getDateOfBirth()) : null);
            stmt.setString(5, entity.getResidentialAddress());
            stmt.setLong(6, entity.getId());
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

    private Patient mapRow(ResultSet rs) throws SQLException {
        Date dbDate = rs.getDate("date_of_birth");
        return new Patient(
                rs.getLong("id"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                dbDate != null ? dbDate.toLocalDate() : null,
                rs.getString("residential_address")
        );
    }
}