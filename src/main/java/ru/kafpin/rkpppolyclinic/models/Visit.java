package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Visit {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty scheduleId = new SimpleLongProperty();
    private final LongProperty patientId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();

    public Visit() {}

    public Visit(long id, long scheduleId, long patientId, LocalDateTime dateTime, String status) {
        setId(id);
        setScheduleId(scheduleId);
        setPatientId(patientId);
        setDateTime(dateTime);
        setStatus(status);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getScheduleId() { return scheduleId.get(); }
    public LongProperty scheduleIdProperty() { return scheduleId; }
    public void setScheduleId(long scheduleId) { this.scheduleId.set(scheduleId); }

    public long getPatientId() { return patientId.get(); }
    public LongProperty patientIdProperty() { return patientId; }
    public void setPatientId(long patientId) { this.patientId.set(patientId); }

    public LocalDateTime getDateTime() { return dateTime.get(); }
    public ObjectProperty<LocalDateTime> dateTimeProperty() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime.set(dateTime); }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }
}