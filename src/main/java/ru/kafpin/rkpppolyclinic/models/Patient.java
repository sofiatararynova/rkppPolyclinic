package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Patient {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>();
    private final StringProperty residentialAddress = new SimpleStringProperty();

    public Patient() {}

    public Patient(long id, String lastName, String firstName, String middleName, LocalDate dateOfBirth, String residentialAddress) {
        setId(id);
        setLastName(lastName);
        setFirstName(firstName);
        setMiddleName(middleName);
        setDateOfBirth(dateOfBirth);
        setResidentialAddress(residentialAddress);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public String getLastName() { return lastName.get(); }
    public StringProperty lastNameProperty() { return lastName; }
    public void setLastName(String lastName) { this.lastName.set(lastName); }

    public String getFirstName() { return firstName.get(); }
    public StringProperty firstNameProperty() { return firstName; }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }

    public String getMiddleName() { return middleName.get(); }
    public StringProperty middleNameProperty() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName.set(middleName); }

    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth.set(dateOfBirth); }

    public String getResidentialAddress() { return residentialAddress.get(); }
    public StringProperty residentialAddressProperty() { return residentialAddress; }
    public void setResidentialAddress(String residentialAddress) { this.residentialAddress.set(residentialAddress); }

    @Override
    public String toString() { return getLastName() + " " + getFirstName(); }
}