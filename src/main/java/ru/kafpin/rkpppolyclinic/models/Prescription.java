package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;

public class Prescription {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty visitId = new SimpleLongProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty medicationOrProcedure = new SimpleStringProperty();

    public Prescription() {}

    public Prescription(long id, long visitId, String description, String medicationOrProcedure) {
        setId(id);
        setVisitId(visitId);
        setDescription(description);
        setMedicationOrProcedure(medicationOrProcedure);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getVisitId() { return visitId.get(); }
    public LongProperty visitIdProperty() { return visitId; }
    public void setVisitId(long visitId) { this.visitId.set(visitId); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }

    public String getMedicationOrProcedure() { return medicationOrProcedure.get(); }
    public StringProperty medicationOrProcedureProperty() { return medicationOrProcedure; }
    public void setMedicationOrProcedure(String medicationOrProcedure) { this.medicationOrProcedure.set(medicationOrProcedure); }
}