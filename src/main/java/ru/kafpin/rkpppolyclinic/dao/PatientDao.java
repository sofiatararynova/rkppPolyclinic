package ru.kafpin.rkpppolyclinic.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.rkpppolyclinic.DBHelper;
import ru.kafpin.rkpppolyclinic.models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements Dao<Patient, Long> {

    private static final Logger log = LoggerFactory.getLogger(PatientDao.class);

    private static final String FIND_BY_ID = "SELECT id, last_name, first_name, middle_name, date_of_birth, residential_address FROM myschema.patient WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, last_name, first_name, middle_name, date_of_birth, residential_address FROM myschema.patient";
    private static final String SAVE = "INSERT INTO myschema.patient (last_name, first_name, middle_name, date_of_birth, residential_address) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE myschema.patient SET last_name = ?, first_name = ?, middle_name = ?, date_of_birth = ?, residential_address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM myschema.patient WHERE id = ?";

    @Override
    public Patient findById(Long id) {
        log.debug("Поиск пациента по id={}", id);
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(FIND_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Patient p = mapRow(rs);
                    log.debug("Пациент найден: {}", p.getLastName());
                    return p;
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске пациента id={}", id, e);
        }
        return null;
    }

    @Override
    public List<Patient> findAll() {
        log.debug("Запрос всех пациентов");
        List<Patient> list = new ArrayList<>();
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            log.debug("Найдено {} пациентов", list.size());
        } catch (SQLException e) {
            log.error("Ошибка при получении списка пациентов", e);
        }
        return list;
    }

    @Override
    public void save(Patient entity) {
        log.debug("Сохранение пациента: {} {}", entity.getLastName(), entity.getFirstName());
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
            log.info("Пациент сохранён с id={}", entity.getId());
        } catch (SQLException e) {
            log.error("Ошибка при сохранении пациента", e);
        }
    }

    @Override
    public void update(Patient entity) {
        log.debug("Обновление пациента id={}", entity.getId());
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(UPDATE)) {
            stmt.setString(1, entity.getLastName());
            stmt.setString(2, entity.getFirstName());
            stmt.setString(3, entity.getMiddleName());
            stmt.setDate(4, entity.getDateOfBirth() != null ? Date.valueOf(entity.getDateOfBirth()) : null);
            stmt.setString(5, entity.getResidentialAddress());
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();
            log.info("Пациент id={} обновлён", entity.getId());
        } catch (SQLException e) {
            log.error("Ошибка при обновлении пациента id={}", entity.getId(), e);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Удаление пациента id={}", id);
        try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            log.info("Пациент id={} удалён", id);
        } catch (SQLException e) {
            log.error("Ошибка при удалении пациента id={}", id, e);
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