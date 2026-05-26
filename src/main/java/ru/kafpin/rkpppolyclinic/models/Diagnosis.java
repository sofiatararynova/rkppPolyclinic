package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;

public class Diagnosis {
    private final LongProperty sickLeaveId = new SimpleLongProperty();
    private final StringProperty icdCode = new SimpleStringProperty();

    public Diagnosis() {}

    public Diagnosis(long sickLeaveId, String icdCode) {
        setSickLeaveId(sickLeaveId);
        setIcdCode(icdCode);
    }

    public long getSickLeaveId() { return sickLeaveId.get(); }
    public LongProperty sickLeaveIdProperty() { return sickLeaveId; }
    public void setSickLeaveId(long sickLeaveId) { this.sickLeaveId.set(sickLeaveId); }

    public String getIcdCode() { return icdCode.get(); }
    public StringProperty icdCodeProperty() { return icdCode; }
    public void setIcdCode(String icdCode) { this.icdCode.set(icdCode); }
}