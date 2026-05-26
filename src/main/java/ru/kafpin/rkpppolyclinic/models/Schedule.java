package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Schedule {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty doctorId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> appointmentDateTime = new SimpleObjectProperty<>();
    private final StringProperty appointmentStatus = new SimpleStringProperty();

    public Schedule() {}

    public Schedule(long id, long doctorId, LocalDateTime appointmentDateTime, String appointmentStatus) {
        setId(id);
        setDoctorId(doctorId);
        setAppointmentDateTime(appointmentDateTime);
        setAppointmentStatus(appointmentStatus);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getDoctorId() { return doctorId.get(); }
    public LongProperty doctorIdProperty() { return doctorId; }
    public void setDoctorId(long doctorId) { this.doctorId.set(doctorId); }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime.get(); }
    public ObjectProperty<LocalDateTime> appointmentDateTimeProperty() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime.set(appointmentDateTime); }

    public String getAppointmentStatus() { return appointmentStatus.get(); }
    public StringProperty appointmentStatusProperty() { return appointmentStatus; }
    public void setAppointmentStatus(String appointmentStatus) { this.appointmentStatus.set(appointmentStatus); }
}