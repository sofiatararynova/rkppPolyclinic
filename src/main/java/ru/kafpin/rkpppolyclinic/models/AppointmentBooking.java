package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class AppointmentBooking {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty patientId = new SimpleLongProperty();
    private final LongProperty scheduleId = new SimpleLongProperty();
    private final LongProperty administratorId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final StringProperty bookingStatus = new SimpleStringProperty();

    public AppointmentBooking() {}

    public AppointmentBooking(long id, long patientId, long scheduleId, long administratorId, LocalDateTime createdAt, String bookingStatus) {
        setId(id);
        setPatientId(patientId);
        setScheduleId(scheduleId);
        setAdministratorId(administratorId);
        setCreatedAt(createdAt);
        setBookingStatus(bookingStatus);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getPatientId() { return patientId.get(); }
    public LongProperty patientIdProperty() { return patientId; }
    public void setPatientId(long patientId) { this.patientId.set(patientId); }

    public long getScheduleId() { return scheduleId.get(); }
    public LongProperty scheduleIdProperty() { return scheduleId; }
    public void setScheduleId(long scheduleId) { this.scheduleId.set(scheduleId); }

    public long getAdministratorId() { return administratorId.get(); }
    public LongProperty administratorIdProperty() { return administratorId; }
    public void setAdministratorId(long administratorId) { this.administratorId.set(administratorId); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public String getBookingStatus() { return bookingStatus.get(); }
    public StringProperty bookingStatusProperty() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus.set(bookingStatus); }
}