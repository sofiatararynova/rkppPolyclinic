package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class MedicalAppointment {
    private LongProperty id;
    private LongProperty patientId;
    private LongProperty doctorId;
    private ObjectProperty<LocalDateTime> appointmentDateTime;
    private StringProperty officeNumber;
    private StringProperty statusText;

    public MedicalAppointment(long id, long patientId, long doctorId, LocalDateTime appointmentDateTime, String officeNumber, String statusText) {
        this.id = new SimpleLongProperty(id);
        this.patientId = new SimpleLongProperty(patientId);
        this.doctorId = new SimpleLongProperty(doctorId);
        this.appointmentDateTime = new SimpleObjectProperty<>(appointmentDateTime);
        this.officeNumber = new SimpleStringProperty(officeNumber);
        this.statusText = new SimpleStringProperty(statusText);
    }

    public MedicalAppointment() {
        this.id = new SimpleLongProperty();
        this.patientId = new SimpleLongProperty();
        this.doctorId = new SimpleLongProperty();
        this.appointmentDateTime = new SimpleObjectProperty<>();
        this.officeNumber = new SimpleStringProperty();
        this.statusText = new SimpleStringProperty();
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getPatientId() { return patientId.get(); }
    public LongProperty patientIdProperty() { return patientId; }
    public void setPatientId(long patientId) { this.patientId.set(patientId); }

    public long getDoctorId() { return doctorId.get(); }
    public LongProperty doctorIdProperty() { return doctorId; }
    public void setDoctorId(long doctorId) { this.doctorId.set(doctorId); }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime.get(); }
    public ObjectProperty<LocalDateTime> appointmentDateTimeProperty() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime.set(appointmentDateTime); }

    public String getOfficeNumber() { return officeNumber.get(); }
    public StringProperty officeNumberProperty() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber.set(officeNumber); }

    public String getStatusText() { return statusText.get(); }
    public StringProperty statusTextProperty() { return statusText; }
    public void setStatusText(String statusText) { this.statusText.set(statusText); }
}