module ru.kafpin.rkpppolyclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;

    opens ru.kafpin.rkpppolyclinic to javafx.fxml;
    opens ru.kafpin.rkpppolyclinic.models to javafx.base;

    exports ru.kafpin.rkpppolyclinic;
}