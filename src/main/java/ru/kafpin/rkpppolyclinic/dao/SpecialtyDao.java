package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Specialty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialtyDao implements Dao<Specialty, Long> {

    private static final String FIND_BY_ID = "SELECT id, name, description FROM myschema.specialty WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, name, description FROM myschema.specialty";
    private static final String SAVE = "INSERT INTO myschema.specialty (name, description) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE myschema.specialty SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.specialty WHERE id = ?";

    @Override
    public Specialty findById(Long id) {
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
    public List<Specialty> findAll() {
        List<Specialty> list = new ArrayList<>();
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
    public void save(Specialty entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
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
    public void update(Specialty entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setLong(3, entity.getId());
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

    private Specialty mapRow(ResultSet rs) throws SQLException {
        return new Specialty(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}