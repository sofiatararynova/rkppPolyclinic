package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class SickLeave {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty visitId = new SimpleLongProperty();
    private final ObjectProperty<LocalDate> issueDate = new SimpleObjectProperty<>();
    private final StringProperty content = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public SickLeave() {}

    public SickLeave(long id, long visitId, LocalDate issueDate, String content, String status) {
        setId(id);
        setVisitId(visitId);
        setIssueDate(issueDate);
        setContent(content);
        setStatus(status);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getVisitId() { return visitId.get(); }
    public LongProperty visitIdProperty() { return visitId; }
    public void setVisitId(long visitId) { this.visitId.set(visitId); }

    public LocalDate getIssueDate() { return issueDate.get(); }
    public ObjectProperty<LocalDate> issueDateProperty() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate.set(issueDate); }

    public String getContent() { return content.get(); }
    public StringProperty contentProperty() { return content; }
    public void setContent(String content) { this.content.set(content); }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }
}