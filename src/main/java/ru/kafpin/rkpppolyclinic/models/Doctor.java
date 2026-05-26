package ru.kafpin.rkpppolyclinic.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Doctor {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty specialtyId = new SimpleLongProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty middleName = new SimpleStringProperty();
    private final StringProperty specialtyText = new SimpleStringProperty();
    private final IntegerProperty experienceYears = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>();
    private final StringProperty residentialAddress = new SimpleStringProperty();

    public Doctor() {}

    public Doctor(long id, long specialtyId, String lastName, String firstName, String middleName,
                  String specialtyText, int experienceYears, LocalDate dateOfBirth, String residentialAddress) {
        setId(id);
        setSpecialtyId(specialtyId);
        setLastName(lastName);
        setFirstName(firstName);
        setMiddleName(middleName);
        setSpecialtyText(specialtyText);
        setExperienceYears(experienceYears);
        setDateOfBirth(dateOfBirth);
        setResidentialAddress(residentialAddress);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getSpecialtyId() { return specialtyId.get(); }
    public LongProperty specialtyIdProperty() { return specialtyId; }
    public void setSpecialtyId(long specialtyId) { this.specialtyId.set(specialtyId); }

    public String getLastName() { return lastName.get(); }
    public StringProperty lastNameProperty() { return lastName; }
    public void setLastName(String lastName) { this.lastName.set(lastName); }

    public String getFirstName() { return firstName.get(); }
    public StringProperty firstNameProperty() { return firstName; }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }

    public String getMiddleName() { return middleName.get(); }
    public StringProperty middleNameProperty() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName.set(middleName); }

    public String getSpecialtyText() { return specialtyText.get(); }
    public StringProperty specialtyTextProperty() { return specialtyText; }
    public void setSpecialtyText(String specialtyText) { this.specialtyText.set(specialtyText); }

    public int getExperienceYears() { return experienceYears.get(); }
    public IntegerProperty experienceYearsProperty() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears.set(experienceYears); }

    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth.set(dateOfBirth); }

    public String getResidentialAddress() { return residentialAddress.get(); }
    public StringProperty residentialAddressProperty() { return residentialAddress; }
    public void setResidentialAddress(String residentialAddress) { this.residentialAddress.set(residentialAddress); }

    @Override
    public String toString() { return getLastName() + " " + getFirstName(); }
}