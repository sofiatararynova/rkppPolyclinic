module ru.kafpin.rkpppolyclinic {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens ru.kafpin.rkpppolyclinic to javafx.fxml;
    exports ru.kafpin.rkpppolyclinic;
}