package ru.kafpin.rkpppolyclinic.models;

import java.time.LocalDateTime;

public class DoctorPatientVisit {
    private final Long patientId;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final LocalDateTime visitDate;

    public DoctorPatientVisit(Long patientId, String lastName, String firstName, String middleName, LocalDateTime visitDate) {
        this.patientId = patientId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.visitDate = visitDate;
    }

    public Long getPatientId() { return patientId; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public LocalDateTime getVisitDate() { return visitDate; }
}