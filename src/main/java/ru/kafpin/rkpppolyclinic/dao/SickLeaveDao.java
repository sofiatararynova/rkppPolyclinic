package ru.kafpin.rkpppolyclinic.dao;

import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.SickLeave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SickLeaveDao implements Dao<SickLeave, Long> {

    private static final String FIND_BY_ID = "SELECT id, visit_id, issue_date, content, status FROM myschema.sick_leave WHERE id = ?";
    private static final String SAVE = "INSERT INTO myschema.sick_leave (visit_id, issue_date, content, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.sick_leave SET visit_id = ?, issue_date = ?, content = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.sick_leave WHERE id = ?";

    @Override
    public SickLeave findById(Long id) {
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
    public List<SickLeave> findAll() {
        List<SickLeave> list = new ArrayList<>();
        String sql =
                "SELECT sl.id, sl.visit_id, sl.issue_date, sl.content, sl.status, " +
                        "p.last_name || ' ' || p.first_name AS patient_full_name " +
                        "FROM myschema.sick_leave sl " +
                        "LEFT JOIN myschema.visit v ON sl.visit_id = v.id " +
                        "LEFT JOIN myschema.patient p ON v.patient_id = p.id";
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SickLeave sl = new SickLeave(
                        rs.getLong("id"),
                        rs.getLong("visit_id"),
                        rs.getDate("issue_date") != null ? rs.getDate("issue_date").toLocalDate() : null,
                        rs.getString("content"),
                        rs.getString("status")
                );
                sl.setPatientFullName(rs.getString("patient_full_name"));
                list.add(sl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(SickLeave entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getVisitId());
            stmt.setDate(2, entity.getIssueDate() != null ? Date.valueOf(entity.getIssueDate()) : null);
            stmt.setString(3, entity.getContent());
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
    public void update(SickLeave entity) {
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setLong(1, entity.getVisitId());
            stmt.setDate(2, entity.getIssueDate() != null ? Date.valueOf(entity.getIssueDate()) : null);
            stmt.setString(3, entity.getContent());
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

    private SickLeave mapRow(ResultSet rs) throws SQLException {
        Date date = rs.getDate("issue_date");
        return new SickLeave(
                rs.getLong("id"),
                rs.getLong("visit_id"),
                date != null ? date.toLocalDate() : null,
                rs.getString("content"),
                rs.getString("status")
        );
    }

    public SickLeave findByVisitId(Long visitId) {
        String sql = "SELECT id, visit_id, issue_date, content, status FROM myschema.sick_leave WHERE visit_id = ?";
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, visitId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}