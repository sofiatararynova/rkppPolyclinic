package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;

public class Specialty {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public Specialty() {}

    public Specialty(long id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }

    @Override
    public String toString() { return getName(); }
}